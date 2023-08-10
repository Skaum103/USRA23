package org.example.Bots_leg;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class YoutubeBot {

    // https://github.com/pystardust/ytfzf

    /*
    @Test
    public void eightComponents() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");

        String title = driver.getTitle();
        assertEquals("Web form", title);

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        WebElement textBox = driver.findElement(By.name("my-text"));
        WebElement submitButton = driver.findElement(By.cssSelector("button"));

        textBox.sendKeys("Selenium");
        submitButton.click();

        WebElement message = driver.findElement(By.id("message"));
        String value = message.getText();
        assertEquals("Received!", value);

        driver.quit();
    }

     */

    public static void startBot() throws InterruptedException, IOException {

        /*

        // Initialize a Chrome Driver
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.youtube.com/watch?v=dQw4w9WgXcQ");

        // Find the play button and click
        WebElement element =
                driver.findElement(By.xpath("//button[@class='ytp-large-play-button ytp-button']"));
        element.click();

        // Play the video
        TimeUnit.SECONDS.sleep(45);

        // Quit Driver
        driver.quit();

         */

        vlc();

    }

    public static void vlc() throws IOException, InterruptedException {

        String command = "open -a vlc https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        Process proc = Runtime.getRuntime().exec(command);

        /*

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
        }

         */

        TimeUnit.SECONDS.sleep(30);
        proc.destroy();
    }

}