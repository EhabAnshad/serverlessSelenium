package tests.users;

import org.testng.Assert;
import org.testng.annotations.Test;

import helpers.GenerateUser;
import models.User;
import pageObjects.HomePage;
import tests.TestBase;

public class AddUserSuccessfully extends TestBase {

    @Test 
    public void AddUserTest() {
    	//setup
    	User testUser = new GenerateUser();
    	HomePage homePageObject = new HomePage(driver);
    	
    	String result =  homePageObject.signUp(testUser).getResults();
    	
    	Assert.assertTrue(result
    			.equalsIgnoreCase("Thanks for signing up! You'll be among the first to know when we launch."),
    			result);
    	
    	//clean up
    	homePageObject.refreshPage();
    	homePageObject.openUsers().deleteUser(testUser);
    }
}
