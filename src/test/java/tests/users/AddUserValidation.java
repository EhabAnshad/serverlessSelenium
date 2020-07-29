package tests.users;

import org.testng.Assert;
import org.testng.annotations.Test;

import helpers.GenerateUser;
import models.User;
import pageObjects.HomePage;
import tests.TestBase;

public class AddUserValidation extends TestBase {

	@Test
	public void AddUserValidationTest() {
		// setup
		User testUser = new GenerateUser();
		testUser.setEmail("");
		HomePage homePageObject = new HomePage(driver);

		homePageObject.signUp(testUser).refreshPage();
		String result = new HomePage(driver).signUp(testUser).getResults();

		Assert.assertTrue(
				result.equalsIgnoreCase(
						"Well this is embarrassing. It looks like we're having trouble getting you on the list."),
				result);
	}
}
