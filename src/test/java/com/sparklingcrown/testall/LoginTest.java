package com.sparklingcrown.testall;


import com.sparklingcrown.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public void TC01_blankEmailAndPassword() {
        loginPage.login(null, null);
        Assert.assertEquals(loginPage.getEmailError(), "Required", "Email validation failed");
        Assert.assertEquals(loginPage.getPasswordError(), "Required", "Password validation failed");
    }

    @Test(priority = 2)
    public void TC02_validEmail_blankPassword() {
        loginPage.login("test@yopmail.com", null);
        Assert.assertEquals(loginPage.getPasswordError(), "Required", "Password validation failed");
    }

    @Test(priority = 3)
    public void TC03_blankEmail_validPassword() {
        loginPage.login(null, "Test@1233");
        Assert.assertEquals(loginPage.getEmailError(), "Required", "Email validation failed");
    }

    @Test(priority = 4)
    public void TC04_invalidEmailFormat_validPassword() {
        loginPage.login("testyopmailcom", "Test@1233");
        Assert.assertEquals(loginPage.getEmailError(), "Invalid email address.", "Invalid email format validation failed");
    }

    @Test(priority = 5)
    public void TC05_validEmail_invalidPassword() {
        loginPage.login("testid1demo@gmail.com", "teSt@1111");
        Assert.assertEquals(loginPage.getGeneralError(), "You have Entered Invalid Details ...!", "Invalid password message failed");
    }

    @Test(priority = 6)
    public void TC06_validEmail_invalidFormatPassword() {
        loginPage.login("testid1demo@yopmail.com", "testtt");
        Assert.assertEquals(
            loginPage.getPasswordError(),
            "Password must be at least 6 characters long, with at least one uppercase letter, one lowercase letter, one special character, one number, and no spaces.",
            "Invalid password format validation failed"
        );
    }

    @Test(priority = 7)
    public void TC07_invalidEmail_invalidPassword() {
        loginPage.login("test1@gmail.com", "tEst@1234");
        Assert.assertEquals(loginPage.getGeneralError(), "You have Entered Invalid Details ...!", "General error validation failed");
    }

    @Test(priority = 8, groups = "finalFlow")
    public void TC08_validCredentials_shouldRedirectToHome() {
    	loginPage.login("testid1demo@gmail.com", "Test@1234"); //correct code
     //   loginPage.login("testid1demo@gmail.com", "Test@1235"); // wrong code - writes only to check fail reports - screenshot
        // Add assertion if you have a landing page check
        Assert.assertEquals(driver.getCurrentUrl(),"https://sparklingcrown.24livehost.com/", "Login failed");
    }
}
