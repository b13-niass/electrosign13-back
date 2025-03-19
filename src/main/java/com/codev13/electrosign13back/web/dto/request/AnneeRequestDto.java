package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.data.entity.Annee;
import com.codev13.electrosign13back.data.entity.Filiale;
import com.codev13.electrosign13back.validator.annotation.ExistsList;
import com.codev13.electrosign13back.validator.annotation.Unique;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AnneeRequestDto(
        @NotNull(message = "Le libelle est obligatoire")
        @Unique(message = "L'année existe déjà", field = "libelle", entity = Annee.class)
        String libelle,
        @Nullable
        @ExistsList(message = "L'un des filiales n'existe pas", field = "id", entity = Filiale.class)
        List<Long> filialeIds
) {
}
