package com.orangehrm.testcases;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyTestClass extends BaseClass{
	
	@Test
	public void dummyTest() {
		//Test checkin
		//ExtentManager.startTest("DummyTest1 Test"); --> This has been implemented in TestListener
		String actualTitle = getDriver().getTitle();
		String expectedTitle = "OrangeHRM";
		ExtentManager.logStep("Verifying the title");
		Assert.assertEquals(actualTitle, expectedTitle);
		System.out.println("Test passed - Title is Matching");
//		ExtentManager.logSkip("This case is skipped");
//		throw new SkipException("Skipping the test as part of Testing");
	}

}
