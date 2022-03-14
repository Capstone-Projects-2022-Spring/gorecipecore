package com.cis.gorecipe.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.IOException;
import java.io.InputStream;

@Service
public class S3ServiceImpl implements S3Service {

    private static final String BUCKET = "gorecipe-foodimage-uploads";

    private static final S3Client client = S3Client.builder()
            .region(Region.of("us-east-2"))
            .build();

    @Override
    public String uploadFile(String fileName, InputStream inputStream, String contentType) throws IOException {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(fileName)
                .contentType(contentType)
                .build();

        client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));

        // wait until image has been uploaded
        S3Waiter waiter = client.waiter();
        HeadObjectRequest waitRequest = HeadObjectRequest.builder()
                .bucket(BUCKET)
                .key(fileName)
                .build();

        WaiterResponse<HeadObjectResponse> waitResponse = waiter.waitUntilObjectExists(waitRequest);

        if (waitResponse.matched().response().isEmpty())
            return null;

        return getFileUrl(fileName);
    }

    @Override
    public boolean deleteFile(String fileName) {

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(BUCKET)
                .key(fileName)
                .build();

        client.deleteObject(request);

        S3Waiter waiter = client.waiter();
        HeadObjectRequest waitRequest = HeadObjectRequest.builder()
                .bucket(BUCKET)
                .key(fileName)
                .build();

        WaiterResponse<HeadObjectResponse> waitResponse = waiter.waitUntilObjectNotExists(waitRequest);

        return waitResponse.matched().response().isEmpty();
    }

    @Override
    public String getFileUrl(String fileName) {

        return client.utilities().getUrl(builder ->
                builder.bucket(BUCKET).key(fileName)).toExternalForm();
    }
}
