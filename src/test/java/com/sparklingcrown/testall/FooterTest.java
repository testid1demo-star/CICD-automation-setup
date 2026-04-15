package com.sparklingcrown.testall;

import com.sparklingcrown.framework.pageobjects.FooterPage;
import com.sparklingcrown.tests.base.BaseTest;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

public class FooterTest extends BaseTest {

    FooterPage footerPage;

    /**
     * Test Case 1: Verify all static elements of the footer are displayed.
     */
    @Test(priority = 1, groups = "finalFlow")
    public void verifyFooterStaticContent() throws InterruptedException {
        footerPage = new FooterPage(driver);
        footerPage.scrollToFooter();
        Thread.sleep(1000); // allow footer to load

        // Common section
        Assert.assertTrue(footerPage.footerSection.isDisplayed(), "Footer not displayed");
        Assert.assertTrue(footerPage.copyrightText.isDisplayed(), "Copyright text missing");

        // Row 1: App + Social
        Assert.assertTrue(footerPage.appHeading.isDisplayed(), "App heading missing");
        Assert.assertTrue(footerPage.appImage.isDisplayed(), "App image missing");

        Assert.assertTrue(footerPage.followHeading.isDisplayed(), "Follow heading missing");

        // Row 2: Quick Link
        Assert.assertTrue(footerPage.quickHeading.isDisplayed(), "Quick heading missing");

        // Row 3: Info
        Assert.assertTrue(footerPage.infoHeading.isDisplayed(), "Info heading missing");

        // Row 4: Contact Info
        Assert.assertTrue(footerPage.contactHeading.isDisplayed(), "Contact heading missing");
        Assert.assertTrue(footerPage.addressText.isDisplayed(), "Address text missing");
        Assert.assertTrue(footerPage.addressIcon.isDisplayed(), "Address icon missing");
        Assert.assertTrue(footerPage.phoneIcon.isDisplayed(), "Phone icon missing");
        Assert.assertTrue(footerPage.emailIcon.isDisplayed(), "Email icon missing");
    }

    /**
     * Test Case 2: Verify footer links open in new tab and are not broken.
     */
    @Test(priority = 2)
    public void verifyFooterLinksOpenAndValid() throws InterruptedException {
        footerPage = new FooterPage(driver);
        footerPage.scrollToFooter();
        Thread.sleep(1000); // allow footer to load

        verifyLinksOpenInNewTabAndNotBroken(footerPage.socialLinks, "Social Links");
        verifyLinksOpenInNewTabAndNotBroken(footerPage.quickLinks, "Quick Links");
        verifyLinksOpenInNewTabAndNotBroken(footerPage.infoLinks, "Info Links");
    }

    /**
     * Helper method to verify that each link opens in a new tab and is not broken.
     */
    public void verifyLinksOpenInNewTabAndNotBroken(List<WebElement> links, String label) throws InterruptedException {
        String mainTab = driver.getWindowHandle();

        for (WebElement link : links) {
            String linkText = link.getText().trim();
            String href = link.getAttribute("href");

            if (href == null || href.isEmpty()) {
                System.out.println(label + " - [" + linkText + "] is missing href attribute.");
                continue;
            }

            // Open link in new tab using JavaScript
            ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", href);
            Thread.sleep(1500); // wait for new tab to open

            Set<String> allTabs = driver.getWindowHandles();
            for (String tab : allTabs) {
                if (!tab.equals(mainTab)) {
                    driver.switchTo().window(tab);
                    Thread.sleep(2000); // wait for page to load
                    String currentURL = driver.getCurrentUrl();
                    System.out.println(label + " - [" + linkText + "]: " + currentURL);

                    // Basic validation: check if URL starts with "http"
                    Assert.assertTrue(currentURL.startsWith("http"), "Invalid or broken link: " + currentURL);

                    driver.close(); // close the new tab
                    driver.switchTo().window(mainTab); // switch back to main tab
                    break;
                }
            }
        }
    }
}
