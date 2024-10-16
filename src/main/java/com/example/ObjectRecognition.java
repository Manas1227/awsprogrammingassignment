package com.example;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.ArrayList;
import java.util.List;

public class ObjectRecognition {

    public static void main(String[] args) {
        String bucketName = "njit-cs-643";  // Your S3 bucket name
        Region region = Region.US_EAST_1;

        RekognitionClient rekClient = RekognitionClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        // List all images in the S3 bucket and perform object detection on each
        List<String> imageKeys = listImageKeys(s3Client, bucketName);
        for (String imageKey : imageKeys) {
            System.out.println("Processing image: " + imageKey);
            detectObjects(rekClient, bucketName, imageKey);
        }

        rekClient.close();
        s3Client.close();
    }

    // Method to list all object keys (file names) in the S3 bucket
    public static List<String> listImageKeys(S3Client s3Client, String bucketName) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)  // Set the S3 bucket name
                .build();

        ListObjectsV2Response result = s3Client.listObjectsV2(request);
        List<S3Object> objects = result.contents();

        // Extract image keys from S3Object and store them in a list
        List<String> imageKeys = new ArrayList<>();
        for (S3Object s3Object : objects) {
            imageKeys.add(s3Object.key());
        }
        return imageKeys;
    }

    public static void detectObjects(RekognitionClient rekClient, String bucketName, String imageKey) {
        try {
            // Build the S3 object reference for Rekognition
            Image image = Image.builder()
                    .s3Object(software.amazon.awssdk.services.rekognition.model.S3Object.builder()
                            .bucket(bucketName)  // Set the S3 bucket name here
                            .name(imageKey)  // Image key (file name)
                            .build())
                    .build();

            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                    .image(image)
                    .maxLabels(10)
                    .minConfidence(90.0f)  // Only detect objects with confidence > 90%
                    .build();

            DetectLabelsResponse detectLabelsResult = rekClient.detectLabels(detectLabelsRequest);
            List<Label> labels = detectLabelsResult.labels();

            // Process and display detected labels (objects)
            for (Label label : labels) {
                System.out.println("Detected label: " + label.name() + " (Confidence: " + label.confidence() + ")");
                // Check if the label is "Car" and confidence > 90%
                if (label.name().equalsIgnoreCase("Car") && label.confidence() > 90.0) {
                    System.out.println("Car detected in image: " + imageKey + " with confidence: " + label.confidence());
                    // Add the image index to SQS if a car is detected
                    // For example: sendToSqs(imageKey);
                }
            }
        } catch (RekognitionException e) {
            System.err.println(e.getMessage());
        }
    }
}
