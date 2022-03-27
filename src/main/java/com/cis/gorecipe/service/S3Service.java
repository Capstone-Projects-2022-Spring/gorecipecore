package com.cis.gorecipe.service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for S3ServiceImpl
 */
public interface S3Service {

    /**
     * The AWS S3 bucket ID
     */
    String BUCKET = "gorecipe-foodimage-uploads";

    /**
     * The client object for interfacing with AWS
     */
    S3Client client = S3Client.builder()
            .region(Region.of("us-east-2"))
            .build();

    String uploadFile(String fileName, InputStream inputStream, String contentType) throws IOException;

    boolean deleteFile(String fileName);

    String getFileUrl(String fileName);

}
