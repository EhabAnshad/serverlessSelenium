package com.serverlessSelenium.helpers;

public interface StorageHelper {

	void uploadFolder(String path, String dic);
	
	void downloadFolder(String path, String folder) ;
	
	void downloadFolder(String path);
	
	void deleteFolder(String path);
	
}