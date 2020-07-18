package com.serverlessSelenium.lambda;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverlessSelenium.helpers.AmazonS3Helper;
import com.serverlessSelenium.helpers.StorageHelper;
import com.serverlessSelenium.lambda.model.ExecutionRequest;
import com.serverlessSelenium.lambda.model.TestResult;

public class LambdaFunctionHandler implements RequestHandler<ExecutionRequest, TestResult> {
	private ClassLoader loader;
	private final String directory = "/tmp/user-data/";
	private final String bucketName;
	private final StorageHelper s3Helper;
	private final String currentAbsolutePath;
	private String classPath;
	private LambdaLogger logger;
	private static TestResult myResults ;
	
	public LambdaFunctionHandler() {
		currentAbsolutePath = Paths.get(Paths.get("").toAbsolutePath().toString(), directory).toString();
		bucketName =  "lambda-input-selenium";
		s3Helper = new AmazonS3Helper(bucketName);
		myResults = new TestResult();
	}

	public TestResult handleRequest(ExecutionRequest executionRequest/* tests.Amazon */, Context context) {
		logger = context.getLogger();
		classPath = Paths.get(currentAbsolutePath , executionRequest.getFolder()).toString();
		
		logger.log("Classname: " + executionRequest.getTestClassName());
		
		s3Helper.downloadFolder(currentAbsolutePath, executionRequest.getFolder());
		
		XmlSuite mainSuite = CreateAndLoadTest(executionRequest.getTestClassName());

		runTest(mainSuite);
		
		extractResults(executionRequest.getTestClassName());
		
		logResults(myResults);
		
		return myResults;
	}

	private void extractResults(String className) {
		try { 
			Path resultsPath = Paths.get(currentAbsolutePath,"junitreports", "TEST-"+ className + ".xml");
			myResults.setJunitResults( new String(Files.readAllBytes(resultsPath)));
			myResults.setTestngResult(new String(Files.readAllBytes(Paths.get(currentAbsolutePath, "testng-results.xml"))));
			myResults.setIndexHtml(new String(Files.readAllBytes(Paths.get(currentAbsolutePath, "index.html"))));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void runTest(XmlSuite mainSuite) {
		TestNG runner = new TestNG();
		runner.setOutputDirectory(directory);
		runner.setXmlSuites(new ArrayList<XmlSuite>(Arrays.asList(mainSuite)));
		runner.run();
	}
	
	private void logResults(TestResult result) {
		logger.log("Jnunit Result:\n " + result.getJunitResults());
		logger.log("Testng Result:\n " + result.getTestngResult());
		logger.log("Index:\n " + result.getIndexHtml());
	}

	private XmlSuite CreateAndLoadTest(String className) {
		XmlSuite mainSuite = new XmlSuite();
		mainSuite.setName("SmokeTest Suite");
		mainSuite.setFileName("SmokeTest.xml");
		mainSuite.setParallel(XmlSuite.ParallelMode.NONE);
		// mainsuite.addListener("utility.Listener");
		XmlTest mainTest = new XmlTest(mainSuite);
		mainTest.setName(className);
		mainTest.setPreserveOrder("True");


		
		File file = new File(classPath);

		try {
			URL[] classUrl = new URL[] { file.toURI().toURL() };
			loader = new URLClassLoader(classUrl,  LambdaFunctionHandler.class.getClassLoader());
			Class<?> clazz = loader.loadClass(className);
			XmlClass xmlclass = new XmlClass(clazz);
			mainTest.setXmlClasses(new ArrayList<XmlClass>(Arrays.asList(xmlclass)));
		} catch (ClassNotFoundException | MalformedURLException e) {
			e.printStackTrace();
		}
		
		
		return mainSuite;
	}

	public static void addScreenShot(String name, byte[] attachment) {
		myResults.getAttachments().put(name, attachment);
	}

	
}

