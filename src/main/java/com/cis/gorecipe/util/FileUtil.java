package com.cis.gorecipe.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class FileUtil {

    public static boolean isImage(MultipartFile file) {

        String type = file.getContentType();
        type = Objects.requireNonNull(type).split("/")[0];
        return type.equals("image");
    }
}
