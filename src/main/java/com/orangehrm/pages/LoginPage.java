package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {
	
	private ActionDriver actionDriver;
	
	//Define Locators using By class
	
	private By usernameField = By.name("username");
	private By passwordField = By.xpath("//input[@type='password']");
	private By loginButton = By.xpath("//button[text()=' Login ']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

	//Initialize ActionDriver object by passing WebDriver instance
	/*public LoginPage(WebDriver driver) {
		this.actionDriver = new ActionDriver(driver);
	}*/
	
	public LoginPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	//Method to perform login
	public void login(String username, String password) {
		actionDriver.enterText(usernameField, username);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginButton);
	}
	
	//Method to check if error message is displayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}
	
	//Method to get the text from error message
	public String getErrorMessage() {
		return actionDriver.getText(errorMessage);
	}
	
	//Verify if error is correct or not
	public boolean verifyErrorMessage(String expectedText) {
		return actionDriver.compareText(errorMessage, expectedText);
	}
	
	public boolean isLoginButtonVisible() {
		return actionDriver.isDisplayed(loginButton);
	}
}
