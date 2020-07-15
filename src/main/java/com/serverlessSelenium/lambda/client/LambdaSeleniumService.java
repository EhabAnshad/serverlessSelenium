package com.serverlessSelenium.lambda.client;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.serverlessSelenium.lambda.model.ExecutionRequest;
import com.serverlessSelenium.lambda.model.TestResult;

public interface LambdaSeleniumService {
    @LambdaFunction(functionName = "MyFunction")
    TestResult runTest(ExecutionRequest testExecutionRequest);
}
