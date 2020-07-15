package com.serverlessSelenium;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import com.serverlessSelenium.driver.ChromeLambdaDriver;
import com.serverlessSelenium.driver.LambdaDriver;
import com.serverlessSelenium.lambda.LambdaFunctionHandler;

public abstract class LambdaBaseTest {
	public WebDriver driver;

	@BeforeClass
	public void baseTestBeforeClass() {
		LambdaDriver chromeDriver = new ChromeLambdaDriver();
		driver = chromeDriver.createSession();
	}

	@AfterClass
	public void baseTestAfterClass() {
		driver.quit();
	}

	@AfterMethod
	public void screeshotOnFailure(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			screenshot(result.getName());
		}
	}

	protected void screenshot(String description) {
		LambdaFunctionHandler.addScreenShot(description + ".png",
				((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
	}
}
