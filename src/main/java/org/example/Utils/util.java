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

    // Generate a random number represents the minutes to visit a website
    // Between 0-600 seconds
    public static int generateRandom(int scale) {
        return (int) (Math.random() * scale);
    }

    public static double generateRandom(int lowerBound, int upperbound) {
        return (Math.random() * (upperbound-lowerBound)) + lowerBound;
    }


    // Generate a random number represents the index of website to visit
    // Between 1-20
    public static int generateRandomSelect(int listSize) {
        int res =  (int) (Math.random() * listSize);
        if (res == listSize) {
            return generateRandomSelect(listSize);
        }
        return res;
    }


    // Use the random index to find the website to visit
    public static ArrayList<String> readURL(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        ArrayList<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }

    public static String generateRandomString(int lowerBound, int upperBound) {
        int length = (int) generateRandom(lowerBound,upperBound);

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz"
                +"!@#$%^&*()-+=/,.<>_ ";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public static String generateFakerString(int lowerBound, int upperBound) {
        int length = (int) generateRandom(lowerBound,upperBound);
        Faker faker = new Faker();
        return faker.lorem().fixedString(length);
    }

    public ArrayList<String> generateStrings(int count, int lowerBound, int upperBound) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            strings.add(generateRandomString(lowerBound,upperBound));
        }
        return strings;
    }


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


    public static void waitRemoteDisconnect() {
        try {
            TimeUnit.SECONDS.sleep(5);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
