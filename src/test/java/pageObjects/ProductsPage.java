package pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import models.Product;

public class ProductsPage extends NavigationLinks {
	
	@FindBy (name="productName")
	private List<WebElement> productNames;
	
	@FindBy (id="update")
	private WebElement updateButton;
	
	@FindBy (id="priceToUpdate")
	private WebElement productPriceToUpdate;
	
	@FindBy (id="product")
	private WebElement productName;

	@FindBy (id="price")
	private WebElement productPrice;
	
	@FindBy (id="addProduct")
	private WebElement addProductButton;
	
	@FindBy (id="delete")
	private WebElement deleteButton;
	
	@FindBy (id="addNewProduct")
	private WebElement addNewProduct;
	
	@FindBy (id="productErrorText")
	private WebElement errorText;
	
	@FindBy (id="productDuplicateText")
	private WebElement duplicateText;
	
	@FindBy (id="productAddedSuccessText")
	private WebElement successText;
	
	public ProductsPage(WebDriver driver) {
		super(driver);
		waitForPageToLoad();
		waitForJQuery();
	}
	
	public ProductsPage addNewProduct(Product product) {
		clickElement(By.id("addNewProduct"));
		waitForJQuery();
		int tries = 4;
		boolean match = true;
		while (match && tries > 0) {
			waitForElementToBeVisible(By.id("product"));
			productName.clear();
			productName.sendKeys(product.getProductName());
			match = !productName.getAttribute("value").equalsIgnoreCase(product.getProductName());
			tries--;
		}
		productPrice.clear();
		productPrice.sendKeys(product.getPrice());
		waitForJQuery();
		addProductButton.click();
		waitForJQuery();
		return this;
	}
	
	public ProductsPage addExistingNewProduct(Product product) {
		clickElement(By.id("addNewProduct"));
		waitForJQuery();
		waitForElementToBeVisible(By.id("product"));
		waitForJQuery();
		productName.clear();
		productName.sendKeys(product.getProductName());
		productPrice.clear();
		productPrice.sendKeys(product.getPrice());
		waitForJQuery();
		addProductButton.click();
		waitForJQuery();
		return this;
	}
	
	
	public boolean doesProductExists(Product product) {
		return productNames.stream()
		.filter(s-> s.getText().equalsIgnoreCase(product.getProductName()))
		.count() > 0;
	}

	public ProductsPage editProduct(Product oldProduct, Product updatedProduct) {
		By selector = By.cssSelector("a[data-id='" + oldProduct.getProductName() + "']");
		WebElement editButton = driver.findElement(selector);
		editButton.click();
		waitForJQuery();
		waitForElementToBeVisible(By.id("priceToUpdate"));
		productPriceToUpdate.clear();
		productPriceToUpdate.sendKeys(updatedProduct.getPrice());
		updateButton.click();
		return this;
	}

	public String getProductPrice(Product testProduct) {
		WebElement product = driver.findElement(By.id(buildProductSelector(testProduct)));
		return product.getText();
	}
		
	public ProductsPage deleteProduct(Product testProduct) {
		By selector = By.name(testProduct.getProductName() + "_delete");
		clickElement(selector);
		waitForElementToBeVisible(By.id("delete"));	
		deleteButton.click();
		return this;
	}
	
	public String getResults() {
		if(errorText.isDisplayed())
		{
			return errorText.getText();
		}
		
		if(duplicateText.isDisplayed())
		{
			return duplicateText.getText();
		}
		
		return successText.getText() ;
	}
	
	public ProductsPage refreshProductlist(){
		refreshPage();
		return this;
	}

	
	private String buildProductSelector(Product testProduct) {
		return testProduct.getProductName() + "_" + testProduct.getPrice();
	}



}
