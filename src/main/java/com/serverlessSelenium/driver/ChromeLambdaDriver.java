package com.serverlessSelenium.driver;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

public class ChromeLambdaDriver implements LambdaDriver {
	final static String HEADLESS_CHROME_PATH = "/opt/bin/headless-chromium";
	final static String CHROMEDRIVER_PATH = "/opt/bin/chromedriver";
	
	public WebDriver createSession() {
		return new ChromeDriver(getOptions());
	}
	
	private ChromeOptions getOptions() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless", 
                "--window-size=1280x1696",
                "--no-sandbox",
                "--user-data-dir=/tmp/user-data",
                "--hide-scrollbars",
                "--enable-logging",
                "--log-level=0",
                "--v=99",
                "--single-process",
                "--data-path=/tmp/data-path",
                "--ignore-certificate-error",
                "--homedir=/tmp",
                "--disk-cache-dir=/tmp/cache-dir");
		options.setBinary(HEADLESS_CHROME_PATH);
		
		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
		
		Map<String, Object> perfLoggingPrefs = new HashMap<String, Object>();
		perfLoggingPrefs.put("enableNetwork", true);
		perfLoggingPrefs.put("enablePage", true);
		
		options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
		options.setExperimentalOption("perfLoggingPrefs", perfLoggingPrefs);
		
		return options;
	}

}
