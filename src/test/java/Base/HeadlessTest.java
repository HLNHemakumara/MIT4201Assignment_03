package Base;

import Utils.TakeErrorScreenShots;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HeadlessTest {
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected static ExtentTest test;
    private String reportFileName;

    @BeforeSuite
    public void setUp() {
        // Create a unique file name for the Extent Report
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        reportFileName = "ExtentReport_" + timestamp + ".html"; // Default report name

        // Set up ExtentSparkReporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("./reports/" + reportFileName);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    public void setReportName(String customName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        reportFileName = customName + "_" + timestamp + ".html";

        // Reinitialize ExtentSparkReporter with the new name
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("./reports/" + reportFileName);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    public void startTest(String testName) {
        test = extent.createTest(testName);
    }

    @BeforeClass
    public void setUpBrowser() {
        // Set up ChromeOptions for headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run browser in headless mode
        options.addArguments("--disable-gpu"); // Disable GPU usage
        options.addArguments("--window-size=1920,1080"); // Set window size
        options.addArguments("--ignore-certificate-errors"); // Handle SSL certificate errors
        options.addArguments("--disable-extensions"); // Disable extensions for a clean environment

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.ebay.com/");
    }

    @AfterMethod
    public void captureScreenshotOnFailure(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            TakeErrorScreenShots.takeScreenshot(driver, result.getName());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Close all browser windows and end the WebDriver session
        }
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush();
    }
}
