package com.serverlessSelenium.lambda;

import static com.serverlessSelenium.lambda.logger.LoggerContainer.LOGGER;
import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverlessSelenium.lambda.logger.Logger;
import com.serverlessSelenium.lambda.logger.LoggerContainer;

public class LambdaFunctionHandler implements RequestHandler<TestRequest, TestResult> {

	private static TestResult testResult;

	public LambdaFunctionHandler() {
		testResult = new TestResult();
	}

	public TestResult handleRequest(TestRequest testRequest, Context context) {
		LoggerContainer.LOGGER = new Logger(context.getLogger());
		System.setProperty("target.test.uuid", testRequest.getTestRunUUID());

		Optional<Result> result = Optional.empty();
		try {
			BlockJUnit4ClassRunner runner = new BlockJUnit4ClassRunner(getTestClass(testRequest));
			runner.filter(new MethodFilter(testRequest.getFrameworkMethod()));

			result = ofNullable(new JUnitCore().run(runner));
		} catch (Exception e) {
			testResult.setThrowable(e);
			LOGGER.log(e);
		}

		if (result.isPresent()) {
			testResult.setRunCount(result.get().getRunCount());
			testResult.setRunTime(result.get().getRunTime());
			LOGGER.log("Run count: " + result.get().getRunCount());
			result.get().getFailures().forEach(failure -> {
				LOGGER.log(failure.getException());
				testResult.setThrowable(failure.getException());
			});
		}

		return testResult;
	}

	private Class getTestClass(TestRequest testRequest) throws Exception {
		LOGGER.log("Running Test: %s::%s", testRequest.getTestClass(), testRequest.getFrameworkMethod());
		try {
			LOGGER.log(testRequest.getTestClass());
			LOGGER.log(testRequest.getFrameworkMethod());
			return Class.forName(testRequest.getTestClass());
		} catch (Exception e) {
			LOGGER.log(e);
			 throw e;
			 }
	}

	public static void addAttachment(String fileName, byte[] attachment) {
		testResult.getAttachments().put(fileName, attachment);
	}
}