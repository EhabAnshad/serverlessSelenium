package com.serverlessSelenium.providers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public enum ApplicationProperties {
	INSTANCE;

	private final Properties properties;

	ApplicationProperties() {
		properties = new Properties();
		try (InputStream input = new FileInputStream(Paths
				.get(Paths.get("").toAbsolutePath().toString(), "src", "main", "resources", "application.properties")
				.toString())) {
			properties.load(input);

		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	public String getProperty(String Name) {
		return properties.getProperty(Name);
	}
}
