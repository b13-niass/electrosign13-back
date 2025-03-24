package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.enums.PriorityDemande;
import com.codev13.electrosign13back.validator.annotation.PdfFile;
import com.codev13.electrosign13back.validator.annotation.PdfFileList;
import com.codev13.electrosign13back.validator.annotation.ValidMultiPattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public record DemandeCreateRequestDto(
        String titre,
        String description,
        @NotNull(message = "La date limite est obligatoire")
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "UTC")
        @DateTimeFormat(pattern = "dd-MM-yyyy")
        LocalDate dateLimite,
        @PdfFile
        @Nullable
        MultipartFile file,
        @PdfFileList
        List<MultipartFile> fileAttachment,
        List<UserDemandeRequestDto> signataires,
        @Nullable
        List<UserDemandeRequestDto> approbateurs,
        @Nullable
        List<UserDemandeRequestDto> ampliateurs,
        PriorityDemande priority
) {
}
