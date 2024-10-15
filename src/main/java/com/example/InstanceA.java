package com.example;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.nio.file.Paths;

public class FetchImagesFromS3 {
    public static void main(String[] args) {
        String bucketName = "njit-cs-643";  // S3 bucket name
        String keyPrefix = "";              // Path in bucket (if images are in root, leave blank)
        String downloadDir = "C:/Users/acer/Downloads";  // Local path to download the images

        // Create an S3 client
        S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1)   // Your bucket region
                .credentialsProvider(ProfileCredentialsProvider.create())  // Credentials profile in ~/.aws/credentials
                .build();

        // List and download images from S3
        try {
            // List objects in the S3 bucket
            ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(keyPrefix)      // List objects with this prefix (optional)
                    .build();

            ListObjectsV2Response listRes = s3.listObjectsV2(listReq);
            for (S3Object s3Object : listRes.contents()) {
                String key = s3Object.key();
                System.out.println("Downloading: " + key);

                // Download the object (image) to the local file system
                GetObjectRequest getReq = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

                s3.getObject(getReq, Paths.get(downloadDir + File.separator + key));
            }
        } catch (S3Exception e) {
            System.err.println(e.getMessage());
        }
    }
}