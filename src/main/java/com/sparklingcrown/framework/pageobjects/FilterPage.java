package com.sparklingcrown.framework.pageobjects;

import com.sparklingcrown.framework.abstractcomponents.AbstractComponent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class FilterPage extends AbstractComponent {

	WebDriver driver;

	public FilterPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// --- Common Filters ---
	@FindBy(xpath = "//a[@href='/collections/shop']")
	WebElement shopAllLink;

	@FindBy(xpath = "//a[@class='clrfilter']")
	WebElement clearFilterBtn;

	@FindBy(xpath = "//div[@class='select-wrapper']//select")
	WebElement sortDropdown;

	// --- Product Listing Details ---
	@FindBy(xpath = "//b/sapn[@class='ng-binding']")
	List<WebElement> productPrices;

	@FindBy(xpath = "//span[@class='text-danger ng-binding']")
	List<WebElement> productCategories;

	// --- Category (Product Type) Filter ---
	@FindBy(xpath = "//div[@class='sidebar-widget categories-widget']")
	WebElement productTypeBlock;

	@FindBy(xpath = "//div[@class='sidebar-widget categories-widget']//ul/li//span")
	List<WebElement> allCategoryLabels;

	@FindBy(xpath = "//div[@class='sidebar-widget categories-widget']//i[@class='fa fa-angle-down']")
	WebElement categoryDropIconDown;

	@FindBy(xpath = "//div[@class='sidebar-widget categories-widget']//i[@class='fa fa-angle-up']")
	WebElement categoryDropIconUp;

	// --- Price Filter ---
	@FindBy(xpath = "(//div[@class='sidebar-widget brand-widget'])[4]")
	WebElement priceBlock;

	@FindBy(xpath = "//span[contains(text(),'₹10,001 - ₹20,000')]")
	WebElement priceRange10001to20000;

	@FindBy(xpath = "//span[contains(text(),'₹20,001 - ₹50,000')]")
	WebElement priceRange20001to50000;

	@FindBy(xpath = "(//div[@class='sidebar-widget brand-widget'])[4]//i[@class='fa fa-angle-down']")
	WebElement priceDropIconDown;

	@FindBy(xpath = "(//div[@class='sidebar-widget brand-widget'])[4]//i[@class='fa fa-angle-up']")
	WebElement priceDropIconUp;

	// ======================
	// Actions and Methods
	// ======================

	public void goToListingPage() {
		scrollIntoView(shopAllLink);
		waitForElementToBeVisible(shopAllLink);
		shopAllLink.click();
		waitForProductSectionUpdate();
	}

	public void selectCategory(String categoryName) {
		for (WebElement category : allCategoryLabels) {
			if (category.getText().equalsIgnoreCase(categoryName)) {
				scrollIntoView(category);
				waitForElementToBeVisible(category);
				category.click();
				waitForDomUpdate();
				break;
			}
		}
	}

	public void clearFilter() {
		scrollIntoView(clearFilterBtn);
		waitForElementToBeVisible(clearFilterBtn);
		clearFilterBtn.click();
		waitForDomUpdate();
	}

	public void closeCategoryBlockIfOpen() {
		try {
			if (categoryDropIconUp.isDisplayed()) {
				scrollIntoView(categoryDropIconUp);
				categoryDropIconUp.click();
			}
		} catch (Exception ignored) {
		}
	}

	public void openPriceBlockIfClosed() {
		try {
			if (priceDropIconDown.isDisplayed()) {
				scrollIntoView(priceDropIconDown);
				priceDropIconDown.click();
			}
		} catch (Exception ignored) {
		}
	}

	public void selectPriceRange(String rangeLabel) {
		if (rangeLabel.equals("₹10,001 - ₹20,000")) {
			scrollIntoView(priceRange10001to20000);
			waitForElementToBeVisible(priceRange10001to20000);
			priceRange10001to20000.click();
			waitForDomUpdate();
		}
	}

	/*
	 * public void selectPriceRange(String rangeLabel) { if
	 * (rangeLabel.equals("₹20,001 - ₹50,000")) {
	 * scrollIntoView(priceRange20001to50000);
	 * waitForElementToBeVisible(priceRange20001to50000);
	 * priceRange20001to50000.click(); waitForDomUpdate(); } }
	 */
	public void selectSortOption(String visibleText) {
		scrollIntoView(sortDropdown);
		waitForElementToBeVisible(sortDropdown);
		// driver.findElement(By.xpath("//div[@class='select-wrapper']//select")).click();
		new Select(sortDropdown).selectByVisibleText(visibleText);
		waitForDomUpdate();
	}

	public List<Double> getAllVisibleProductPrices() {
		return productPrices.stream().map(e -> e.getText().replaceAll("[^0-9.]", "")).filter(s -> !s.isEmpty())
				.map(Double::parseDouble).collect(Collectors.toList());
	}

	public List<String> getAllVisibleProductCategories() {
		return productCategories.stream().map(WebElement::getText).collect(Collectors.toList());
	}

	// ======================
	// Utilities
	// ======================

	private void scrollIntoView(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
	}

	private void waitForDomUpdate() {
		try {
			Thread.sleep(2000); // Adjust if needed for AJAX/DOM delay
		} catch (InterruptedException ignored) {
		}
	}

	public void waitForProductSectionUpdate() {
		By loader = By.xpath("//div[@ng-if='isLoading']");
		By productList = By.xpath("//div[@ng-repeat='product in products']");
		By noProductMsg = By.xpath("//div[@class='col-lg-12 col-md-12 text-center']");

		// filterPage.waitForElementToDisappear(loader, 15); // loading gone

		try {
			waitForElementToAppear(productList, 10); // products loaded
		} catch (Exception e) {
			waitForElementToAppear(noProductMsg, 10); // or no products
		}
	}

}