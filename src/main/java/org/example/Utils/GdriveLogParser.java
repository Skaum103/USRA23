package org.example.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GdriveLogParser {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        String path = in.nextLine();
        parseGDriveLog(path);
    }

    /**
     * Parse the GDrive log file and print the upload speed
     * @param path the path of the log file
     * @return A string contains info of the upload
     * @throws IOException if the file is not found
     */
    public static String parseGDriveLog(String path) throws IOException {
        File gDriveLog = new File(path);
        Scanner reader = new Scanner(gDriveLog);

        // Parse the log file
        int totalUploadCount = 0;
        double totalUploadData = 0;
        float totalTime = 1200;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.contains("Size:")) {
                totalUploadCount++;
                totalUploadData += Double.parseDouble(line.substring(6,line.length()-3));
            }
        }

        // Calculate the upload speed
        totalUploadData = Math.round(totalUploadData * 100.0) / 100.0;
        double avgSpeed = Math.round((totalUploadData / totalTime) * 100.0) / 100.0;
        System.out.println(path);
        System.out.println(totalUploadCount + " Files are uploaded");
        System.out.println(totalUploadData + " MB of data are uploaded");
        System.out.println("Avg Speed: " + avgSpeed + " MB/s");
        System.out.println("---------------------");

        return path + "\n" + totalUploadCount + " Files are uploaded" + "\n"
                + totalUploadData + " MB of data are uploaded" + "\n"
                + "Ave Speed: " + avgSpeed +  " MB/s" + "\n" + "---------------------" + "\n";

    }
}
