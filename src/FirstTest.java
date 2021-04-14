import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "8.0.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "/Users/yuriyfromrussia/Downloads/JavaAppiumAutomation/apks/org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void firstTest() {
        waitForElementAndClick
                (
                        By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                        "Cannot find Search Wikipedia input",
                        5
                );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementPresent
                (By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                                "//*[@text='Object-oriented programming language']"),
                        "Cannot find 'Object-oriented programming language' topic searching by Java",
                        15
                );
    }

    @Test
    public void testCancelSearch() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find Search Wikipedia input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search field",
                5
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search",
                5
        );

        waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "X is still present on the page",
                5
        );
    }

    @Test
    public void testCompareArticleTitle() {
        waitForElementAndClick
                (
                        By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                        "Cannot find Search Wikipedia input",
                        5
                );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClick
                (
                        By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                                "//*[@text='Object-oriented programming language']"),
                        "Cannot find Search Wikipedia input",
                        5
                );

        WebElement title_element = waitForElementPresent
                (
                        By.id("org.wikipedia:id/view_page_title_text"),
                        "Cannot find article title",
                        8
                );

        String article_title = title_element.getAttribute("text");

        Assert.assertEquals(
                "We see unexpected title",
                "Java (programming language)",
                article_title
        );
    }

    @Test
    public void testCheckSearchFieldText() {
        waitForElementAndClick
                (
                        By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                        "Cannot find Search Wikipedia input",
                        5
                );

        assertElementHasText(
                By.id("org.wikipedia:id/search_src_text"),
                "Search…",
                "Cannot locate Search element",
                6
        );
    }

    @Test
    public void testSearchCancel() {
        waitForElementAndClick
                (
                        By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                        "Cannot find Search Wikipedia input",
                        5
                );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Macbook",
                "Cannot find search input",
                5
        );

        assertResultsNumber(
                By.id("org.wikipedia:id/page_list_item_container"),
                "Cannot find search results",
                2,
                5
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search field",
                5
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search",
                5
        );

        waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "X is still present on the page",
                5
        );

    }

    @Test
    public void testSearchItemsTitle() {
        waitForElementAndClick
                (
                        By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                        "Cannot find Search Wikipedia input",
                        5
                );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Macbook",
                "Cannot find search input",
                5
        );
        assertItemsTitleContainsText(
                By.id("org.wikipedia:id/page_list_item_title"),
                "Element is not found",
                "Macbook",
                7
        );
    }

    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementPresent(By by, String error_message) {
        return waitForElementPresent(by, error_message, 5);
    }

    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    private void assertElementHasText(By by, String expected_text, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        String element_text = element.getAttribute("text");
        Assert.assertEquals("Element's text is not matching with expected text", expected_text, element_text);
    }

    private void assertResultsNumber(By by, String error_message, int expected_number, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        List<WebElement> search_results = driver.findElements(by);
        Assert.assertTrue("Number of articles is less than " + expected_number, expected_number <= search_results.size());
    }

    private void assertItemsTitleContainsText(By by, String error_message, String expected_text, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        List<WebElement> search_results = driver.findElements(by);
        for (WebElement el : search_results) {
            Assert.assertTrue("Item title is not matching text " + expected_text, el.getText().toLowerCase().contains(expected_text.toLowerCase()));
        }
    }

}
