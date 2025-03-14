package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.data.entity.Document;
import com.codev13.electrosign13back.data.entity.User;
import com.core.communs.validator.annotation.Exists;
import jakarta.validation.constraints.NotNull;

public record SignatureDocumentRequestDto(
        @NotNull
        String documentHash,
        @Exists(message = "Le document n'existe pas", field = "id", entity = Document.class)
        Long documentId,
        @Exists(message = "L'utilisateur n'existe pas", field = "id", entity = User.class)
        Long userId
) {
}
