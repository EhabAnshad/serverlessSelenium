package com.serverlessSelenium.listeners;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.serverlessSelenium.helpers.AmazonS3Helper;
import com.serverlessSelenium.helpers.ResultsHandler;
import com.serverlessSelenium.helpers.StorageHelper;
import com.serverlessSelenium.lambda.client.TestInvoker;
import com.serverlessSelenium.lambda.model.ExecutionRequest;
import com.serverlessSelenium.lambda.model.TestResult;
import com.serverlessSelenium.providers.ApplicationProperties;
import com.serverlessSelenium.util.KeysGenerators;

public class AlterSuiteListener implements IAlterSuiteListener {
	private StorageHelper s3Helper;
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
    @Override
    public void alter(List<XmlSuite> suites) {
    	logTimeNow();
    	
		//Get random text to use as identifier for upload/download
    	String rootFolder = KeysGenerators.getRadomText();

    	ResultsHandler results = new ResultsHandler();
    	uploadClasses(rootFolder);

    	 for(XmlSuite suite:suites) {
    	    List<XmlClass> list =suite.getTests().get(0).getClasses();
    	    list.parallelStream().forEach(x -> {
	    	    TestInvoker invoker = new TestInvoker(new ExecutionRequest(x.getName(), rootFolder));
	        	logTimeNow("Before:" + x.getName());
	        	TestResult myresult  = invoker.run();
	    	    logTimeNow("After:" + myresult.getName());
	    	    results.addResult(myresult);
    	    });
    	    suite.setTests(new ArrayList<XmlTest>());
    		}
 	    
 	    new Thread(new Runnable() {
 	        public void run() {
 	         s3Helper.deleteFolder(rootFolder);
 	       	 s3Helper.shutdown();
 	        }
 	    }).start();
 	    
    	 	
 	    results.aggregateAndReport();
    	 
    	logTimeNow();
    }

	private void logTimeNow() {
		logTimeNow("");
	}
	
	private void logTimeNow(String value) {
    	LocalDateTime dateBefore = LocalDateTime.now();
    	System.out.println(value + dtf.format(dateBefore));
	}

	private void uploadClasses(String rootFolder) {
		//upload tests classes to S3
		String bucketName = ApplicationProperties.INSTANCE.getProperty("BucketName");
		s3Helper = new AmazonS3Helper(bucketName);
    	
    	String currentAbsolutePath = Paths.get("").toAbsolutePath().toString();
    	String packagePath = Paths.get(currentAbsolutePath,"target","test-classes").toString();
    	s3Helper.uploadFolder(packagePath,rootFolder);
    	packagePath = Paths.get(currentAbsolutePath,"target","classes", "com").toString();
    	// concat the path to avoid linux/windows issues this must match bucket expectation
    	s3Helper.uploadFolder(packagePath, rootFolder+ "/com");
	}
}