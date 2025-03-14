package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.data.entity.Role;
import com.core.communs.validator.annotation.Unique;

public record RoleRequestDto(
        @Unique(message = "Le role existe déjà", field = "libelle", entity = Role.class)
        String libelle
) {
}
