package TestCases;

import Base.HeadlessTest;
import Pages.HomePage;
import Pages.ProductPage;
import Pages.SearchResultsPage;
import Utils.ExcelHandler;
import Utils.TakeErrorScreenShots;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class EbayWatchlistHeadless extends HeadlessTest {

    @BeforeTest
    public void setup() {
        setUpBrowser(); // Initialize the headless browser
    }

    @Test
    public void searchAndAddToWatchlist() {
        HomePage homePage = new HomePage(driver);
        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        ProductPage productPage = new ProductPage(driver);

        // Initialize Excel Information
        String excelFilePath = "src/test/resources/testdata/TestData.xlsx";
        String sheetName = "Data";

        // Initialize ExcelHandler
        ExcelHandler excel = new ExcelHandler(excelFilePath, sheetName);

        // Read data
        String mattressBrand = excel.getCellData(1, 1); // Row 1, Column 1

        // Step 1: Search for the mattress brand
        homePage.searchFor(mattressBrand);
        setReportName("Test Case - Headless Watchlist");
        startTest("Add To Watchlist Scenario - Headless Mode");
        test = extent.createTest("Successful Search", "System successfully searched the item and retrieved results");
        String screenshotPath1 = TakeErrorScreenShots.takeScreenshot(driver, "SuccessfulSearch");
        test.pass("System successfully searched the item and retrieved results").addScreenCaptureFromPath(screenshotPath1);

        // Step 2: Select the first product
        searchResultsPage.selectFirstProduct();
        test = extent.createTest("First Item Selected", "System successfully selected the first search result");
        String screenshotPath2 = TakeErrorScreenShots.takeScreenshot(driver, "FirstResultTaken");
        test.pass("System successfully selected the first search result").addScreenCaptureFromPath(screenshotPath2);

        // Write data back to the Excel file
        excel.setCellData(1, 2, "First Product Selected", excelFilePath);

        // Step 3: Add the product to the watchlist
        productPage.addToWatchList();
        test = extent.createTest("Add to Watchlist Successful", "System successfully added the item to the watchlist");
        String screenshotPath3 = TakeErrorScreenShots.takeScreenshot(driver, "AddToWatchList");
        test.pass("System successfully added the item to the watchlist").addScreenCaptureFromPath(screenshotPath3);

        // Close the Excel workbook
        excel.closeWorkbook();
    }
}
