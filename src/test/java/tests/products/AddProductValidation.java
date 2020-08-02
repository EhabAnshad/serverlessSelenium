package tests.products;

import org.testng.Assert;
import org.testng.annotations.Test;

import helpers.GenerateProduct;
import models.Product;
import pageObjects.HomePage;
import pageObjects.ProductsPage;
import tests.TestBase;

public class AddProductValidation extends TestBase {

    @Test 
    public void AddProductValidationTest() {
    	//setup
    	Product testProduct = new GenerateProduct();
    	testProduct.setProductName("");
    	
    	ProductsPage productsPage =  new HomePage(driver).openProducts();
    	    	
    	String result =  productsPage.addNewProduct(testProduct).getResults();
    	
    	Assert.assertTrue(result
    			.equalsIgnoreCase("Well this is embarrassing. It looks like we're having trouble creating the product."),
    			result);
    }
}
