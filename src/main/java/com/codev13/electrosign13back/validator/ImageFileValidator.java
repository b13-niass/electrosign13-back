package com.codev13.electrosign13back.validator;

import com.codev13.electrosign13back.validator.annotation.ImageFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageFileValidator implements ConstraintValidator<ImageFile, MultipartFile> {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png");
    private static final List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png");

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }

        String fileExtension = getFileExtension(fileName);
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            return false;
        }

        // Vérifier le type MIME
        return ALLOWED_MIME_TYPES.contains(file.getContentType());
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return (lastIndex == -1) ? "" : fileName.substring(lastIndex + 1);
    }
}
