package com.goweezeflo;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ResourceBundle;
import java.util.function.Function;

public class Main {

    private static final ResourceBundle SETTINGS = ResourceBundle.getBundle("settings");
    private static final String YOUTUBE_PLAYLIST_URL = SETTINGS.getString("youtube_playlist_url");
    private static final String TOTAL_VIDEOS_XPATH = SETTINGS.getString("total_videos_xpath");
    private static final String VIDEO_TITLE_XPATH = SETTINGS.getString("video_title_xpath");
    private static String currentVideoTitle = "";
    private static String nextVideoTitle = "";
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        WebDriver driver = getDriver();
        navigateToUrl(driver, YOUTUBE_PLAYLIST_URL);
        printAllVideoTitles(driver);
        try {
            driver.quit();
        } catch (Exception e) {
            logger.error("WebDriver did not shutdown gracefully.", e);
        }
    }

    private static void printAllVideoTitles(WebDriver driver) {
        for (int i = 1; i <= getTotalNumberOfVideos(driver); i++) {
            printVideoTitle(driver);
            playNextVideo(driver);
        }
    }

    private static void navigateToUrl(WebDriver driver, String playlistUrl) {
        driver.navigate().to(playlistUrl);
        driver.manage().window().maximize();

    }

    private static int getTotalNumberOfVideos(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until((Function<WebDriver, Object>) webDriver -> driver.findElement(By.xpath(TOTAL_VIDEOS_XPATH)));
        WebElement totalVideos = driver.findElement(By.xpath(TOTAL_VIDEOS_XPATH));
        return Integer.parseInt(totalVideos.getText());
    }

    private static String getVideoTitle(WebDriver driver) {
        // Wait for the video title to change
        while (currentVideoTitle.equals(nextVideoTitle)) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until((Function<WebDriver, Object>) webDriver -> driver.findElement(By.xpath(VIDEO_TITLE_XPATH)));
            WebElement titleElement = driver.findElement(By.xpath(VIDEO_TITLE_XPATH));
            nextVideoTitle = titleElement.getText();
        }
        currentVideoTitle = nextVideoTitle;
        return currentVideoTitle;
    }

    private static void printVideoTitle(WebDriver driver) {
        String videoTitle = String.format("[YouTube: %s](%s)%n", getVideoTitle(driver), getVideoUrl(driver));
        logger.info(videoTitle);
    }

    private static String getVideoUrl(WebDriver driver) {
        String url = driver.getCurrentUrl();
        return url.substring(0, url.indexOf("&"));
    }

    private static void playNextVideo(WebDriver driver) {
        new Actions(driver)
            .keyDown(Keys.SHIFT)
            .keyDown("N")
            .perform(); // Skip to the next YouTube video using the [Shift+N] keyboard shortcut
    }

    private static WebDriver getDriver() {
        EdgeOptions options = new EdgeOptions()
            .addArguments("--guest") // Disable the “Personalize your web experience” prompt in Microsoft Edge
            .addArguments("--remote-allow-origins=*"); // Avoid the "WARNING: Connection reset" error
        return new EdgeDriver(options);
    }
}
