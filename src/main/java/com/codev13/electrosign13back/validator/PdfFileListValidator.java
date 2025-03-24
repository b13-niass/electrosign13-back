package com.codev13.electrosign13back.validator;

import com.codev13.electrosign13back.validator.annotation.PdfFileList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class PdfFileListValidator implements ConstraintValidator<PdfFileList, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) {
                    return false;
                }
                String fileName = file.getOriginalFilename();
                if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
                    return false;
                }
                if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
                    return false;
                }
            }
        }

        return true;
    }
}
