package com.serverlessSelenium.lambda.client;

import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.serverlessSelenium.lambda.model.ExecutionRequest;
import com.serverlessSelenium.lambda.model.TestResult;

public class TestInvoker {

	private final ExecutionRequest testExecutionRequest;

	public TestInvoker(final ExecutionRequest testExecutionRequest) {
		this.testExecutionRequest = testExecutionRequest;
	}

	public TestResult run() {

		final LambdaSeleniumService lambdaService = LambdaInvokerFactory.builder()
				.lambdaClient(AWSLambdaClientBuilder.defaultClient())
				.build(LambdaSeleniumService.class);

		return lambdaService.runTest(testExecutionRequest);
	}
}