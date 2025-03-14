package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.data.entity.DemandeSignature;
import com.codev13.electrosign13back.data.entity.Document;
import com.codev13.electrosign13back.enums.StatusDemande;
import com.core.communs.validator.annotation.ExistsList;
import com.core.communs.validator.annotation.ValidMultiPattern;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Date;
import java.util.List;

public record DemandeRequestDto(
        @NotNull(message = "Le titre est obligatoire")
        String titre,
        String description,
        @NotNull(message = "La date limite est obligatoire")
        @ValidMultiPattern(
                patterns = {
                        "^\\d{4}-\\d{2}-\\d{2}$",
                        "^\\d{2}/\\d{2}/\\d{4}$",
                        "^\\d{2}-\\d{2}-\\d{4}$",
                        "^\\d{4}/\\d{2}/\\d{2}$",
                        "^\\d{4}\\.\\d{2}\\.\\d{2}$",
                        "^\\d{2} [A-Za-z]{3} \\d{4}$",
                        "^\\d{2} [A-Za-z]+ \\d{4}$"
                },
                message = "La date est invalide"
        )
        Date dateLimite,
        @Positive(message = "Le nombre de d'approbation doit être positive")
        int nombreApprobation,
        @Positive(message = "Le nombre de signature doit être positive")
        int nombreSignature,
        @Positive(message = "Le nombre d'approbation actuel doit être positive")
        int currentApprobation,
        @Positive(message = "Le nombre de signature actuelle doit être positive")
        int currentSignature,
        @Nullable
        StatusDemande status,
        @ExistsList(message = "L'un des Signatures n'existe pas", field = "id", entity = DemandeSignature.class)
        @Nullable
        List<Long> demandeSignatureIds,
        @ExistsList(message = "L'un des document n'existe pas", field = "id", entity = Document.class)
        @Nullable
        List<Long> documentIds
) {
}
