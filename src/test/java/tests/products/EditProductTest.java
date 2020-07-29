package tests.products;

import org.testng.Assert;
import org.testng.annotations.Test;

import helpers.GenerateProduct;
import models.Product;
import pageObjects.HomePage;
import pageObjects.ProductsPage;
import tests.TestBase;

public class EditProductTest extends TestBase {

    @Test 
    public void EditProductTests() {
    	//setup
    	Product testProduct = new GenerateProduct();
    	HomePage homePageObject = new HomePage(driver);
    	
    	ProductsPage productsPage =  homePageObject.openProducts();
    	    	
    	String result =  productsPage.addNewProduct(testProduct).getResults();
    	Assert.assertTrue(result
    			.equalsIgnoreCase("Product added successfully, refresh the page to load new data."),
    			result);
    	
    	productsPage.refreshPage();
    	Product updatedProduct = new GenerateProduct();
    	updatedProduct.setProductName(testProduct.getProductName());
    	productsPage.editProduct(testProduct, updatedProduct).refreshPage();
    	String updatedProductName = new ProductsPage(driver).getProductPrice(updatedProduct);
    	Assert.assertEquals(updatedProductName,  updatedProduct.getPrice());
    	
    	//clean up 
    	productsPage.deleteProduct(updatedProduct);
    }
}
