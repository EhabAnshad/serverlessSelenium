package utilities;
import java.util.Random;
import java.util.UUID;

/*
 * Create a singletone generator and add more random generators to generate different data
 * 
 */
public class KeysGenerators 
{
	private String key;
	private static KeysGenerators instance = null;


	private KeysGenerators() {
		String word = UUID.randomUUID().toString();
		key = word.substring(0, 8);
	}

	public static KeysGenerators getInstance() {
		if (instance == null) {
			instance = new KeysGenerators();
		}
		return instance;
	}

	public String getPartialKey() {
		return key;
	}
	
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
	
	public static String getRandomEmail()
	{
		//TODO: Change this hard coded value to get the value form base.
		StringBuilder mail = new StringBuilder();
		mail.append("m+")
		.append(getRadomText(4))
		.append("@gmail.com");
		
		return mail.toString();
		
	}
	
}


