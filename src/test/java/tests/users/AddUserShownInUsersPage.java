package tests.users;

import org.testng.Assert;
import org.testng.annotations.Test;

import helpers.GenerateUser;
import models.User;
import pageObjects.HomePage;
import pageObjects.UsersPage;
import tests.TestBase;

public class AddUserShownInUsersPage extends TestBase {

    @Test 
    public void VerifyUserExists() {
    	//setup
    	User testUser = new GenerateUser();
    	HomePage homePageObject = new HomePage(driver);
    	String result =  homePageObject.signUp(testUser).getResults();
    	Assert.assertTrue(result
    			.equalsIgnoreCase("Thanks for signing up! You'll be among the first to know when we launch."),
    			result);
    	
    	UsersPage userPage =  homePageObject.openUsers();
    	userPage.refreshPage();
    	Assert.assertTrue(userPage.doesUserExists(testUser), testUser.getEmail() + " doesn't exists");
    	
    	//clean up
    	userPage.deleteUser(testUser);
    }
}
