package com.serverlessSelenium.helpers;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;

public class AmazonS3Helper implements StorageHelper {
	private final String bucketName;
	private final TransferManager transferManager;

	public AmazonS3Helper(final String bucketName) {
		this.bucketName = bucketName;
		transferManager = TransferManagerBuilder.standard().build();
	}

	@Override
	public void uploadFolder(String path, String dic) {
		MultipleFileUpload xfer = transferManager.uploadDirectory(bucketName, dic, new File(path),
				true /* recursive */);
		try {
			xfer.waitForCompletion();
		} catch (AmazonClientException | InterruptedException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	@Override
	public void downloadFolder(String path, String folder) {
		MultipleFileDownload download = transferManager.downloadDirectory(bucketName, folder, new File(path));
		try {
			download.waitForCompletion();
		} catch (AmazonClientException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void downloadFolder(String path) {
		downloadFolder(path, "");
	}

}
