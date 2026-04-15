// Updated WishlistTests.java (scrolling to wishlist icon itself)

package com.sparklingcrown.testall;

import com.sparklingcrown.framework.pageobjects.LoginPage;
import com.sparklingcrown.framework.pageobjects.ProductListingPage;
import com.sparklingcrown.tests.base.BaseTest;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class WishlistTests extends BaseTest {

	String inputItem = "chain";
public static String newTestItem;

	@Test(priority = 1)
	public void TC01_addItemToWishlist() {
		LoginPage loginPage = new LoginPage(driver);
		ProductListingPage listingPage = new ProductListingPage(driver);

		loginPage.loginAsCustomer("testid1demo@gmail.com", "Test@1234");
		loginPage.clickShopAll();

		if (listingPage.isProductPresent(inputItem)) {
			if (!listingPage.isWishlistIconChecked(inputItem)) {
				newTestItem = inputItem;
			} else {
				newTestItem = listingPage.findFirstNonWishlistedProduct();
			}
		} else {
			newTestItem = listingPage.findFirstNonWishlistedProduct();
		}

		scrollToWishlistIcon(newTestItem);
		listingPage.clickWishlistIconForProduct(newTestItem);

		String toastMsg = listingPage.getWishlistToastMessage();
		Assert.assertTrue(toastMsg.contains("Success"), "Item not added to wishlist.");
	}

	@Test(priority = 2, dependsOnMethods = "TC01_addItemToWishlist")
	public void TC02_verifyItemInWishlistPage() {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginAsCustomer("testid1demo@gmail.com", "Test@1234");

		loginPage.goToWishlist();
		waitForSeconds(3);

		String xpath = "//table[@class='table']//a[contains(text(), '" + newTestItem + "')]";
		boolean isPresent = !driver.findElements(By.xpath(xpath)).isEmpty();
		Assert.assertTrue(isPresent, "Item not found in wishlist table");
	}

	@Test(priority = 3, dependsOnMethods = "TC01_addItemToWishlist")
	public void TC03_addDuplicateWishlist_shouldShowWarning() {
		LoginPage loginPage = new LoginPage(driver);
		ProductListingPage listingPage = new ProductListingPage(driver);

		loginPage.loginAsCustomer("testid1demo@gmail.com", "Test@1234");
		loginPage.clickShopAll();

		

		scrollToWishlistIcon(newTestItem);
	//	waitForSeconds(2);
		listingPage.clickWishlistIconForProduct(newTestItem);

		String warning = listingPage.getWarningToastMessage();
		Assert.assertTrue(warning.contains("Already in Wishlist") || warning.contains("Warning!"),
				"Duplicate wishlist warning not shown");
	}

	@Test(priority = 4, dependsOnMethods = "TC01_addItemToWishlist")
	public void TC04_DeleteWishlist_shouldRemoveItem() {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginAsCustomer("testid1demo@gmail.com", "Test@1234");

		loginPage.goToWishlist();
		waitForSeconds(2);

		String productXpath = "//table[@class='table']//a[contains(text(), '" + newTestItem + "')]";
		boolean found = !driver.findElements(By.xpath(productXpath)).isEmpty();
		Assert.assertTrue(found, "Wishlist item not present before delete");

		WebElement removeIcon = driver.findElement(By.xpath("//a[@class='remove_product']"));
		removeIcon.click();

		waitForAlertAndAccept();
//		waitForSeconds(0.5);

		ProductListingPage listingPage = new ProductListingPage(driver);

		String toastMsg = listingPage.getWishlistToastMessage();

		Assert.assertTrue(toastMsg.contains("Success"), "Item not removed from wishlist.");

		
	//	 boolean stillPresent = !driver.findElements(By.xpath(productXpath)).isEmpty();
	//	 Assert.assertFalse(stillPresent, "Wishlist item was not deleted.");

	}

	@Test(priority = 5)
	public void TC05_EmptyWishlist_shouldShowEmpty() {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToWishlist();
		waitForSeconds(2);

		String msgXpath = "//td[contains(normalize-space(),'No Record Found...!')]";
		boolean isEmpty = !driver.findElements(By.xpath(msgXpath)).isEmpty();
		Assert.assertTrue(isEmpty, "Wishlist is not empty after deletion.");
	}

	public void waitForSeconds(double d) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds((long) d));
		wait.pollingEvery(Duration.ofMillis(500));
		wait.withMessage("Waiting " + d + " seconds...");
	}

	public void waitForAlertAndAccept() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	public void scrollToWishlistIcon(String productName) {
		List<WebElement> blocks = driver
				.findElements(By.xpath("//div[@id='content-products']//div[contains(@class, 'product-block')]"));
		for (WebElement block : blocks) {
			try {
				WebElement nameElement = block.findElement(By.xpath(".//p/a"));
				if (nameElement.getText().trim().equalsIgnoreCase(productName.trim())) {
					WebElement icon = block.findElement(By.cssSelector("i[id*='wish']"));
					((JavascriptExecutor) driver)
							.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", icon);
					// waitForSeconds(1);
					break;
				}
			} catch (Exception e) {
				// skip
			}
		}
	}
}
