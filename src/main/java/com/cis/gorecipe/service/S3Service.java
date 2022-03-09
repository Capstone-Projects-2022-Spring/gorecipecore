package com.cis.gorecipe.service;

import java.io.IOException;
import java.io.InputStream;

public interface S3Service {

    String uploadFile(String fileName, InputStream inputStream, String contentType) throws IOException;
    boolean deleteFile(String fileName);
    String getFileUrl(String fileName);

}
