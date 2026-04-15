package com.sparklingcrown.framework.abstractcomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AbstractComponent {
    public WebDriver driver;
    public WebDriverWait wait;
    
    By loginLink = By.cssSelector("a[href='/login']");
    By cartIcon = By.cssSelector("a[href='/cart']");
    By wishlistLink = By.cssSelector("a[href='/wishlist']");
    By userName = By.cssSelector("p[title='testid1']");
    By cartCount = By.cssSelector("span#divcartcount");
    By shopAllLink = By.xpath("//a[@href=\"/collections/shop\"]");

    public AbstractComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitForElementToDisappear(WebElement element) {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementToAppear(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    public void waitForElementToAppear(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForElementToDisappear(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    
    
    public void waitForNumberOfWindowsToBe(int expectedNumber) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> driver.getWindowHandles().size() == expectedNumber);
    }

    public void waitForURLToContain(String partialURL) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains(partialURL));
    }


    
    public void goToLogin() {
        Actions actions = new Actions(driver);
        WebElement loginIcon = driver.findElement(By.cssSelector("div[class*='ur-user-links']"));
        actions.moveToElement(loginIcon).perform();
        driver.findElement(loginLink).click();
    }

    public void goToCart() {
        driver.findElement(cartIcon).click();
    }

    public void goToWishlist() {
        driver.findElement(wishlistLink).click();
    }

    public String getCartCount() {
        return driver.findElement(cartCount).getText();
    }

    public void clickShopAll() {
        driver.findElement(shopAllLink).click();
    }
    
    public void pressEnterKey(By locator) {
        driver.findElement(locator).sendKeys(Keys.ENTER);
    }

    
}
