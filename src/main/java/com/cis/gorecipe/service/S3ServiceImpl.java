package com.cis.gorecipe.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.IOException;
import java.io.InputStream;

/**
 * This service manages all interactions with GoRecipe's S3 bucket
 */
@Service
public class S3ServiceImpl implements S3Service {

    /**
     * @param fileName    the name of the image file being uploaded
     * @param inputStream the file being uploaded as a bytestream
     * @param contentType the MIME type of the file
     * @return the URL of the file that has been uploaded to S3
     * @throws IOException
     */
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

    /**
     * @param fileName the name of the file to be deleted from the S3 bucket
     * @return whether the file was successfully deleted
     */
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

    /**
     * @param fileName the name of the file to be located
     * @return the URL of the file in the S3 bucket
     */
    @Override
    public String getFileUrl(String fileName) {

        return client.utilities().getUrl(builder ->
                builder.bucket(BUCKET).key(fileName)).toExternalForm();
    }
}
