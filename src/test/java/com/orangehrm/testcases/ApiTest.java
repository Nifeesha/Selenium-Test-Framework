package com.orangehrm.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest {

	@Test
	public void verifyGetUserAPI() {
		
		SoftAssert softAssert = new SoftAssert();

		// Step1: Define API Endpoint
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint: " + endPoint);

		// Step2: Send GET Request
		ExtentManager.logStep("Sending GET Request to the API");
		Response response = ApiUtility.sendGetRequest(endPoint);

		// Step3: Validate status code
		ExtentManager.logStep("Validating API Response status code");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);

		softAssert.assertTrue(isStatusCodeValid, "Status code is not as expected");

		if (isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status code validation passed!");
		} else {
			ExtentManager.logFailureAPI("Status code validation failed!");
		}

		// Step4: Validate username
		ExtentManager.logStep("Validating response body for username");
		String username = ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(username);
		softAssert.assertTrue(isUserNameValid, "Username is not valid");
		if (isUserNameValid) {
			ExtentManager.logStepValidationForAPI("Username validation passed!");
		} else {
			ExtentManager.logFailureAPI("Username validation failed!");
		}

		// Step5: Validate email
		ExtentManager.logStep("Validating response body for email");
		String userEmail = ApiUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
		softAssert.assertTrue(isEmailValid, "Email is not valid");
		if (isEmailValid) {
			ExtentManager.logStepValidationForAPI("Email validation passed!");
		} else {
			ExtentManager.logFailureAPI("Email validation failed!");
		}
		
		softAssert.assertAll();

	}
}
