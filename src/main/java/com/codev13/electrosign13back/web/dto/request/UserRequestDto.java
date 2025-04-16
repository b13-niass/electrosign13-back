package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.data.entity.Fonction;
import com.codev13.electrosign13back.data.entity.Role;
import com.codev13.electrosign13back.data.entity.User;
import com.codev13.electrosign13back.validator.annotation.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public record UserRequestDto(
        @NotNull
        String prenom,
        @NotNull
        String nom,
        @Unique(message = "L'email existe déjà", field = "email", entity = User.class)
        String email,
        @NotNull
        String password,
        @Unique(message = "Le téléphone existe déjà", field = "telephone", entity = User.class)
        @ValidPhoneNumber
        String telephone,
        @ImageFile
        MultipartFile photo,
        @Unique(message = "Le cni existe déjà", field = "cni", entity = User.class)
        String cni,
        @Exists(message = "La fonction n'existe pas", field = "id", entity = Fonction.class)
        Long fonctionId,
//        @ExistsList(message = "L'un des roles n'existe pas", field = "roles", entity = Role.class)
        List<String> roles
) {
}
