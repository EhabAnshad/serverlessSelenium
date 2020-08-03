package com.serverlessSelenium.listeners;

import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.serverlessSelenium.providers.ApplicationProperties;
import com.serverlessSelenium.util.KeysGenerators;

public class AlterSuiteListener implements IAlterSuiteListener {
	private StorageHelper s3Helper;
    
    @Override
    public void alter(List<XmlSuite> suites) {
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    	LocalDateTime dateBefore = LocalDateTime.now();
    	System.out.println(dtf.format(dateBefore)); 
    	
    	
		//Get random text to use as identifier for upload/download
    	String rootFolder = KeysGenerators.getRadomText();

    	ResultsHandler results = new ResultsHandler();
    	uploadClasses(rootFolder);

    	 for(XmlSuite suite:suites) {
    	    List<XmlClass> list =suite.getTests().get(0).getClasses();
    	    list.parallelStream().forEach(x -> {
	    	    TestInvoker invoker = new TestInvoker(new ExecutionRequest(x.getName(), rootFolder));
	    	    results.addResult(invoker.run());
    	    });
    	    
       	 	s3Helper.deleteFolder(rootFolder);
    	    results.aggregateAndReport();
    	    
    	    suite.setTests(new ArrayList<XmlTest>());
    		}
    	 
    	 s3Helper.shutdown();
    	 //clean up
    	 
    	LocalDateTime dateAfter = LocalDateTime.now();
     	System.out.println(dtf.format(dateAfter)); 
     	Duration duration = Duration.between(dateAfter, dateBefore);
     	System.out.println("Duration: " + Math.abs(duration.toSeconds())); 
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