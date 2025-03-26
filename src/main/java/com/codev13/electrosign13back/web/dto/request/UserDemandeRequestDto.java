package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.data.entity.User;
import com.codev13.electrosign13back.validator.annotation.Exists;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

public record UserDemandeRequestDto(
        @Exists(message = "l'utilisateur n'existe pas", field = "id", entity = User.class)
        Long id,
        @NotNull
        @NotEmpty
        String action,
        @NotNull
        @NotEmpty
        int ordre
) {
}
