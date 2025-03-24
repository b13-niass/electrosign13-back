package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service("cloudStorageService")
public class CloudFileStorageService implements FileStorageService {

    @Value("${storage.cloud.provider}")
    private String cloudProvider;

    @Value("${storage.cloud.bucket}")
    private String cloudBucket;

    @Override
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        return "Fichier uploadé sur " + cloudProvider + " dans le bucket: " + cloudBucket;
    }
}
