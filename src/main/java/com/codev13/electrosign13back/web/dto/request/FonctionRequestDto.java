package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.data.entity.Departement;
import com.codev13.electrosign13back.data.entity.Filiale;
import com.codev13.electrosign13back.data.entity.Fonction;
import com.core.communs.validator.annotation.Exists;
import com.core.communs.validator.annotation.Unique;

public record FonctionRequestDto(
        @Unique(message = "Le libelle existe déjà", field = "libelle", entity = Fonction.class)
        String libelle,
        @Unique(message = "L'acronyme existe déjà", field = "acronyme", entity = Fonction.class)
        String acronyme,
        @Exists(message = "Le département n'existe pas", field = "id", entity = Departement.class)
        Long departementId
) {
}
