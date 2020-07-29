package helpers;


import models.Product;
import utilities.KeysGenerators;

public class GenerateProduct extends Product
{

	public GenerateProduct()
	{
		setProductName(KeysGenerators.getRadomText());
		setPrice(KeysGenerators.getRandomNumber());
	}
	
}
