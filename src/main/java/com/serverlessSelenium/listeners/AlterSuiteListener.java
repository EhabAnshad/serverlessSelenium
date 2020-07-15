package com.serverlessSelenium.listeners;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.serverlessSelenium.helpers.AmazonS3Helper;
import com.serverlessSelenium.helpers.StorageHelper;
import com.serverlessSelenium.lambda.client.TestInvoker;
import com.serverlessSelenium.lambda.model.ExecutionRequest;
import com.serverlessSelenium.util.KeysGenerators;

public class AlterSuiteListener implements IAlterSuiteListener {
	
    
    @Override
    public void alter(List<XmlSuite> suites) {
		//Get random text to use as identifier for upload/download
    	String rootFolder = KeysGenerators.getRadomText();
    	
    	uploadClasses(rootFolder);
    	
    	 for(XmlSuite suite:suites) {
    	    List<XmlClass> list =suite.getTests().get(0).getClasses();
    	    list.parallelStream().forEach(x -> {
	    	    TestInvoker invoker = new TestInvoker(new ExecutionRequest(x.getName(), rootFolder));
	    	    System.out.println(invoker.run().getTestngResult());
	    	    //TODO: Collect results, aggregate and generate final result , Save attachment if any
    	    });
    	    
    	    suite.setTests(new ArrayList<XmlTest>());
    		}
    	 
        
    }

	private void uploadClasses(String rootFolder) {
		//upload tests classes to S3
    	StorageHelper s3Helper = new AmazonS3Helper("lambda-input-selenium");
    	
    	String currentAbsolutePath = Paths.get("").toAbsolutePath().toString();
    	String packagePath = Paths.get(currentAbsolutePath,"target","test-classes").toString();
    	s3Helper.uploadFolder(packagePath,rootFolder);
    	packagePath = Paths.get(currentAbsolutePath,"target","classes", "com").toString();
    	// concat the path to avoid linux/windows issues this must match bucket expectation
    	s3Helper.uploadFolder(packagePath, rootFolder+ "/com");
	}
}