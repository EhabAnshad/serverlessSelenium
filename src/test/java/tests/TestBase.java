package tests;
import org.testng.annotations.BeforeMethod;

import com.serverlessSelenium.LambdaBaseTest;



public class TestBase extends LambdaBaseTest
{

	@BeforeMethod
	public void BeforeTest() 
	{
		driver.navigate().to("http://testenv-env.eba-gxcq2r6b.eu-west-1.elasticbeanstalk.com/");
	}


}