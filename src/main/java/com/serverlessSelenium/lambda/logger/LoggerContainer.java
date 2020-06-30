package com.serverlessSelenium.lambda.logger;

public class LoggerContainer {

    public static Logger LOGGER = new Logger(new MockLambdaConsoleLogger());
}
