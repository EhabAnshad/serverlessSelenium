package helpers;


import models.User;
import utilities.KeysGenerators;

public class GenerateUser extends User
{

	public GenerateUser()
	{
		setUsername(KeysGenerators.getRadomText());
		setEmail(KeysGenerators.getRandomEmail());
	}
	
}
