package com.sparklingcrown.framework.pageobjects;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sparklingcrown.framework.abstractcomponents.AbstractComponent;

public class ProductDetailPage extends AbstractComponent {

	By productTitle = By.cssSelector("h3[class='mb-4 ng-binding']");
	By sizeDropdown = By.id("ddlitemsize");
	By sizeOption = By.xpath("//select[@id='ddlitemsize']/option[@value='15']"); // 2CM
	By incrementButton = By.cssSelector("button.increment");
	By addToCartBtn = By.cssSelector("a.add_to_cart_btn");
	By confirmBtn = By.cssSelector("button[class*='confirm']");
	By cancelBtn = By.cssSelector("button[class*='cancel']");
	By toastMessage = By.xpath("//strong[normalize-space()='Success!']");
	By warningMsg = By.xpath("//span[normalize-space()='Please Select Size']");

	public ProductDetailPage(WebDriver driver) {
		super(driver);
	}

	public String getProductTitle() {
		return driver.findElement(productTitle).getText();
	}

	public void selectSize() {
		driver.findElement(sizeDropdown).click();
		driver.findElement(sizeOption).click();
	}

	public void increaseQuantity() {
		driver.findElement(incrementButton).click();
	}

	/*
	 * public void clickAddToCart() { driver.findElement(addToCartBtn).click(); }
	 */
	/*
	 * public void clickAddToCart() { WebDriverWait wait = new WebDriverWait(driver,
	 * Duration.ofSeconds(10));
	 * 
	 * // Locate the Add to Cart button WebElement addToCartBtn =
	 * driver.findElement(By.cssSelector("a.add_to_cart_btn"));
	 * 
	 * // Scroll into view ((JavascriptExecutor)
	 * driver).executeScript("arguments[0].scrollIntoView(true);", addToCartBtn);
	 * 
	 * // Wait for any toast message to disappear (optional: based on your flow) try
	 * { wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(
	 * "//strong[normalize-space()='Success!']"))); } catch (Exception e) { //
	 * Ignore if no toast is shown yet }
	 * 
	 * // Wait until the button is clickable
	 * wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));
	 * 
	 * // Finally click the button addToCartBtn.click(); }
	 */
	public void clickAddToCart() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement addBtn = driver.findElement(addToCartBtn);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addBtn);
		wait.until(ExpectedConditions.elementToBeClickable(addBtn));
		addBtn.click();
	}

	public void confirmIfPopupShown() {
		try {
			waitForElementToAppear(confirmBtn, 5);
			driver.findElement(confirmBtn).click();
		} catch (Exception e) {
			System.out.println("No confirmation popup appeared.");
		}
	}

	public void confirmAddToCartPopup() {
		waitForElementToAppear(confirmBtn, 10);
		driver.findElement(confirmBtn).click();
	}

	public void cancelAddToCartPopup() {
		waitForElementToAppear(cancelBtn, 10);
		driver.findElement(cancelBtn).click();
	}

	public String getToastMessage() {
		waitForElementToAppear(toastMessage);
		return driver.findElement(toastMessage).getText().trim();
	}

	public boolean isWarningShownForSize() {
		try {
			return driver.findElement(warningMsg).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

}
