package org.example.Bots_dev;

import org.example.Utils.Tshark;

import java.io.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.example.Utils.util.*;

public class TwitchBot {

    static File log;
    static FileWriter fw;


    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the total running time.");
        int runTime = in.nextInt();

        startBot(runTime);
    }


    public static void startBot(int runTime) throws IOException, InterruptedException {
        System.out.println("Starting bot");
        // Initialize a log writer
        log = new File("StreamingLog.txt");
        fw = new FileWriter(log);

        // Total browsing time
        int remainTime = runTime;

        // Read websites
        ArrayList<String> channels = readURL("channels.csv");

        LocalDateTime time = LocalDateTime.now();
        int timeToVisit = 3650;
        int channelNo = 0;
        String channelURL = channels.get(channelNo).split(",")[1];

        // Wait for remote control disconnect
        waitRemoteDisconnect();

        // Start tshark
        Thread tshark = new Thread(new Tshark("streamingCapRes10_1111.pcapng"));
        tshark.start();

        while (remainTime > 0) {
            if (remainTime > timeToVisit) {
                LocalDateTime endTime = time.plusSeconds(timeToVisit);
                playStream(channelURL,endTime);
                remainTime -= timeToVisit;
            }
            else {
                LocalDateTime endTime = time.plusSeconds(remainTime);
                playStream(channelURL,endTime);
                remainTime = 0;
            }
            timeToVisit = 3650;
            time = LocalDateTime.now();
            channelNo = 0;
            channelURL = channels.get(channelNo);
        }
        fw.flush();
        fw.close();

        System.out.println("Done.");
    }


    static void twitchChat() {
                /*

        // chat credential
        OAuth2Credential credential = new OAuth2Credential("twitch", "gyzwotojqfdx1k3upyazd3j7rxie18");

        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withChatAccount(credential)
                .withEnableChat(true)
                .withEnableHelix(true)
                .build();

        twitchClient.getChat().sendMessage("skaum103", "Hey!");

         */
    }


    static void playStream(String channelURL, LocalDateTime endTime) throws IOException, InterruptedException {
        // Logging
        System.out.println(LocalDateTime.now() + " Going to visit " + channelURL + " until " + endTime);
        fw.write(LocalDateTime.now() + " Going to visit " + channelURL + " until " + endTime + "\n");

        // Browsing, scroll to the bottom of the website to ensure all elements are loaded
        String command = "streamlink " + channelURL + " 720p60";
        Process proc = Runtime.getRuntime().exec(command);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
            fw.write(line + "\n");
            fw.flush();
        }

        // Sleep until time up
        int sleepDuration = (int) ChronoUnit.SECONDS.between(LocalDateTime.now(), endTime);
        fw.flush();
        TimeUnit.SECONDS.sleep(sleepDuration);
        proc.destroy();
    }



}
