package com.serverlessSelenium.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.serverlessSelenium.lambda.model.TestResult;

public class ResultsHandler {

	private List<TestResult> results;

	public ResultsHandler() {
		results = new ArrayList<TestResult>();
	}

	public void addResult(TestResult result) {
		results.add(result);
	}

	public void aggregateAndReport() {

		for (TestResult result : results) {
			//Aggregate reports
			//TODO: aggregate instead of log
			System.out.println(result.getTestngResult());
			
			// save attachments
			if (!result.getAttachments().isEmpty()) {
				writeAttachments(result.getAttachments());
			}
		}

		
		//Write reports
		
	}

	private void writeAttachments(Map<String, byte[]> attachments) {
		File outputDirectory = new File(System.getProperty("user.dir") + "/build/screenshots/");
		outputDirectory.mkdirs();

		attachments.forEach((fileName, bytes) -> {
			try {
				FileUtils.writeByteArrayToFile(new File(outputDirectory, fileName), bytes);
			} catch (IOException e) {
				System.out.println(e);
			}
		});
	}

}
