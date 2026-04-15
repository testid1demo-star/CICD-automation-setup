package com.sparklingcrown.framework.pageobjects;

import org.openqa.selenium.*;
import com.sparklingcrown.framework.abstractcomponents.AbstractComponent;
import java.util.List;

public class SearchPage extends AbstractComponent {
    WebDriver driver;

    public SearchPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    private By searchField = By.xpath("//input[@id='txtglobalsrch']");
    private By searchIcon = By.cssSelector("i[class*='fa-search']");
    private By autocompleteResults = By.xpath("//div[@id='autocomplete-results']");
    private By autocompleteLinks = By.xpath("//div[@id='autocomplete-results']/a");
    private By shopBreadcrumb = By.xpath("//li[@class='breadcrumb-item']/span");
    private By noResultHeading = By.xpath("//h4[@class='text-center pt-20']");
    private By homepageLogo = By.xpath("//div[@class='header-logo']");
    private By productDetailContainer = By.cssSelector("div.product-detail"); // assume this exists
    private By productListGrid = By.cssSelector("div.product-listing");       // for listing page

    public void goToHomePage() {
        driver.findElement(homepageLogo).click();
    }

    public void clickSearchField() {
        driver.findElement(searchField).click();
    }

    public void enterSearchText(String keyword) {
        driver.findElement(searchField).clear();
        driver.findElement(searchField).sendKeys(keyword);
    }

    public void pressEnter() {
        driver.findElement(searchField).sendKeys(Keys.ENTER);
    }

    public void clickSearchIcon() {
        driver.findElement(searchIcon).click();
    }

    public void selectFirstDropdownItem() {
        waitForElementToAppear(autocompleteResults); // Wait for dropdown
        List<WebElement> items = driver.findElements(autocompleteLinks);

        String originalWindow = driver.getWindowHandle();

        if (!items.isEmpty()) {
            items.get(0).click();

            // Wait for new tab to open
            waitForNumberOfWindowsToBe(2);

            // Switch to the new tab
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }

            // Wait for product detail page to load (adjust locator if needed)
            waitForURLToContain("/product/");
        } else {
            throw new RuntimeException("No items in autocomplete list.");
        }
    }


    public String getBreadcrumbText() {
        return driver.findElement(shopBreadcrumb).getText();
    }

    public String getNoResultHeading() {
        return driver.findElement(noResultHeading).getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ✅ New utility to wait for result pages to load
    public void waitForSearchResultsToLoad() {
        // Either breadcrumb, product grid, or error message will appear
        try {
            waitForElementToAppear(shopBreadcrumb, 10); // for listing page
        } catch (Exception e) {
            try {
                waitForElementToAppear(productDetailContainer, 5); // product page
            } catch (Exception ex) {
                waitForElementToAppear(noResultHeading, 5); // error msg
            }
        }
    }
}
