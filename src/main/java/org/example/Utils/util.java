package org.example.Utils;

import com.github.javafaker.Faker;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class util {

    /**
     * Generate a random number between 0 and scale
     * @param scale The upper Limit of the random number
     * @return The random number
     */
    public static int generateRandom(int scale) {
        return (int) (Math.random() * scale);
    }


    /**
     * Generate a random number between lowerBound and upperBound
     * @param lowerBound The lower bound of the random number
     * @param upperbound The upper bound of the random number
     * @return The random number
     */
    public static double generateRandom(int lowerBound, int upperbound) {
        return (Math.random() * (upperbound-lowerBound)) + lowerBound;
    }


    /**
     * Generate a random select index for a list
     * @param listSize the size of the list
     * @return the random index
     */
    public static int generateRandomSelect(int listSize) {
        int res =  (int) (Math.random() * listSize);
        if (res == listSize) {
            return generateRandomSelect(listSize);
        }
        return res;
    }


    /**
     * Read a URL file and return a list of the URLs
     * @param filePath the path of the URL file
     * @return an ArrayList of the URLs
     * @throws IOException if the file is not found
     */
    public static ArrayList<String> readURL(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        ArrayList<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }


    /**
     * Generate a random string with a length between lowerBound and upperBound
     * Use Java Faker to generate the string
     * @param lowerBound the lower bound of the length
     * @param upperBound the upper bound of the length
     * @return a random string
     */
    public static String generateFakerString(int lowerBound, int upperBound) {
        int length = (int) generateRandom(lowerBound,upperBound);
        Faker faker = new Faker();
        return faker.lorem().fixedString(length);
    }


    /**
     * Replace a line in a file
     * @param path path of the file
     * @param replacement the replacement string
     */
    public static void replaceLine(String path, String replacement) {
        File file = new File(path);
        StringBuilder buffer = new StringBuilder();

        try {
            Scanner reader = new Scanner(file);
            String line = reader.nextLine();
            if (reader.hasNextLine()) {
                buffer.append(line);
            }
            buffer.append("\n").append(replacement);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            FileWriter fWriter = new FileWriter(path);
            BufferedWriter writer = new BufferedWriter(fWriter);
            writer.write(buffer.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Execute a command in the terminal
     * @param command command to be executed
     */
    static void cmdExec(String command) {
        try {
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String line = "";
            while((line = reader.readLine()) != null) {
                System.out.print(line + "\n");
            }
            proc.waitFor();
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * For remote connection use, wait for the remote to disconnect
     * Avoid the situation that the remote is still connected when the capture is started
     * This will cause the capture to be corrupted
     */
    public static void waitRemoteDisconnect() {
        try {
            TimeUnit.SECONDS.sleep(5);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Read the Slack API token from the token.txt file
     * @return the token
     */
    public static String readToken() {
        String token = null;
        try {
            token =  readURL("token.txt").get(0);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }
}
