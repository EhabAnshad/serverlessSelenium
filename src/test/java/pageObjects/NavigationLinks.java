package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class NavigationLinks extends PageBase {

	@FindBy (linkText="Users")
	private WebElement usersPage;
	
	@FindBy (linkText="Products")
	private WebElement productPage;
	
	
	public NavigationLinks(WebDriver driver) {
		super(driver);
	}

	public UsersPage openUsers() {
		waitForPageToLoad();
		waitForJQuery();
		usersPage.click();
		waitForPageToLoad();
		waitForJQuery();
		return new UsersPage(driver);
	}
	
	public ProductsPage openProducts() {
		waitForPageToLoad();
		waitForJQuery();
		productPage.click();
		return new ProductsPage(driver);
	}

}
