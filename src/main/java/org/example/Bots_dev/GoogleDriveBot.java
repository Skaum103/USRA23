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

        // Generate dummy files
        System.out.println("Please enter the total number of small, medium and large files.");
        int nSFiles = in.nextInt();
        int nMFiles = in.nextInt();
        int nLFiles = in.nextInt();
        makeSFiles(nSFiles);
        makeMFiles(nMFiles);
        makeLFiles(nLFiles);

        // Get the gDrive folder ID to upload to
        System.out.println("Please enter the gdrive folder ID");
        String folderID = in.next();

        // Start tshark to capture traffic
        Thread tshark = new Thread(new Tshark("cap.pcapng"));
        tshark.start();
        // Start the bots, each bot will run for 1/3 of the total running time
        startBot(runTime/3,nSFiles,"sFiles/sFile#",folderID);
        startBot(runTime/3,nMFiles,"mFiles/mFile#",folderID);
        startBot(runTime/3,nLFiles,"lFiles/lFile#",folderID);

        // Parse the log file
        File report = new File("gDriveReport.txt");
        FileWriter fwReport = new FileWriter(report);
        String s = GdriveLogParser.parseGDriveLog("sFiles/sFile#GoogleDriveUploadLog.txt");
        String m = GdriveLogParser.parseGDriveLog("mFiles/mFile#GoogleDriveUploadLog.txt");
        String l = GdriveLogParser.parseGDriveLog("lFiles/lFile#GoogleDriveUploadLog.txt");

        // Write the report
        fwReport.write(s);
        fwReport.write(m);
        fwReport.write(l);
        fwReport.close();
    }


    /**
     * Start the bot to upload dummy files to google Drive using the gDrive tool
     * @param runTime the total running time
     * @param listSize the total number of files
     * @param path the path of the files
     * @param folderID the gDrive folder ID
     * @throws IOException if the file is not found
     * @throws InterruptedException if the process is interrupted
     */
    public static void startBot(int runTime, int listSize, String path, String folderID)
            throws IOException, InterruptedException {
        System.out.println("Starting bot");

        // Initialize upload log writer
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
        // While the current time is before the end time
        // Execute the command to upload a file
        while (time.isBefore(endTime)) {
            testUpload(path+fileNo,folderID);
            fileNo = generateRandomSelect(listSize);
            time = LocalDateTime.now();
        }
        fw_upload.close();
    }


    /**
     * Upload a file to google Drive using the gDrive tool
     * @param path the path of the file
     * @param folderID the gDrive folder ID
     * @throws IOException if the file is not found
     * @throws InterruptedException if the process is interrupted
     */
    public static void testUpload(String path, String folderID) throws IOException, InterruptedException {
        String command = "gdrive files upload --parent " + folderID + " " + path;
        System.out.println(command);
        exec(command,fw_upload);

        fw_upload.write("----------------------\n");
    }


    /**
     * Download a file from Google Drive using the gDrive tool
     * @throws IOException if the file is not found
     * @throws InterruptedException if the process is interrupted
     */
    public static void testDownload() throws IOException, InterruptedException {
        fw_download.write("Working Directory = " + System.getProperty("user.dir") + "\n");
        String command = "gdrive files download ".concat(viewURL);
        exec(command,fw_download);
        System.out.println("Done.");
    }


    /**
     * Execute a command in the terminal
     * @param command command to be executed
     * @param fw the file writer to write the output to
     * @return the execution time
     * @throws IOException if the file is not found
     * @throws InterruptedException if the process is interrupted
     */
    private static float exec(String command, FileWriter fw) throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        Process proc = Runtime.getRuntime().exec(command);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line;
        // Extract the file view URL from the output
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


    /**
     * Generate a dummy file
     * @param index the index of the file
     * @param lowerBound the lower bound of the file size
     * @param scale the scale of the file size
     * @param path the path of the file
     * @throws IOException if the file is not found
     */
    public static void makeFile(int index, int lowerBound, int scale, String path) throws IOException {
        double size = util.generateRandom(lowerBound,scale);
        RandomAccessFile file = new RandomAccessFile(path + "File#" + index,"rw");
        file.setLength((long) (size *1024*1024));
    }


    /**
     * Generate small dummy files from 1-3 MB
     * @param count the number of files
     * @throws IOException if the file is not found
     */
    public static void makeSFiles(int count) throws IOException {
        for (int i = 0; i < count; i++) {
            makeFile(i,1,3,"sFiles/s");
        }
    }


    /**
     * Generate medium dummy files from 3-100 MB
     * @param count the number of files
     * @throws IOException if the file is not found
     */
    public static void makeMFiles(int count) throws IOException {
        for (int i = 0; i < count; i++) {
            makeFile(i,3,100,"mFiles/m");
        }
    }


    /**
     * Generate large dummy files from 100-512 MB
     * @param count the number of files
     * @throws IOException if the file is not found
     */
    public static void makeLFiles(int count) throws IOException {
        for (int i = 0; i < count; i++) {
            makeFile(i,100,512,"lFiles/l");
        }
    }
}
