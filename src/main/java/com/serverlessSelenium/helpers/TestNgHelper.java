package com.serverlessSelenium.helpers;

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

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.serverlessSelenium.lambda.LambdaFunctionHandler;
import com.serverlessSelenium.lambda.model.TestResult;

public class TestNgHelper implements UnitTestFrameworkHelper {

	public XmlSuite CreateAndLoadTest(final String className, final String classPath) {
		XmlSuite mainSuite = new XmlSuite();
		mainSuite.setName("SmokeTest Suite");
		mainSuite.setFileName("SmokeTest.xml");
		mainSuite.setParallel(XmlSuite.ParallelMode.NONE);
		// mainsuite.addListener("utility.Listener");
		XmlTest mainTest = new XmlTest(mainSuite);
		mainTest.setName(className);
		mainTest.setPreserveOrder("True");


		
		File file = new File(classPath);
		ClassLoader loader;
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
	
	public void runTest(XmlSuite mainSuite, String directory) {
		TestNG runner = new TestNG();
		runner.setOutputDirectory(directory);
		runner.setXmlSuites(new ArrayList<XmlSuite>(Arrays.asList(mainSuite)));
		runner.run();
	}
	
	public void extractResults(String className, String currentAbsolutePath, TestResult myResults) {
		try { 
			Path resultsPath = Paths.get(currentAbsolutePath,"junitreports", "TEST-"+ className + ".xml");
			myResults.setJunitResults( new String(Files.readAllBytes(resultsPath)));
			myResults.setTestngResult(new String(Files.readAllBytes(Paths.get(currentAbsolutePath, "testng-results.xml"))));
			myResults.setIndexHtml(new String(Files.readAllBytes(Paths.get(currentAbsolutePath, "index.html"))));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void logResults(LambdaLogger logger, TestResult result) {
		logger.log("Jnunit Result:\n " + result.getJunitResults());
		logger.log("Testng Result:\n " + result.getTestngResult());
		logger.log("Index:\n " + result.getIndexHtml());
	}

}
