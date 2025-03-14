package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.data.entity.Departement;
import com.codev13.electrosign13back.data.entity.Filiale;
import com.core.communs.validator.annotation.ExistsList;
import com.core.communs.validator.annotation.Unique;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DepartementRequestDto(
        @NotNull(message = "le libelle est obligatoire")
        @Unique(message = "Le libelle existe déjà", field = "libelle", entity = Departement.class)
        String libelle,
        @NotNull(message = "l'accronyme est obligatoire")
        @Unique(message = "L'accronyme existe déjà", field = "acronyme", entity = Departement.class)
        String acronyme,
        @Nullable
        @ExistsList(message = "Il y'a une filiale qui n'existe pas", field = "id", entity = Filiale.class)
        List<Long> filialeIds
) {
}
