package com.amazonaws.lambda.tests;


import static com.serverlessSelenium.lambda.logger.LoggerContainer.LOGGER;

import java.util.Collection;

import org.junit.Test;
import org.junit.experimental.categories.Categories.CategoryFilter;
import org.junit.runners.Parameterized.Parameters;

import com.serverlessSelenium.lambda.LambdaTestSuite;
import com.serverlessSelenium.lambda.TestInvoker;
import com.serverlessSelenium.lambda.TestRequest;
import com.serverlessSelenium.lambda.TestResult;


public class ExampleTestSuite extends LambdaTestSuite {

    private static final CategoryFilter filter = CategoryFilter.include(Test.class);
    private TestRequest testRequest;

    public ExampleTestSuite(TestRequest testRequest) {
        this.testRequest = testRequest;
    }

    @Parameters(name = "{0}")
    public static Collection<TestRequest> testRequests() {
    	LOGGER.log("Running " + filter.describe());

        return getTestRequests("com.amazonaws.lambda.tests", filter);
    }

    public void runTest() {
        TestInvoker testInvoker = new TestInvoker(testRequest);
        TestResult testResult = testInvoker.run();
        writeAttachments(testResult.getAttachments());
        logTestResult(testRequest, testResult);
    }
}
