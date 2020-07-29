package tests.users;

import org.testng.Assert;
import org.testng.annotations.Test;

import helpers.GenerateUser;
import models.User;
import pageObjects.HomePage;
import pageObjects.UsersPage;
import tests.TestBase;

public class DeleteUserTest extends TestBase {

    @Test 
    public void VerifyUserExistsAfterDeletion() {
    	//setup
    	User testUser = new GenerateUser();
    	HomePage homePageObject = new HomePage(driver);
    	String result =  homePageObject.signUp(testUser).getResults();
    	Assert.assertTrue(result
    			.equalsIgnoreCase("Thanks for signing up! You'll be among the first to know when we launch."),
    			result);
    	
    	homePageObject.refreshPage();
    	UsersPage userPage =  homePageObject.openUsers();
    	userPage.deleteUser(testUser).refreshPage();
    	
    	boolean userExists = new UsersPage(driver).doesUserExists(testUser);
    	Assert.assertFalse(userExists, "User is not deleted" + testUser.getEmail());
    	
    	
    }
}
