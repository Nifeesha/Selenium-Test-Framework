package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
//	protected static WebDriver driver;
//	private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

	// Getter method for soft assert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	// Load configuration file
	@BeforeSuite // It only initializes once
	public void loadConfig() throws IOException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");

		// Start the Extent Report
		// ExtentManager.getReporter(); --> This has been implemented in TestListener
	}

	@SuppressWarnings("deprecation")
	@BeforeMethod
	@Parameters("browser")
	public synchronized void setUp(String browser) throws IOException {
		System.out.println("Setting up webDriver for: " + this.getClass().getSimpleName());
		launchBrowser(browser);
		configureBrowser();
		staticWait(2);

		logger.info("WebDriver initialized and Browser maximized");
		logger.trace("This is a trace message");
		logger.error("This is a error message");
		logger.debug("This is a debug message");
		logger.fatal("This is a fatal message");
		logger.warn("This is a warn message");

		// Initialize the actionDriver only once
		/*
		 * if(actionDriver==null) { actionDriver = new ActionDriver(driver);
		 * logger.info("ActionDriver instance is created." +
		 * Thread.currentThread().getId()); }
		 */

		// Initialize ActionDriver for the current Thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized for thread: " + Thread.currentThread().threadId());
	}

	/*
	 * Initialize the WebDriver based on browser defined in config.properties file
	 */
	private synchronized void launchBrowser(String browser) {
		// String browser = prop.getProperty("browser");

		boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
		String gridURL = prop.getProperty("gridURL");

		if (seleniumGrid) {
			try {
				if (browser.equalsIgnoreCase("chrome")) {
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
					driver.set(new RemoteWebDriver(new URL(gridURL), options));
				} else if (browser.equalsIgnoreCase("firefox")) {
					FirefoxOptions options = new FirefoxOptions();
					options.addArguments("-headless");
					driver.set(new RemoteWebDriver(new URL(gridURL), options));
				} else if (browser.equalsIgnoreCase("edge")) {
					EdgeOptions options = new EdgeOptions();
					options.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
					driver.set(new RemoteWebDriver(new URL(gridURL), options));
				} else {
					throw new IllegalArgumentException("Browser Not Supported: " + browser);
				}
				logger.info("RemoteWebDriver instance created for Grid in headless mode");
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid Grid URL", e);
			}
		} else {

			if (browser.equalsIgnoreCase("chrome")) {

				// Create ChromeOptions
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless");// Run chrome in headless mode
				options.addArguments("--disable-gpu");// Disable GPU for headless mode
				options.addArguments("--disable-notifications");// Disable browser notifications
				options.addArguments("--no-sandbox");// Required for some CI environments
				options.addArguments("--disable-dev-shm-usage");// Resolve issues in resources

				// driver = new ChromeDriver();
				driver.set(new ChromeDriver());// New changes as per Thread
				ExtentManager.registerDriver(getDriver());
				logger.info("ChromeDriver instance is created");
			} else if (browser.equalsIgnoreCase("firefox")) {

				// Create FirefoxOptions
				FirefoxOptions options = new FirefoxOptions();

				options.addArguments("--headless");// Run Firefox in headless mode
				options.addArguments("--disable-gpu");// Disable GPU for headless mode
				options.addArguments("--width=1920");// Set browser width
				options.addArguments("--height=1080");// Set browser height
				options.addArguments("--disable-notifications");// Disable browser notifications
				options.addArguments("--no-sandbox");// Required for some CI environments
				options.addArguments("--disable-dev-shm-usage");// Resolve issues in resources

				// driver = new FirefoxDriver();
				driver.set(new FirefoxDriver());// New changes as per Thread
				ExtentManager.registerDriver(getDriver());
				logger.info("FirefoxDriver instance is created");
			} else if (browser.equalsIgnoreCase("edge")) {

				EdgeOptions options = new EdgeOptions();

				options.addArguments("--headless");// Run Firefox in headless mode
				options.addArguments("--disable-gpu");// Disable GPU for headless mode
				options.addArguments("--window-size=1920,1080");// Set window size
				options.addArguments("--disable-notifications");// Disable pop-up notifications
				options.addArguments("--no-sandbox");// Needed for CI/CD
				options.addArguments("--disable-dev-shm-usage");// Resolve issues in resources

				// driver = new EdgeDriver();
				driver.set(new EdgeDriver());// New changes as per Thread
				ExtentManager.registerDriver(getDriver());
				logger.info("EdgeDriver instance is created");
			} else {
				throw new IllegalArgumentException("Browser not supported: " + browser);
			}
		}
	}

	/*
	 * Configure browser settings such as implicit wait, maximize browser navigate
	 * to the URL
	 */
	private void configureBrowser() {
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		boolean seleniumGrid = Boolean.parseBoolean(System.getProperty("seleniumGrid", prop.getProperty("seleniumGrid")));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		getDriver().manage().window().maximize();

		// Navigate to URL
		/*try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to Navigate to the URL: " + e.getMessage());
		}*/
		
		if (seleniumGrid) {
			getDriver().get(prop.getProperty("url_grid"));
		} else {
			getDriver().get(prop.getProperty("url"));
		}
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (driver != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quit the driver: " + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed.");
//		driver=null;
//		actionDriver=null;

		driver.remove();
		actionDriver.remove();
		// ExtentManager.endTest(); --> This has been implemented in TestListener
	}

	// Getter method for prop
	public static Properties getProp() {
		return prop;
	}

	// Setter method for prop
	public void setProp(Properties prop) {
		this.prop = prop;
	}

	// Driver getter method
	/*
	 * public WebDriver getDriver() { return driver; }
	 */

	// Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	// Getter Method for WebDriver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();
	}

	// Getter Method for ActionDriver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actionDriver.get();
	}

	// Static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}
