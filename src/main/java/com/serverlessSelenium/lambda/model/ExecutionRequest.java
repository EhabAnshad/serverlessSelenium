package com.serverlessSelenium.lambda.model;

public class ExecutionRequest {

    private String testClassName;
    private String folder;

    //mandatory to avoid json mapping exception
    public ExecutionRequest() {}
    
    public ExecutionRequest(String testClassName, String folder ) {
        this.testClassName = testClassName;
        this.folder = folder;
    }


    public String getTestClassName() {
        return testClassName;
    }

    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

}
