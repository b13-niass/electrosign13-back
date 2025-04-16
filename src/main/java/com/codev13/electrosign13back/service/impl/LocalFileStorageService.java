package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service("localStorageService")
public class LocalFileStorageService implements FileStorageService {

    @Value("${storage.local.path}")
    private String localPath;

    @Override
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destination = new File(localPath + File.separator  + folder + File.separator + fileName);

        // Créer le dossier s'il n'existe pas
        destination.getParentFile().mkdirs();

        try (FileOutputStream fos = new FileOutputStream(destination)) {
            fos.write(file.getBytes());
        }

        return folder+"/"+fileName;
    }
}
