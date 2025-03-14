package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.data.entity.Demande;
import com.codev13.electrosign13back.data.entity.User;
import com.core.communs.validator.annotation.Exists;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DemandeSignatureRequestDto(
        @Nullable
        String action,
        @Positive(message = "L'ordre doit être positive")
        @NotNull(message = "L'ordre est obligatoire")
        int ordre,
        @Positive(message = "Le numéro du détenant doit être positive")
        @NotNull(message = "Le numéro du détenant est obligatoire")
        int detenant,
        @Exists(message = "La demande n'existe pas", field = "id", entity = Demande.class)
        @NotNull(message = "L'id du demande est obligatoire")
        Long demandeId,
        @Exists(message = "Le Sender n'existe pas", field = "id", entity = User.class)
        @NotNull(message = "L'id du sender est obligatoire")
        Long senderId,
        @Exists(message = "Le receiver n'existe pas", field = "id", entity = User.class)
        @NotNull(message = "L'id du receiver est obligatoire")
        Long receiverId
) {
}
