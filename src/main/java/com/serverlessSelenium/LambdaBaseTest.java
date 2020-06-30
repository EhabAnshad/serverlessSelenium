package com.serverlessSelenium;

import org.junit.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.serverlessSelenium.driver.ChromeLambdaDriver;
import com.serverlessSelenium.driver.LambdaDriver;
import com.serverlessSelenium.lambda.LambdaFunctionHandler;



public abstract class LambdaBaseTest {
	protected WebDriver driver;

	@Before
	public void baseTestBeforeClass() {		
		LambdaDriver chromeDriver = new ChromeLambdaDriver();
		driver = chromeDriver.createSession();
	}
	
	 protected void screenshot(String description) {
		 LambdaFunctionHandler.addAttachment(description + ".png",
	                    ((TakesScreenshot) driver)
	                            .getScreenshotAs(OutputType.BYTES));
	    }
}
