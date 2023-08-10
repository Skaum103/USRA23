package org.example.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.example.Utils.util.cmdExec;

public class GdriveLogParser {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        String path = in.nextLine();
        parseGDriveLog(path);
    }

    public static String parseGDriveLog(String path) throws IOException {
        File gDriveLog = new File(path);
        Scanner reader = new Scanner(gDriveLog);

        int totalUploadCount = 0;
        double totalUploadData = 0;
        float totalTime = 0;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.contains("Size:")) {
                totalUploadCount++;
                totalUploadData += Double.parseDouble(line.substring(6,line.length()-3));
            }
        }

        totalUploadData = Math.round(totalUploadData * 100.0) / 100.0;
        double avgSpeed = Math.round((totalUploadData / 1200) * 100.0) / 100.0;
        System.out.println(path);
        System.out.println(totalUploadCount + " Files are uploaded");
        System.out.println(totalUploadData + " MB of data are uploaded");
        System.out.println("Ave Speed: " + avgSpeed + " MB/s");
        System.out.println("---------------------");

        return path + "\n" + totalUploadCount + " Files are uploaded" + "\n"
                + totalUploadData + " MB of data are uploaded" + "\n"
                + "Ave Speed: " + avgSpeed +  " MB/s" + "\n" + "---------------------" + "\n";

    }

    public static void parsePcap(String path) throws IOException, InterruptedException {
        util.replaceLine("capinfos.sh","capinfos " + path);
        cmdExec("./capinfos.sh");
    }


}
