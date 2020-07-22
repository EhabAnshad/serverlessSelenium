package com.serverlessSelenium.helpers;

import org.testng.xml.XmlSuite;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.serverlessSelenium.lambda.model.TestResult;

public interface UnitTestFrameworkHelper {

	 XmlSuite CreateAndLoadTest(final String className, final String classPath);
	 void runTest(XmlSuite mainSuite, String directory);
	 void extractResults(String className, String currentAbsolutePath, TestResult myResults);
	 void logResults(LambdaLogger logger, TestResult result);
}