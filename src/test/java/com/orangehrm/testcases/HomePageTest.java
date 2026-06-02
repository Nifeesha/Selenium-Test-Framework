package com.orangehrm.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass{
	
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setUpPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	
	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void T01_verifyOrangeHRMLogo(String username, String password) {
		//ExtentManager.startTest("Verify home page logo Test"); --> This has been implemented in TestListener
		ExtentManager.logStep("Navigating to login page entering username and password");
		loginPage.login(username, password);
		ExtentManager.logStep("Verifying Logo is visible or not");
		Assert.assertTrue(homePage.isOrangeHRMLogoVisible(), "Logo doesn't match");
		ExtentManager.logStep("Validation successful");
		staticWait(2);
	}

}
