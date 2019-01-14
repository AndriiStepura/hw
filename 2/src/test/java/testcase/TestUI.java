package testcase;

import java.util.*;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.annotations.Optional;

import static org.testng.AssertJUnit.assertTrue;

public class TestUI {
	private WebDriver driver;	
	
	static Logger logger = Logger.getLogger(TestUI.class);

	@Parameters({"browser", "dimensionW", "dimensionH", "driverPath"})
	@BeforeTest
	public void initDriver(String browser, String dimensionW, String dimensionH, @Optional("") String driverPath) throws Exception {
		logger.info("You are testing on browser " + browser + " with dimension "+ dimensionW + "X" + dimensionH);
		browser = browser.toLowerCase();
		if (!driverPath.equals("")) {
			System.setProperty("webdriver.chrome.driver", driverPath);
		}
		if (browser.equals("chrome")) {			
			driver = new ChromeDriver();
			// Repeat test with different dimensions
			if (dimensionW.equals("max")){
				driver.manage().window().maximize();
			}
			else {
				Dimension d = new Dimension(Integer.parseInt(dimensionW), Integer.parseInt(dimensionH));
				driver.manage().window().setSize(d);
			}
		} else if (browser.equals("firefox")) {
			driver = new FirefoxDriver();
		} else {
			throw new RuntimeException("Please create a driver for " + browser);
		}

	}

	@Parameters({"environment","dimensionH"})
	@Test
	public void searchResultsAlphabeticalOrder(String environment,String dimensionH) throws InterruptedException {
		driver.navigate().to(environment);
		driver.findElement(By.id("home-search-keywords")).sendKeys("Dublin, Ireland");

		//Wait for suggestion addresses
		WebDriverWait waitSuggestionAddresses = new WebDriverWait(driver, 20); //seconds
		waitSuggestionAddresses.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@class, 'suggestion')]")));

		driver.findElement(By.xpath("(//a[contains(@class, 'suggestion')])[1]")).click();
		driver.findElement(By.xpath("//button[contains(@title, 'SEARCH')]")).click();

		// Optional for print search results before use sort button
		List<String> searchResultHostelsTitlesBeforeSort = helpers.converter.getTitles(driver.findElements(By.xpath("//div[@id='fabResultsContainer']//h2/a")));
		logger.info("Search result hostels before sort: " + searchResultHostelsTitlesBeforeSort);

		//Wait for results sort button by Name
		WebDriverWait wait = new WebDriverWait(driver, 10); //seconds
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("dd.topfilter.rounded.sort-button > span")));

		WebElement sortButton = driver.findElement(By.cssSelector("dd.topfilter.rounded.sort-button > span"));
		//Scroll to button for small dimensions
		if (!dimensionH.equals("max") && Integer.parseInt(dimensionH) < 680){
			Actions scrollToSortButton = new Actions(driver);
			scrollToSortButton.moveToElement(sortButton);
			scrollToSortButton.perform();
			//Additional scroll for move button out from cookies accept overlay
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,"+Integer.parseInt(dimensionH)+");");
		}
		sortButton.click();

		WebDriverWait waitSortByName = new WebDriverWait(driver, 10); //seconds
		waitSortByName.until(ExpectedConditions.visibilityOfElementLocated(By.id("sortByName")));
		WebElement sortByNameButton = driver.findElement(By.id("sortByName"));
		Actions scrollByNameButton = new Actions(driver);
		scrollByNameButton.moveToElement(sortByNameButton);
		scrollByNameButton.perform();
		sortByNameButton.click();

		List<String> searchResultHostelsNames = helpers.converter.getTitles(driver.findElements(By.xpath("//div[@id='fabResultsContainer']//h2/a")));

		logger.info("Search result hostels after use sort by Name: " + searchResultHostelsNames);
		logger.info("Search result hostels are displayed in alphabetical order - " + helpers.isSorted.isAlphabeticallySorted(searchResultHostelsNames));
		logger.info(Strings.repeat("=", 15));
		assertTrue(helpers.isSorted.isAlphabeticallySorted(searchResultHostelsNames));
	}

	@AfterTest
	public void quitDriver() throws Exception {
		driver.quit();
	}

}
