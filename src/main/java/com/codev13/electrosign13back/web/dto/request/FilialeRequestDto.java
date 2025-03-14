package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.data.entity.Departement;
import com.codev13.electrosign13back.data.entity.Filiale;
import com.core.communs.validator.annotation.ExistsList;
import com.core.communs.validator.annotation.Unique;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FilialeRequestDto(
        @Unique(message = "Le libelle existe déjà", field = "libelle", entity = Filiale.class)
        String libelle,
        @Unique(message = "L'acronyme existe déjà", field = "acronyme", entity = Filiale.class)
        String acronyme,
        @ExistsList(message = "L'un des département n'existe pas", field = "id", entity = Departement.class)
        List<Long> departementIds
) {
}
