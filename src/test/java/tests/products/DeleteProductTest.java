package tests.products;

import org.testng.Assert;
import org.testng.annotations.Test;

import helpers.GenerateProduct;
import models.Product;
import pageObjects.HomePage;
import pageObjects.ProductsPage;
import tests.TestBase;

public class DeleteProductTest extends TestBase {

    @Test 
    public void VerifyProductExistsAfterDeletion() {
    	//setup
    	Product testProduct = new GenerateProduct();
    	HomePage homePageObject = new HomePage(driver);
    	
    	ProductsPage productsPage =  homePageObject.openProducts();
    	    	
    	String result =  productsPage.addNewProduct(testProduct).getResults();
    	Assert.assertTrue(result
    			.equalsIgnoreCase("Product added successfully, refresh the page to load new data."),
    			result);
    	//force refresh as page doesn't automatically load new added products
    	productsPage.refreshPage();
    	productsPage.deleteProduct(testProduct);
    	boolean productExists = new ProductsPage(driver).doesProductExists(testProduct);
    	Assert.assertFalse(productExists, "User is not deleted" + testProduct.getProductName());
    	
    	
    }
}
