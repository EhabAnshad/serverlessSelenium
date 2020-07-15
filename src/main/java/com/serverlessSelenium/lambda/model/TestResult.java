package com.serverlessSelenium.lambda.model;

import java.util.HashMap;
import java.util.Map;

public class TestResult  {

    private String indexHtml;
    private String testngResult;
    private String junitResults;

    private Map<String, byte[]> attachments = new HashMap<>();

    public TestResult() {}


    public String getIndexHtml() {
        return indexHtml;
    }

    public void setIndexHtml(String indexHtml) {
        this.indexHtml = indexHtml;
    }

    public String getTestngResult() {
        return testngResult;
    }

    public void setTestngResult(String testngResult) {
        this.testngResult = testngResult;
    }
    
    public String getJunitResults() {
        return junitResults;
    }

    public void setJunitResults(String junitResults) {
        this.junitResults = junitResults;
    }


    public Map<String, byte[]> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, byte[]> attachments) {
        this.attachments = attachments;
    }

}
