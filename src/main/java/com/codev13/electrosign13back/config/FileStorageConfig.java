package com.codev13.electrosign13back.config;

import com.codev13.electrosign13back.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {

    @Bean
    public FileStorageService fileStorageService(
            @Value("${storage.type}") String storageType,
            FileStorageService localStorageService,
            FileStorageService cloudStorageService) {

        return "cloud".equalsIgnoreCase(storageType) ? cloudStorageService : localStorageService;
    }
}
