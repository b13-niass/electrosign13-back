package com.codev13.electrosign13back.validator;

import com.codev13.electrosign13back.validator.annotation.PdfFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class PdfFileValidator implements ConstraintValidator<PdfFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
            return false;
        }

        return "application/pdf".equalsIgnoreCase(file.getContentType());
    }
}
