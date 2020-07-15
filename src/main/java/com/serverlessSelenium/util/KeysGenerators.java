package com.serverlessSelenium.util;
import java.util.Random;
import java.util.UUID;

/*
 * Create a singletone generator and add more random generators to generate different data
 * 
 */
public class KeysGenerators 
{
	
	public static String getRandomNumber()
	{
		Random radomzier = new Random();
		String theKey = String.valueOf(radomzier.nextInt(1000000));
		return theKey;
	}
	
	public static String getRadomText()
	{
		String word = UUID.randomUUID().toString();
		return word.substring(0, 8);
	}
	
	public static String getRadomText(int length)
	{
		String word = UUID.randomUUID().toString();
		return word.substring(0, length);
	}

	
}


