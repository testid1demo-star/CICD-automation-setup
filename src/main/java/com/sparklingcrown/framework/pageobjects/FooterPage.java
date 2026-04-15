package com.sparklingcrown.framework.pageobjects;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

public class FooterPage {

    WebDriver driver;

    public FooterPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ======= Common Section ========
    @FindBy(xpath = "//section/div/address[contains(text(),'Copyright')]")
    public WebElement copyrightText;

    @FindBy(css = "div[class*='footer wow fadeInUp']")
    public WebElement footerSection;

    // ======= Row 1: App + Social =======
    @FindBy(xpath = "//h3[normalize-space()='Download the App Now']")
    public WebElement appHeading;

    @FindBy(xpath = "//div/img[@src='/assets/images/app-store-google-play-logo.png']")
    public WebElement appImage;

    @FindBy(xpath = "//h3[normalize-space()='Follow Us On']")
    public WebElement followHeading;

    @FindBy(xpath = "(//div[@class='quick-link'])[1]//ul/li/a")
    public List<WebElement> socialLinks;

    // ======= Row 2: Quick Link =======
    @FindBy(xpath = "//h3[normalize-space()='Quick Link']")
    public WebElement quickHeading;

    @FindBy(xpath = "(//div[@class='quick-link'])[2]//ul/li/a")
    public List<WebElement> quickLinks;

    // ======= Row 3: Info =======
    @FindBy(xpath = "//h3[normalize-space()='Info']")
    public WebElement infoHeading;

    @FindBy(xpath = "(//div[@class='quick-link'])[3]//ul/li/a")
    public List<WebElement> infoLinks;

    // ======= Row 4: Contact Us =======
    @FindBy(xpath = "//h3[normalize-space()='Contact Us']")
    public WebElement contactHeading;

    @FindBy(xpath = "//span[@class='spancmpnyaddress']")
    public WebElement addressText;

    @FindBy(xpath = "//i[@class='fa-solid fa-location-dot']")
    public WebElement addressIcon;

    @FindBy(xpath = "//i[@class='fa-solid fa-phone']")
    public WebElement phoneIcon;

    @FindBy(xpath = "//i[@class='fa-regular fa-envelope']")
    public WebElement emailIcon;

    // ====== Scroll to footer ======
    public void scrollToFooter() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void waitForTabAndGetUrl(String expectedText, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Set<String> handlesBefore = driver.getWindowHandles();

        // Wait for new tab to open
        wait.until(driver1 -> driver1.getWindowHandles().size() > handlesBefore.size());

        Set<String> handlesAfter = driver.getWindowHandles();
        handlesAfter.removeAll(handlesBefore);
        String newTabHandle = handlesAfter.iterator().next();

        driver.switchTo().window(newTabHandle);

        try {
            Thread.sleep(2000); // allow URL to load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void closeCurrentTabAndSwitchBack(WebDriver driver, String homeTabHandle) {
        driver.close(); // closes current (new) tab
        driver.switchTo().window(homeTabHandle); // go back to main tab
    }

}
