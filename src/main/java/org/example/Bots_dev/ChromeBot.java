package org.example.Bots_dev;

import org.example.Utils.Tshark;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.example.Utils.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.example.Utils.util.*;

public class ChromeBot {

    static String https = "https://";
    static ChromeDriverService chromeDriverService;
    static WebDriver driver;
    static  JavascriptExecutor js;
    static File log;
    static FileWriter fw;

    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the total running time.");
        int runTime = in.nextInt();
        startBot(runTime);
    }

    public static void startBot(int runTime) throws InterruptedException, IOException {
        System.out.println("Starting bot");
        // Initialize a log writer
        log = new File("BrowsingLog.txt");
        fw = new FileWriter(log);

        // Total browsing time
        int remainTime = runTime;

        // Read websites
        ArrayList<String> websites = readURL("websites.csv");


        // Initialize a Chrome Driver
        chromeDriverService = new ChromeDriverService.Builder().usingPort(23466).build();
        driver = new ChromeDriver(chromeDriverService);
        js = (JavascriptExecutor) driver;

        // Wait for remote control disconnection
        waitRemoteDisconnect();

        // Init Tshark
        Thread tshark = new Thread(new Tshark("browsingCapRes10_1111.pcapng"));
        tshark.start();

        LocalDateTime time = LocalDateTime.now();
        int timeToVisit = generateRandom(300);
        int websiteNo = generateRandomSelect(1000);
        String websiteURL = formatWebsiteURL(websites.get(websiteNo));

        while (remainTime > 0) {
            if (remainTime > timeToVisit) {
                LocalDateTime endTime = time.plusSeconds(timeToVisit);
                browse(websiteURL,endTime);
                remainTime -= timeToVisit;
            }
            else {
                LocalDateTime endTime = time.plusSeconds(remainTime);
                browse(websiteURL,endTime);
                remainTime = 0;
            }
            timeToVisit = generateRandom(300);
            time = LocalDateTime.now();
            websiteNo = generateRandomSelect(1000);
            websiteURL = formatWebsiteURL(websites.get(websiteNo));;
        }
        fw.flush();
        fw.close();

        System.out.println("Done.");
        driver.close();
    }


    // Reformat the website URL
    public static String formatWebsiteURL(String src) {
        String result = src.split(",")[1];
        result = result.replace('"',' ');
        result = https.concat(result.trim());
        return result;
    }


    // Actual browse function
    public static void browse(String websiteURL, LocalDateTime endTime) throws InterruptedException, IOException {
        // Logging
        System.out.print(LocalDateTime.now() + " Going to browse " + websiteURL + " until " + endTime + "\n");
        fw.write(LocalDateTime.now() + " Going to browse " + websiteURL + " until " + endTime + "\n");

        // Browsing, scroll to the bottom of the website to ensure all elements are loaded
        try {
            driver.get(websiteURL);
            js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
        }
        catch (WebDriverException e){
            e.printStackTrace();
        }

        // Sleep until time up
        int sleepDuration = (int) ChronoUnit.SECONDS.between(LocalDateTime.now(), endTime);
        fw.flush();
        TimeUnit.SECONDS.sleep(sleepDuration);
    }
 }
