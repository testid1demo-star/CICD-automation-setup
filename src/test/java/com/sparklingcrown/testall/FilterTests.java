package com.sparklingcrown.testall;

import com.sparklingcrown.framework.pageobjects.FilterPage;
import com.sparklingcrown.tests.base.BaseTest;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class FilterTests extends BaseTest {

	FilterPage filterPage;

	@Test(priority = 1)
	public void filterByCategoryTest() {
		filterPage = new FilterPage(driver);
		filterPage.goToListingPage();

		filterPage.selectCategory("RINGS");

		filterPage.waitForProductSectionUpdate();

		List<String> categories = filterPage.getAllVisibleProductCategories();
		for (String category : categories) {
			Assert.assertTrue(category.toUpperCase().contains("RING"),
					"Expected category to contain RING but found: " + category);
		}
	}

	@Test(priority = 2, groups = "finalFlow")
	public void clearFilterTest() {
		filterPage = new FilterPage(driver);
		filterPage.goToListingPage();

		filterPage.selectCategory("LOCKETS");

		filterPage.waitForProductSectionUpdate();

		List<String> filteredCategories = filterPage.getAllVisibleProductCategories();
		Assert.assertTrue(filteredCategories.stream().allMatch(cat -> cat.toUpperCase().contains("LOCKET")),
				"Expected only LOCKETS before clearing filter.");

		filterPage.clearFilter();

		filterPage.waitForProductSectionUpdate();

		List<String> categoriesAfterClear = filterPage.getAllVisibleProductCategories();
		Assert.assertTrue(categoriesAfterClear.size() > filteredCategories.size(),
				"Clear filter failed: still filtered, expected multiple category types.");
	}

	@Test(priority = 3)
	public void filterByPriceRangeTest() {
		filterPage = new FilterPage(driver);
		filterPage.goToListingPage();

		filterPage.closeCategoryBlockIfOpen();

		filterPage.openPriceBlockIfClosed();

		filterPage.selectPriceRange("₹10,001 - ₹20,000");
		// filterPage.selectPriceRange("₹20,001 - ₹50,000");

		filterPage.waitForProductSectionUpdate();

		List<Double> prices = filterPage.getAllVisibleProductPrices();
		for (Double price : prices) {
			Assert.assertTrue(price >= 10001 && price <= 20000, "Product price out of selected range: " + price);
		}
	}

	@Test(priority = 4)
	public void sortByPriceLowToHighTest() {
		filterPage = new FilterPage(driver);
		filterPage.goToListingPage();

		filterPage.selectSortOption("Sort by Price (Low to High)");
		// filterPage.selectSortOption("Sort by Price (High to Low)");

		filterPage.waitForProductSectionUpdate();

		List<Double> prices = filterPage.getAllVisibleProductPrices();
		System.out.println("Prices found: " + prices);
		Assert.assertFalse(prices.isEmpty(), "No prices found on the page!");

		for (int i = 0; i < prices.size() - 1; i++) {
			Double current = prices.get(i);
			Double next = prices.get(i + 1);
			Assert.assertTrue(current <= next, "Products not sorted by price (Low to High): " + current + " > " + next);
		}

	}

}