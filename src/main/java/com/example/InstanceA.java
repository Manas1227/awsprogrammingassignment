package com.example;

// import software.amazon.awssdk.services.s3.S3Client;
// import software.amazon.awssdk.services.sqs.SqsClient;
// import software.amazon.awssdk.services.rekognition.RekognitionClient;
// import software.amazon.awssdk.regions.Region;

// public class InstanceA {
//     public static void main(String[] args) {
//         // Set up AWS clients
//         // S3Client s3Client = S3Client.builder().region(Region.US_EAST_1).build();
//         // SqsClient sqsClient = SqsClient.builder().region(Region.US_EAST_1).build();
//         // RekognitionClient rekognitionClient = RekognitionClient.builder().region(Region.US_EAST_1).build();

//         // Add your logic here for image processing and SQS
//         System.out.println("Instance A is ready.");
//     }
// }

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.util.List;

public class InstanceA {

    public static void main(String[] args) {
        // Initialize AWS clients for Rekognition, SQS, and S3
        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();
        AmazonSQS sqsClient = AmazonSQSClientBuilder.defaultClient();
        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();

        String bucketName = "njit-cs-643";
        String queueUrl = "<SQS-Queue-URL>";
        int imageCount = 10;

        for (int i = 1; i <= imageCount; i++) {
            String imageKey = i + ".jpg";

            // Perform object detection on the image using Rekognition
            DetectLabelsRequest request = new DetectLabelsRequest()
                    .withImage(new Image().withS3Object(new S3Object().withBucket(bucketName).withName(imageKey)))
                    .withMinConfidence(90f);

            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List<Label> labels = result.getLabels();

            // Check if 'Car' label is detected with confidence > 90%
            for (Label label : labels) {
                if (label.getName().equals("Car") && label.getConfidence() > 90) {
                    // Send the image index to SQS
                    SendMessageRequest sendMsgRequest = new SendMessageRequest()
                            .withQueueUrl(queueUrl)
                            .withMessageBody(imageKey);
                    sqsClient.sendMessage(sendMsgRequest);
                    System.out.println("Car detected in " + imageKey + ", sending to SQS.");
                    break;
                }
            }
        }

        // Signal end of processing by sending "-1" to the queue
        sqsClient.sendMessage(new SendMessageRequest(queueUrl, "-1"));
    }
}
