package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.validator.annotation.PdfFile;
import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record SignerDemandeRequestDto(
        Long demandeId,
//        @PdfFile
        MultipartFile file,
        @Nullable
        String signature
) {
}
