package com.cis.gorecipe.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * A utility class for handling file processing
 */
public class FileUtil {

    /**
     * @param file a file that has been uploaded by the user
     * @return whether the uploaded file is an image (jpeg, png, bmp, etc.)
     */
    public static boolean isImage(MultipartFile file) {

        String type = file.getContentType();
        type = Objects.requireNonNull(type).split("/")[0];
        return !type.equals("image");
    }
}
