package org.example.Bots_dev;

import org.example.Utils.Tshark;
import org.example.Utils.GdriveLogParser;
import org.example.Utils.util;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

import static org.example.Utils.util.generateRandomSelect;

public class GoogleDriveBot {

    static File uploadLog;
    static FileWriter fw_upload;
    static File downloadLog;
    static FileWriter fw_download;
    static String viewURL;

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the total running time.");
        int runTime = in.nextInt();

        System.out.println("Please enter the total number of small, medium and large files.");
        int nSFiles = in.nextInt();
        int nMFiles = in.nextInt();
        int nLFiles = in.nextInt();
        makeSFiles(nSFiles);
        makeMFiles(nMFiles);
        makeLFiles(nLFiles);

        System.out.println("Please enter the gdrive folder ID");
        String folderID = in.next();

        Thread tshark = new Thread(new Tshark("cap.pcapng"));
        tshark.start();
        startBot(runTime/3,nSFiles,"sFiles/sFile#",folderID);
        startBot(runTime/3,nMFiles,"mFiles/mFile#",folderID);
        startBot(runTime/3,nLFiles,"lFiles/lFile#",folderID);


        File report = new File("gDriveReport.txt");
        FileWriter fwReport = new FileWriter(report);
        String s = GdriveLogParser.parseGDriveLog("sFiles/sFile#GoogleDriveUploadLog.txt");
        String m = GdriveLogParser.parseGDriveLog("mFiles/mFile#GoogleDriveUploadLog.txt");
        String l = GdriveLogParser.parseGDriveLog("lFiles/lFile#GoogleDriveUploadLog.txt");

        fwReport.write(s);
        fwReport.write(m);
        fwReport.write(l);

        fwReport.close();

    }


    public static void startBot(int runTime, int listSize, String path, String folderID)
            throws IOException, InterruptedException {
        System.out.println("Starting bot");

        uploadLog =  new File(path + "GoogleDriveUploadLog.txt");
        try {
            fw_upload = new FileWriter(uploadLog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LocalDateTime time = LocalDateTime.now();
        LocalDateTime endTime = time.plusSeconds(runTime);
        System.out.println("Executing until " + endTime);
        int fileNo = generateRandomSelect(listSize);
        while (time.isBefore(endTime)) {
            testUpload(path+fileNo,folderID);
            fileNo = generateRandomSelect(listSize);
            time = LocalDateTime.now();
        }
        fw_upload.close();


        /*
        
        downloadLog =  new File("GoogleDriveDownloadLog.txt");
        try {
            fw_download = new FileWriter(downloadLog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fw_download.close();

         */
    }

    public static void testUpload(String path, String folderID) throws IOException, InterruptedException {

        String command = "gdrive files upload --parent " + folderID + " " + path;
        System.out.println(command);
        exec(command,fw_upload);

        fw_upload.write("----------------------\n");
    }

    public static void testDownload() throws IOException, InterruptedException {
        fw_download.write("Working Directory = " + System.getProperty("user.dir") + "\n");

        String command = "gdrive files download ".concat(viewURL);
        exec(command,fw_download);

        System.out.println("Done.");
    }

    private static float exec(String command, FileWriter fw) throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        Process proc = Runtime.getRuntime().exec(command);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = "";
        while((line = reader.readLine()) != null) {
            if (line.contains("ViewUrl: ")) {
                viewURL = line.substring(41,74);
            }
            System.out.println(line);
            fw.write(line + "\n");
        }
        proc.waitFor();

        long end = System.currentTimeMillis();
        float sec = (end - start) / 1000F;
        fw.write("Execution completed in " + sec + " seconds\n");
        return sec;
    }


    public static void makeFile(int index, int lowerBound, int scale, String path) throws IOException {
        double size = util.generateRandom(lowerBound,scale);
        RandomAccessFile file = new RandomAccessFile(path + "File#" + index,"rw");
        file.setLength((long) (size *1024*1024));
    }

    public static void makeSFiles(int count) throws IOException {
        for (int i = 0; i < count; i++) {
            makeFile(i,1,3,"sFiles/s");
        }
    }

    public static void makeMFiles(int count) throws IOException {
        for (int i = 0; i < count; i++) {
            makeFile(i,3,100,"mFiles/m");
        }
    }

    public static void makeLFiles(int count) throws IOException {
        for (int i = 0; i < count; i++) {
            makeFile(i,100,512,"lFiles/l");
        }
    }
}
