package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.data.entity.Demande;
import com.codev13.electrosign13back.enums.EtatDocument;
import com.core.communs.validator.annotation.Exists;
import jakarta.validation.constraints.NotNull;

public record DocumentRequestDto(
        @NotNull
        String nom,
        @NotNull
        String contenu,
        EtatDocument etatDocument,
        @Exists(message = "La demande n'existe pas", field = "id", entity = Demande.class)
        @NotNull
        Long demandeId
) {
}
