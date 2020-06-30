package com.amazonaws.lambda.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.serverlessSelenium.LambdaBaseTest;


@Category(Test.class)
public class LambdaTest extends LambdaBaseTest {

    @Test
    public void googleTest() {
    	driver.navigate().to("https://www.google.com");
        screenshot("google-home-page");
        assertEquals(driver.getTitle(), containsString("Google"));
    }

}
