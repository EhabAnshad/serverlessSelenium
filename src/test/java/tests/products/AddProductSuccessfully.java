package tests.products;

import org.testng.Assert;
import org.testng.annotations.Test;

import helpers.GenerateProduct;
import models.Product;
import pageObjects.HomePage;
import pageObjects.ProductsPage;
import tests.TestBase;

public class AddProductSuccessfully extends TestBase {

    @Test 
    public void AddProductTest() {
    	//setup
    	Product testProduct = new GenerateProduct();
    	HomePage homePageObject = new HomePage(driver);
    	
    	ProductsPage productsPage =  homePageObject.openProducts();
    	    	
    	String result =  productsPage.addNewProduct(testProduct).getResults();
    	Assert.assertTrue(result
    			.equalsIgnoreCase("Product added successfully, refresh the page to load new data."),
    			result);
    	
    	//clean up 
    	productsPage.refreshPage();
    	productsPage.deleteProduct(testProduct);
    }
}
