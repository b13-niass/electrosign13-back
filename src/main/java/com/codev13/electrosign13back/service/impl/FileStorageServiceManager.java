package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.service.FileStorageService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component

public class FileStorageServiceManager {
    private final FileStorageService localStorageService;
    private final FileStorageService cloudStorageService;
    @Getter
    private FileStorageService activeStorageService;
    @Getter
    private String currentStorageType;

    public FileStorageServiceManager(
            @Value("${storage.type}") String storageType,
            FileStorageService localStorageService,
            FileStorageService cloudStorageService) {
        this.localStorageService = localStorageService;
        this.cloudStorageService = cloudStorageService;
        this.currentStorageType = storageType;
        this.activeStorageService = "cloud".equalsIgnoreCase(storageType) ? cloudStorageService : localStorageService;
    }

    public void updateStorageType(String newStorageType) {
        this.currentStorageType = newStorageType;
        this.activeStorageService = "cloud".equalsIgnoreCase(newStorageType) ? cloudStorageService : localStorageService;
    }
}

