package com.sparklingcrown.tests.base;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import com.sparklingcrown.framework.pageobjects.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

public class BaseTest {
    public WebDriver driver;
    public LoginPage loginPage;

 
public WebDriver initializeDriver() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        return driver;
    }

/**
  public WebDriver initializeDriver() {
    WebDriverManager.chromedriver().setup();

    // 1. Create ChromeOptions to tell the browser how to behave
    ChromeOptions options = new ChromeOptions();
    
    // 2. Add the Headless argument (This is the most important part)
    options.addArguments("--headless=new"); 
    
    // 3. Set a fixed window size (Jenkins needs this to 'see' the elements)
    options.addArguments("--window-size=1920,1080");
    
    // 4. Extra safety for Windows Jenkins servers
    options.addArguments("--disable-gpu");
    options.addArguments("--no-sandbox");

    // 5. Pass the options into the ChromeDriver
    WebDriver driver = new ChromeDriver(options);
    
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    // driver.manage().window().maximize(); // You can comment this out as we set size above
    
    return driver;
}
*/
    @BeforeMethod(alwaysRun = true)
    public void launchApplication() throws IOException {
        driver = initializeDriver();
        driver.get("https://sparklingcrown.24livehost.com/login");
        loginPage = new LoginPage(driver); // Properly initialized after driver setup
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File destination = new File(System.getProperty("user.dir") + "//reports//" + testCaseName + ".png");
        FileUtils.copyFile(source, destination);
        return destination.getAbsolutePath();
    }
}
