package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.service.impl.FileStorageServiceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/public/storage")
@RequiredArgsConstructor
public class FileStorageControllerImpl {
    private final FileStorageServiceManager fileStorageServiceManager;

    @GetMapping("/type")
    public ResponseEntity<Map<String, String>> getCurrentStorageType() {
        return ResponseEntity.ok(Map.of("currentStorage: ", fileStorageServiceManager.getCurrentStorageType()));
    }

    @PostMapping("/type/{newType}")
    public ResponseEntity<Map<String, String>> updateStorageType(@PathVariable String newType) {
        if (!newType.equalsIgnoreCase("cloud") && !newType.equalsIgnoreCase("local")) {
            return ResponseEntity.badRequest().body(Map.of("currentStorage", "ce stockage n'existe pas"));
        }

        fileStorageServiceManager.updateStorageType(newType);

        return ResponseEntity.ok(Map.of("currentStorage", newType) );
    }
}
