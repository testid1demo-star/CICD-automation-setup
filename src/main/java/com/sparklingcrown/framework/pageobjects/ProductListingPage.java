package com.sparklingcrown.framework.pageobjects;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.util.Assert;
import com.sparklingcrown.framework.abstractcomponents.AbstractComponent;

public class ProductListingPage extends AbstractComponent {

	By loadMore = By.xpath("//span[text()='Load More']");
	By productBlocks = By.xpath("//div[@id='content-products']//div[contains(@class, 'product-block')]");
	By productNames = By.xpath("//div[@id='content-products']//p/a");

	By wishlistChecked = By.xpath(".//i[contains(@class,'fa-heart') and contains(@class,'fa-solid')]");
	By wishlistUnchecked = By.xpath(".//i[contains(@class,'fa-heart') and contains(@class,'fa-regular')]");
	By toastSuccess = By.xpath("//strong[normalize-space()='Success!']");
	By toastWarning = By.xpath("//strong[normalize-space()='Warning!']");
	By toastAlreadyInWishlist = By.xpath("//span[normalize-space()='Already in Wishlist']");

	public ProductListingPage(WebDriver driver) {
		super(driver);
	}

	public boolean isProductPresent(String productName) {
		List<WebElement> names = driver.findElements(productNames);
		for (WebElement name : names) {
			if (name.getText().toLowerCase().contains(productName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public void clickOnViewButtonForProduct(String productName) {
		List<WebElement> blocks = driver.findElements(productBlocks);
		for (WebElement block : blocks) {
			if (block.getText().toLowerCase().contains(productName.toLowerCase())) {
				Actions act = new Actions(driver);
				act.moveToElement(block).perform();
				block.findElement(By.xpath(".//a[text()=' View']")).click();
				break;
			}
		}
	}

	/*
	  public void loadMoreProducts() { try { WebElement loadMoreBtn =
	  driver.findElement(loadMore);
	  
	  if (loadMoreBtn.isDisplayed()) { ((JavascriptExecutor)
	  driver).executeScript("arguments[0].scrollIntoView(true);", loadMoreBtn);
	  Thread.sleep(1000); // Allow any overlay/sticky to settle
	  ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
	  loadMoreBtn); } } catch (Exception e) {
	  System.out.println("Load More button not found or not clickable: " +
	  e.getMessage()); } }
*/	
	
	public void loadMoreProducts() {
		try {
			WebElement loadMoreBtn = driver.findElement(loadMore);
			if (loadMoreBtn.isDisplayed()) {
				Actions actions = new Actions(driver);
				actions.moveToElement(loadMoreBtn).perform();
				Thread.sleep(1000);
				loadMoreBtn.click();
			}
		} catch (Exception e) {
			System.out.println("Load More button not found or not clickable: " + e.getMessage());
		}
	}

	public String getFirstProductName() {
		List<WebElement> names = driver.findElements(productNames);
		if (!names.isEmpty()) {
			return names.get(0).getText();
		} else {
			throw new RuntimeException("No products found on listing page");
		}
	}

	public String getThirdProductName() {
		List<WebElement> names = driver.findElements(productNames);
		if (names.size() >= 3) {
			return names.get(2).getText();
		} else {
			throw new RuntimeException("Less than 3 products found on listing page.");
		}
	}

	public boolean isWishlistIconChecked(String productName) {
	    List<WebElement> blocks = driver.findElements(productBlocks);
	    for (int i = 0; i < blocks.size(); i++) {
	        try {
	            // Re-fetch the block fresh to avoid stale element
	            WebElement block = driver.findElements(productBlocks).get(i);
	            WebElement nameElement = block.findElement(By.xpath(".//p/a"));

	            if (nameElement.getText().trim().equalsIgnoreCase(productName.trim())) {
	                return !block.findElements(wishlistChecked).isEmpty();
	            }
	        } catch (StaleElementReferenceException | NoSuchElementException e) {
	            // Handle gracefully and continue
	        }
	    }
	    return false;
	}
	public String findFirstNonWishlistedProduct() {
		List<WebElement> blocks = driver.findElements(productBlocks);

		for (int i = 0; i < blocks.size(); i++) {
			try {
				WebElement block = blocks.get(i);
				WebElement nameElement = block.findElement(By.xpath(".//p/a"));
				String name = nameElement.getText().trim();

				boolean isChecked = !block.findElements(wishlistChecked).isEmpty();
				if (!isChecked && !name.isEmpty()) {
					return name;
				}
			} catch (Exception e) {
				// Continue to next
			}
		}
		throw new RuntimeException("No non-wishlisted product found on listing page.");
	}


	public void clickWishlistIconForProduct(String productName) {
	    List<WebElement> blocks = driver.findElements(productBlocks);
	    for (int i = 0; i < blocks.size(); i++) {
	        try {
	            WebElement block = driver.findElements(productBlocks).get(i);
	            WebElement nameElement = block.findElement(By.xpath(".//p/a"));

	            if (nameElement.getText().trim().equalsIgnoreCase(productName.trim())) {
	                WebElement wishlistIcon = block.findElement(By.cssSelector("i[id*='wish']"));
	                wishlistIcon.click();
	                break;
	            }
	        } catch (StaleElementReferenceException | NoSuchElementException e) {
	            // Skip and continue trying next block
	        }
	    }
	}


	public String getWishlistToastMessage() {
		waitForElementToAppear(toastSuccess);
		return driver.findElement(toastSuccess).getText().trim();
	}

	public String getWarningToastMessage() {
		try {
			waitForElementToAppear(toastAlreadyInWishlist);
			return driver.findElement(toastAlreadyInWishlist).getText().trim();
		} catch (Exception e) {
			return driver.findElement(toastWarning).getText().trim();
		}
	}
}

 

