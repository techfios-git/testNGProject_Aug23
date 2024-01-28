package variousConcepts;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class CrmTest {
	
	WebDriver driver;
	ExtentReports extent;
	ExtentTest test;
	
	String browser;
	String url;
	String userName;
	String password;
	
	String dashboardValidationText = "Dashboard";
	String userAlertValidationText = "Please enter your user name";
	String passwordAlertValidationText = "Please enter your password";
	String addCustomerValidationText = "New Customer1";
	String fullName = "Selenium";
	String company = "Techfios";
	String email = "demo@techifos.com";
	String phone = "123456";
	String country = "Aruba";

	// Element list - By type
	By USER_NAME_FIELD = By.xpath("//input[@id='user_name']");
	By PASSWORD_FIELD = By.xpath("//*[@id=\"password\"]");
	By SINGIN_BUTTON_FIELD = By.xpath("//*[@id=\"login_submit\"]");
	By DASHBOARD_VALIDATION_FIELD = By.xpath("/html/body/div[1]/section/div/div[2]/div/div/header/div/strong");
	By CUSTOMER_MENU_FIELD = By.xpath("/html/body/div[1]/aside[1]/div/nav/ul[2]/li[2]/a/span");
	By ADD_CUSTOMER_MENU_FIELD = By.xpath("//*[@id=\"customers\"]/li[2]/a/span");
	By ADD_CUSTOMER_VALIDATION_FIELD = By.xpath("/html/body/div[1]/section/div/div[2]/div/div[1]/div[1]/div/div/header/div/strong");
	By FULL_NAME_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[1]/div/input");
	By COMPANY_DROPWOWN_FIELD = By.xpath("//select[@name='company_name']");
	By EMAIL_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[3]/div/input");
	By PHONE_FIELD = By.xpath("//*[@id=\"phone\"]");
	By COUNTRY_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[8]/div[1]/select");
	
	@BeforeTest
	public void reportGenerator() {
		
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("extentReport1.html");
		
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		
		
	}
	
	@AfterTest
	public void reporterClose() {
		extent.flush();
	}
	
	@BeforeClass
	public void readConfig() {
		//InputStream //BufferedReader //FileReader //Scanner
		
		try {
			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			System.out.println("Browser used: " + browser);
			url = prop.getProperty("url");
			userName = prop.getProperty("userName");
			password = prop.getProperty("password");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@BeforeMethod
	public void init() {
		
		if(browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		}else if(browser.equalsIgnoreCase("edge")){
			System.setProperty("webdriver.edge.driver", "drivers\\msedgedriver.exe");
			driver = new EdgeDriver();
		}else {
			System.out.println("Please define a valid browser.");
		}
		
		driver.manage().deleteAllCookies();
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}
	
	@Test(priority = 1)
	public void testLogin() {
		test = extent.createTest("Login Test", "Description");
		test.log(Status.INFO, "Login Test");
		
		driver.findElement(USER_NAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(SINGIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.findElement(DASHBOARD_VALIDATION_FIELD).getText(), dashboardValidationText, "Dashboard page is not avliable!");
	}
	
	@Test(priority = 2)
	public void testAlert() {
		test = extent.createTest("Alert Test", "Description");
		test.log(Status.INFO, "Alert Test");
		driver.findElement(SINGIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.switchTo().alert().getText(), userAlertValidationText, "Alert is not available!");
		driver.switchTo().alert().accept();
		
		driver.findElement(USER_NAME_FIELD).sendKeys(userName);
		driver.findElement(SINGIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.switchTo().alert().getText(), passwordAlertValidationText, "Alert is not available!");
		driver.switchTo().alert().accept();
		
	}
	
	@Test(priority = 3)
	public void testAddCustomer() {
		test = extent.createTest("Add Customer Test", "Description");
		test.log(Status.INFO, "Add Customer Test");
		testLogin();
		driver.findElement(CUSTOMER_MENU_FIELD).click();
		driver.findElement(ADD_CUSTOMER_MENU_FIELD).click();
		Assert.assertEquals(driver.findElement(ADD_CUSTOMER_VALIDATION_FIELD).getText(), addCustomerValidationText, "Add Customer page is not available!");

		driver.findElement(FULL_NAME_FIELD).sendKeys(fullName + generateRandomNum(999));
		
		selectFromDropdown(driver.findElement(COMPANY_DROPWOWN_FIELD), company);
//		selectFromDropdown(COMPANY_DROPWOWN_FIELD, company);
		
		driver.findElement(EMAIL_FIELD).sendKeys(generateRandomNum(99999) + email);
		driver.findElement(PHONE_FIELD).sendKeys(phone + generateRandomNum(9999));
		
		selectFromDropdown(driver.findElement(COUNTRY_FIELD), country);
		
		
	}

	private void selectFromDropdown(By elementField, String visibleText) {
		
		Select sel = new Select(driver.findElement(elementField));
		sel.selectByVisibleText(visibleText);
	}

	private void selectFromDropdown(WebElement element, String visibleText) {
		
		Select sel = new Select(element);
		sel.selectByVisibleText(visibleText);
		
	}

	public int generateRandomNum(int boundNum) {
		Random rnd = new Random();
		int randomNum = rnd.nextInt(boundNum);
		return randomNum;
	}



}
