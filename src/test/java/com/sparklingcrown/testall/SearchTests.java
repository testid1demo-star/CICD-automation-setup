package com.sparklingcrown.testall;

import com.sparklingcrown.framework.pageobjects.SearchPage;
import com.sparklingcrown.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchTests extends BaseTest {

	SearchPage search;

	@Test(priority = 1)
	public void TC01_blankOrSpaceSearchShouldRedirectToShopPage() {
		search = new SearchPage(driver);
		search.goToHomePage();
		search.clickSearchField();
		search.enterSearchText(" "); // space
		search.clickSearchIcon();
		search.waitForSearchResultsToLoad(); // ✅ wait here
		Assert.assertEquals(search.getBreadcrumbText().trim(), "Shop", "Should redirect to Shop page");
	}

	@Test(priority = 2)
	public void TC02_validSearchFromDropdownRedirectsToProductDetailPage() {
		search = new SearchPage(driver);
		search.goToHomePage();
		search.clickSearchField();
		search.enterSearchText("chain");
		search.selectFirstDropdownItem(); // Now handles tab switch and wait
		Assert.assertTrue(search.getCurrentUrl().startsWith("https://sparklingcrown.24livehost.com/product/"),
				"Should redirect to product page");
	}

	@Test(priority = 3, groups = "finalFlow")
	public void TC03_validSearchWithIconRedirectsToListingPage() {
		search = new SearchPage(driver);
		search.goToHomePage();
		search.clickSearchField();
		search.enterSearchText("ring");
		search.clickSearchIcon();
		search.waitForSearchResultsToLoad(); // ✅ wait here
		Assert.assertTrue(search.getCurrentUrl().contains("searchkeyword=ring"),
				"Should redirect to search listing page");
	}

	@Test(priority = 4)
	public void TC04_invalidSearchShowsNoResultMessage() {
		search = new SearchPage(driver);
		search.goToHomePage();
		search.clickSearchField();
		search.enterSearchText("invalidwrongitem");
		search.clickSearchIcon();
		search.waitForSearchResultsToLoad(); // ✅ wait here
		Assert.assertEquals(search.getNoResultHeading().trim(), "We couldn`t find any matches!",
				"Invalid search should show no results message");
	}
}
