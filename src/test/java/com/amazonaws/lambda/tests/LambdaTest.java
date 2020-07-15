package com.amazonaws.lambda.tests;



import org.testng.Assert;
import org.testng.annotations.Test;

import com.serverlessSelenium.LambdaBaseTest;

@Test
public class LambdaTest extends LambdaBaseTest {

    @Test
    public void googleTest() {
    	driver.navigate().to("https://www.google.com");
        //screenshot("google-home-page");
        Assert.assertTrue(driver.getTitle().contains("Google"));
    }

}
