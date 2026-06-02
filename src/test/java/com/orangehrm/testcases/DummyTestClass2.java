package com.orangehrm.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyTestClass2 extends BaseClass{
	
	@Test
	public void dummyTest2() {
		
		//ExtentManager.startTest("DummyTest2 Test"); --> This has been implemented in TestListener
		String actualTitle = getDriver().getTitle();
		String expectedTitle = "OrangeHRM";
		ExtentManager.logStep("Verifying the title");
		Assert.assertEquals(actualTitle, expectedTitle);
		System.out.println("Test passed - Title is Matching");
		ExtentManager.logStep("Validation Successful");
	}

}
