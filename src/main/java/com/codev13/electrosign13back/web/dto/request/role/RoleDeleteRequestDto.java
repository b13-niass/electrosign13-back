package com.codev13.electrosign13back.web.dto.request.role;

import com.codev13.electrosign13back.data.entity.Role;
import com.codev13.electrosign13back.validator.annotation.Exists;

public record RoleDeleteRequestDto(
        @Exists(message = "Le libelle n'existe pas", field = "libelle", entity = Role.class)
        String libelle
) {
}
