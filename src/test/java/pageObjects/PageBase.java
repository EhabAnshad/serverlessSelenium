package pageObjects;



import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

import configurations.ApplicationConfigurations;


/**
 * This Class is a Page Base for the other Classes.
 */

public class PageBase {
	protected WebDriver driver;

	public PageBase(WebDriver driver)
	{
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}

	public WebElement fluentWait(final By selector)
	{
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(ApplicationConfigurations.driverDefaultFluentWait))
				.pollingEvery(Duration.ofSeconds(1))
				.ignoring(NoSuchElementException.class);
		return wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver)
			{
				return driver.findElement(selector);
			}
		});
	}
	
	public void waitForElementToBeVisible(final By selector)
	{
		WebDriverWait wait = new WebDriverWait(driver, 45);
		wait.until(
		        ExpectedConditions.visibilityOfElementLocated(selector));
	}
	
	public void waitForElementToBeInvisible(final By selector)
	{
		WebDriverWait wait = new WebDriverWait(driver, 45);
		wait.until(
		        ExpectedConditions.invisibilityOfElementLocated(selector));
	}
	
	public void waitForPageToLoad()
	{
	   new WebDriverWait(driver, 45)
	   		.until((ExpectedCondition<Boolean>) 
				   wd ->((JavascriptExecutor) wd)
				   .executeScript("return document.readyState")
				   .equals("complete"));
	}

	public void waitForJQuery() 
	{
		new WebDriverWait(driver, 45)
				.until((ExpectedCondition<Boolean>) 
						wd ->((JavascriptExecutor) wd)
						.executeScript("return !!window.jQuery && window.jQuery.active == 0")
								   .equals(true));
	}
	public void clickElement(final By selector) {
		WebElement element = driver.findElement(selector);
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", element);
	}
	
	public void threadSleepWaitIsBad(int wait) {
		try {Thread.sleep(wait);} catch (InterruptedException e) {}
	}
	
	public String getCurrentPageTitle()
	{
		return driver.getCurrentUrl();
	}

	public void refreshPage() {
		driver.navigate().refresh();
		waitForPageToLoad();
		waitForJQuery();
	}
}