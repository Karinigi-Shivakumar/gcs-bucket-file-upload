package com.myapp;

import com.google.cloud.storage.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class GCSFileUploader {
    private static final String BUCKET_NAME = "ps-service"; // Replace with your GCS bucket name
    private final Storage storage;

    public GCSFileUploader() {
        this.storage = StorageOptions.getDefaultInstance().getService(); // Uses service account
    }

    // Upload file
    public void uploadFile(String localFilePath, String objectName) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(localFilePath)));
        System.out.println("✅ File uploaded to GCS: " + objectName);
    }

    // Download file
    public void downloadFile(String objectName, String destinationPath) throws IOException {
        Blob blob = storage.get(BlobId.of(BUCKET_NAME, objectName));
        if (blob == null) {
            System.out.println("❌ File not found in GCS!");
            return;
        }
        blob.downloadTo(Paths.get(destinationPath));
        System.out.println("✅ File downloaded: " + destinationPath);
    }

    public static void main(String[] args) {
        try {
            GCSFileUploader uploader = new GCSFileUploader();
            String localFile = "testfile.txt"; // File to upload
            String objectName = "uploads/testfile.txt"; // Object name in GCS

            uploader.uploadFile(localFile, objectName);
            uploader.downloadFile(objectName, "downloaded_testfile.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

