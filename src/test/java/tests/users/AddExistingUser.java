package tests.users;

import org.testng.Assert;
import org.testng.annotations.Test;

import helpers.GenerateUser;
import models.User;
import pageObjects.HomePage;
import tests.TestBase;

public class AddExistingUser extends TestBase {

    @Test 
    public void AddExistingUserTest() {
    	//setup

    	User testUser = new GenerateUser();
    	HomePage homePageObject = new HomePage(driver);
    	
    	 homePageObject.signUp(testUser).refreshPage();
    	 String result =  new HomePage(driver).signUp(testUser).getResults();
    	
    	Assert.assertTrue(result
    			.equalsIgnoreCase("Email already exists, please try again later."),
    			result);
    	
    	//clean up
    	homePageObject.refreshPage();
    	homePageObject.openUsers().deleteUser(testUser);
    }
}
