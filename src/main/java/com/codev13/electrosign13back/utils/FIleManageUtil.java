package com.codev13.electrosign13back.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FIleManageUtil {
    @Value("${storage.local.path}")
    private String localPath;

    public static boolean deleteFile(String fileName) {
        File file = new File(Paths.get("src/main/resources/uploads/", fileName).toAbsolutePath().toString());
        // Check if the file exists
        if (file.exists()) {
            return file.delete();
        } else {
            System.out.println("File not found: " + fileName);
            return false;
        }
    }
}
