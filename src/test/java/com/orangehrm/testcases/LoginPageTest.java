package com.orangehrm.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass{
	
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setUpPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	
	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void T01_verifyValidLoginTest(String username, String password) {
		//ExtentManager.startTest("Valid Login Test"); --> This has been implemented in TestListener 
		System.out.println("Running testMethod1 on thread: " + Thread.currentThread().threadId());
		ExtentManager.logStep("Navigating to login page entering username and password");
		loginPage.login(username, password);
		ExtentManager.logStep("Verifying Admin tab is visible or not");
		Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab should be viisble after login");
		ExtentManager.logStep("Validation successful");
	}
	
	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void T02_verifyValidLogoutTest(String username, String password) {
		loginPage.login(username, password);
		homePage.logout();
		ExtentManager.logStep("Logged out successfully!");
		staticWait(2);
		Assert.assertTrue(loginPage.isLoginButtonVisible(), "Successfully Logout");
	}

	@Test(dataProvider = "invalidLoginData", dataProviderClass = DataProviders.class)
	public void T03_invalidLogin(String username, String password) {
		//ExtentManager.startTest("Invalid Login Test!"); --> This has been implemented in TestListener
		System.out.println("Running testMethod1 on thread: " + Thread.currentThread().threadId());
		ExtentManager.logStep("Navigating to login page entering username and password");
		loginPage.login(username, password);
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Test Failed: Invalid error message");
		ExtentManager.logStep("Validation successful");

	}
}
