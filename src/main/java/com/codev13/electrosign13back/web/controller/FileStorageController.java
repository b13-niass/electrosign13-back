package com.codev13.electrosign13back.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface FileStorageController {
    ResponseEntity<String> getCurrentStorageType();
    ResponseEntity<String> updateStorageType(@PathVariable String newType);
}
