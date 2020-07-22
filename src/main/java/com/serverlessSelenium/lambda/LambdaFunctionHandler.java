package com.serverlessSelenium.lambda;

import java.nio.file.Paths;

import org.testng.xml.XmlSuite;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverlessSelenium.helpers.AmazonS3Helper;
import com.serverlessSelenium.helpers.StorageHelper;
import com.serverlessSelenium.helpers.TestNgHelper;
import com.serverlessSelenium.helpers.UnitTestFrameworkHelper;
import com.serverlessSelenium.lambda.model.ExecutionRequest;
import com.serverlessSelenium.lambda.model.TestResult;

public class LambdaFunctionHandler implements RequestHandler<ExecutionRequest, TestResult> {

	private final String directory = "/tmp/user-data/";
	private final String bucketName;
	private final StorageHelper s3Helper;
	private final String currentAbsolutePath;
	private String classPath;
	private LambdaLogger logger;
	private static TestResult myResults ;
	private UnitTestFrameworkHelper testNgHelper;
	
	public LambdaFunctionHandler() {
		currentAbsolutePath = Paths.get(Paths.get("").toAbsolutePath().toString(), directory).toString();
		bucketName =  "lambda-input-selenium";
		s3Helper = new AmazonS3Helper(bucketName);
		testNgHelper = new TestNgHelper();
		myResults = new TestResult();
	}

	public TestResult handleRequest(ExecutionRequest executionRequest/* tests.Amazon */, Context context) {
		logger = context.getLogger();
		classPath = Paths.get(currentAbsolutePath , executionRequest.getFolder()).toString();
		
		logger.log("Classname: " + executionRequest.getTestClassName());
		
		s3Helper.downloadFolder(currentAbsolutePath, executionRequest.getFolder());
		
		XmlSuite mainSuite = testNgHelper.CreateAndLoadTest(executionRequest.getTestClassName(), classPath);

		testNgHelper.runTest(mainSuite, directory);
		
		testNgHelper.extractResults(executionRequest.getTestClassName(), currentAbsolutePath,  myResults);
		
		testNgHelper.logResults(logger, myResults);
		
		return myResults;
	}

	
	public static void addScreenShot(String name, byte[] attachment) {
		myResults.getAttachments().put(name, attachment);
	}

	
}

