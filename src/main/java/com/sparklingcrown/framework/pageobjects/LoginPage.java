package com.sparklingcrown.framework.pageobjects;

import com.sparklingcrown.framework.abstractcomponents.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class LoginPage extends AbstractComponent {

	public WebDriver driver;
	
 
    	public LoginPage(WebDriver driver) {
    	
    	super(driver); // Access shared utility methods
		this.driver = driver;
        
    }

    public By emailField = By.cssSelector("input[placeholder='Email']");
    public By passwordField = By.cssSelector("input[placeholder='Password']");
    public By loginButton = By.cssSelector("button[type='submit']");
    public By emailError = By.id("Login_Email-error");
    public By passwordError = By.id("Login_Password-error");
    public By generalError = By.id("lblerrormsg");

    public void enterEmail(String email) {
        driver.findElement(emailField).clear();
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        waitForElementToAppear(loginButton);
        driver.findElement(loginButton).click();
    }

    public void login(String email, String password) {
        if (email != null) {
            enterEmail(email);
        }
        if (password != null) {
            enterPassword(password);
        }
        clickLogin();
    }

    public String getEmailError() {
        waitForElementToAppear(emailError);
        return driver.findElement(emailError).getText().trim();
    }

    public String getPasswordError() {
        waitForElementToAppear(passwordError);
        return driver.findElement(passwordError).getText().trim();
    }

    public String getGeneralError() {
        waitForElementToAppear(generalError);
        return driver.findElement(generalError).getText().trim();
    }

    public void goTo() {
        driver.get("https://sparklingcrown.24livehost.com/login");
    }
    
    public void loginAsCustomer(String email, String password) {
        driver.findElement(By.xpath("//span[text()='Customer']")).click();
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    
}
