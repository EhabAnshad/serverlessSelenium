package tests.users;

import org.testng.Assert;
import org.testng.annotations.Test;

import helpers.GenerateUser;
import models.User;
import pageObjects.HomePage;
import pageObjects.UsersPage;
import tests.TestBase;

public class EditUserTest extends TestBase {
	
    @Test 
    public void EditUserTests() {
    	//setup
    	User testUser = new GenerateUser();
    	HomePage homePageObject = new HomePage(driver);
    	String result =  homePageObject.signUp(testUser).getResults();
    	Assert.assertTrue(result
    			.equalsIgnoreCase("Thanks for signing up! You'll be among the first to know when we launch."),
    			result);
    	
    	homePageObject.refreshPage();
    	UsersPage userPage =  homePageObject.openUsers();
    	User updateTestUser = new GenerateUser();
    	updateTestUser.setEmail(testUser.getEmail());

    	userPage.editUser(testUser, updateTestUser).refreshPage();
    	String updatedUserName =  new UsersPage(driver).getUserName(updateTestUser);
    	Assert.assertEquals(updatedUserName,  updateTestUser.getUsername());
    	
    	userPage.deleteUser(testUser);
    }

}
