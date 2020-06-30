package com.serverlessSelenium.lambda;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.serverlessSelenium.lambda.TestResult;

public interface LambdaSeleniumService {
    @LambdaFunction(functionName = "MyFunction")
    TestResult runTest(TestRequest testRequest);
}
