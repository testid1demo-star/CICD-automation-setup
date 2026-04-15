package com.sparklingcrown.testall;

import com.sparklingcrown.framework.pageobjects.LoginPage;
import com.sparklingcrown.framework.pageobjects.ProductDetailPage;
import com.sparklingcrown.framework.pageobjects.ProductListingPage;
import com.sparklingcrown.tests.base.BaseTest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class ProductFlowTest extends BaseTest {

	@Test(priority = 1)
	public void TC01_verifyWarningWhenSizeNotSelected() throws InterruptedException {
		LoginPage loginPage = new LoginPage(driver);
		ProductListingPage productListingPage = new ProductListingPage(driver);
		ProductDetailPage productDetailPage = new ProductDetailPage(driver);

		loginPage.loginAsCustomer("testid1demo@gmail.com", "Test@1234");
		loginPage.clickShopAll();

		String product = productListingPage.getThirdProductName(); // Use 3rd product
		productListingPage.clickOnViewButtonForProduct(product);
		
		productDetailPage.clickAddToCart();
		Assert.assertTrue(productDetailPage.isWarningShownForSize(),
				"Warning message not shown when size not selected");
	}

	@Test(priority = 2)
	public void TC02_verifyRecentlyViewedSectionVisible() throws InterruptedException {
		LoginPage loginPage = new LoginPage(driver);
		ProductListingPage productListingPage = new ProductListingPage(driver);
		ProductDetailPage productDetailPage = new ProductDetailPage(driver);

		loginPage.loginAsCustomer("testid1demo@gmail.com", "Test@1234");
		loginPage.clickShopAll();

		String productName = productListingPage.getThirdProductName(); // Use 3rd product
		productListingPage.clickOnViewButtonForProduct(productName);

		By recentlyViewedTitle = By.xpath("//h2[normalize-space()='Recently Viewed Products']");
		By recentlyViewedBlock = By.cssSelector("div[class*='similar-products-block']");

		// Scroll to Recently Viewed section
		WebElement section = driver.findElement(recentlyViewedTitle);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", section);

		productDetailPage.waitForElementToAppear(recentlyViewedTitle);
		Assert.assertTrue(driver.findElement(recentlyViewedBlock).isDisplayed(),
				"'Recently Viewed Products' block not visible");
	}


	@Test(priority = 3)
	public void TC03_verifyYouMayAlsoLikeSectionVisible() throws InterruptedException {
		LoginPage loginPage = new LoginPage(driver);
		ProductListingPage productListingPage = new ProductListingPage(driver);
		ProductDetailPage productDetailPage = new ProductDetailPage(driver);

		loginPage.loginAsCustomer("testid1demo@gmail.com", "Test@1234");
		loginPage.clickShopAll();

		String productName = productListingPage.getThirdProductName(); // Use 3rd product
		productListingPage.clickOnViewButtonForProduct(productName);

		By mayAlsoLikeTitle = By.xpath("//h2[normalize-space()='You may also Like']");
		By mayAlsoLikeBlock = By.cssSelector("div[class*='may-also-like-block']");

		// Scroll to "You may also like" section
		WebElement section = driver.findElement(mayAlsoLikeTitle);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", section);

		productDetailPage.waitForElementToAppear(mayAlsoLikeTitle);
		Assert.assertTrue(driver.findElement(mayAlsoLikeBlock).isDisplayed(),
				"'You may also Like' block not visible");
	}


	@Test(priority = 4, groups = "finalFlow")
	public void TC04_addMultipleProductsToCart() throws InterruptedException {

		LoginPage loginPage = new LoginPage(driver);
		ProductListingPage productListingPage = new ProductListingPage(driver);
		ProductDetailPage productDetailPage = new ProductDetailPage(driver);

		// Step 3: Login as customer with valid credentials
		loginPage.loginAsCustomer("testid1demo@gmail.com", "Test@1234");

		// Step 4: Click on Shop All link
		loginPage.clickShopAll();

		List<String> productNames = Arrays.asList("earrings", "tops"); // Input list

		for (int i = 0; i < productNames.size(); i++) {
			String product = productNames.get(i);

			boolean found = false;
			int retryCount = 3;

			while (!found && retryCount > 0) {
				if (productListingPage.isProductPresent(product)) {
					productListingPage.clickOnViewButtonForProduct(product);
					found = true;
				} else {
					productListingPage.loadMoreProducts();
					retryCount--;
					Thread.sleep(2000);
				}
			}

			if (!found) {
				System.out.println("Product not found: " + product);
				continue;
			}

			// Step 5: Now on product detail page, match title
			String actualTitle = productDetailPage.getProductTitle();
			//Assert.assertTrue(actualTitle.toLowerCase().contains(product.toLowerCase()), "Product title mismatch");

			// Instead of: assertTrue(actualTitle.equals(expectedTitle));
            String actualTitle = cartPage.getProductName(i).trim();
            String expectedTitle = "Your Expected Name";

            System.out.println("DEBUG: Expected: [" + expectedTitle + "] but found: [" + actualTitle + "]");

            Assert.assertTrue(actualTitle.equalsIgnoreCase(expectedTitle), 
            "Product title mismatch! Expected: " + expectedTitle + " but found: " + actualTitle);

			// Select size and quantity
			productDetailPage.selectSize();
			productDetailPage.increaseQuantity();

			// Click Add to Cart again (after size selected)
			productDetailPage.clickAddToCart();

			// 🆕 Handle popup if appears
			productDetailPage.confirmIfPopupShown();

			// Get Success toast
			String toast = productDetailPage.getToastMessage();
			Assert.assertTrue(toast.contains("Success"), "Add to Cart failed");

			if (productNames.size() == 1 || i == productNames.size() - 1) {
				waitForToastToDisappear();
				loginPage.goToCart();
			} else {
				driver.navigate().back();
				Thread.sleep(2000);
			}
		}
	}

	private void waitForToastToDisappear() throws InterruptedException {
		Thread.sleep(2000); // Replace with explicit wait in future if needed
	}
}
