package com.serverlessSelenium.lambda.logger;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class MockLambdaConsoleLogger implements LambdaLogger {

    @Override
    public void log(String string) {
        System.out.println(string);
    }

	@Override
	public void log(byte[] arg0) {
		// TODO Auto-generated method stub
		
	}
}
