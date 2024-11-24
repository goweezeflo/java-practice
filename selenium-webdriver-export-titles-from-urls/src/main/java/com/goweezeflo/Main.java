package com.goweezeflo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.logging.Level;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        List<String> links = new LinkReader("links.txt").getLinks();

        EdgeOptions options = new EdgeOptions();

        options.addArguments("--guest"); // Disable the “Personalize your web experience” prompt in Microsoft Edge
        options.addArguments("--remote-allow-origins=*"); // Avoid the "WARNING: Connection reset" error
        options.addArguments("--headless"); // Essential Configuration for Headless Mode
        options.addArguments("--disable-gpu"); // Disable GPU (Optional for better compatibility in headless mode)
        options.addArguments("--window-size=1920,1080"); // Optional for consistent rendering in headless mode
        options.addArguments("--disable-extensions"); // Disable browser extensions
        options.addArguments("--disable-popup-blocking"); // Disables popup blocking

        // Set logging preferences
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.SEVERE); // Logs only severe messages
        logPrefs.enable(LogType.DRIVER, Level.SEVERE);
        logPrefs.enable(LogType.CLIENT, Level.SEVERE);
        options.setCapability("goog:loggingPrefs", logPrefs);

        WebDriver driver = new EdgeDriver(options);

        for (String link : links) {
            driver.navigate().to(link);
            //driver.manage().window().maximize();
            String title = driver.getTitle();
            String youTube = "";
            if (title.toLowerCase().contains("youtube")) {
                youTube = "YouTube: ";
            }
            String pageTitle = String.format("- [%s%s](%s)", youTube, title, link);
            System.out.println(pageTitle);
//            break;
        }
        try {
            driver.quit();
        } catch (Exception e) {
            logger.error("WebDriver did not shutdown gracefully.", e);
        }
    }
}
