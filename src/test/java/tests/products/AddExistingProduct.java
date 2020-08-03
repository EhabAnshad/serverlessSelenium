package tests.products;

import org.testng.Assert;
import org.testng.annotations.Test;

import helpers.GenerateProduct;
import models.Product;
import pageObjects.HomePage;
import pageObjects.ProductsPage;
import tests.TestBase;

public class AddExistingProduct extends TestBase {

    @Test 
    public void AddExistingProductTest() {
    	//setup
    	Product testProduct = new GenerateProduct();
    	HomePage homePageObject = new HomePage(driver);
    	
    	ProductsPage productsPage =  homePageObject.openProducts();
    	    	
    	String result =  productsPage
    			.addNewProduct(testProduct)
    			.refreshProductlist()
    			.addExistingNewProduct(testProduct)
    			.getResults();
    	Assert.assertTrue(result
    			.equalsIgnoreCase("Duplicate product found, no action been taken"),
    			result);
    	
    	//clean up 
    	productsPage.refreshProductlist();
    	productsPage.deleteProduct(testProduct);
    }
}
