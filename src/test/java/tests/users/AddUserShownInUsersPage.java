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
    	HomePage homePageObject = new HomePage(driver).signUp(testUser);

    	UsersPage userPage =  homePageObject.openUsers();
    	userPage.refreshPage();
    	Assert.assertTrue(userPage.doesUserExists(testUser), testUser.getEmail() + " doesn't exists");
    	
    	//clean up
    	userPage.deleteUser(testUser);
    }
}
