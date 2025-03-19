package com.codev13.electrosign13back.web.dto.request;

import com.codev13.electrosign13back.data.entity.User;
import com.codev13.electrosign13back.enums.StatusNotification;
import com.codev13.electrosign13back.enums.TypeNotification;
import com.codev13.electrosign13back.validator.annotation.Exists;
import jakarta.validation.constraints.NotNull;

public record NotificationRequestDto(
        @NotNull
        String message,
        @NotNull
        TypeNotification type,
        StatusNotification status,
        @Exists(message = "L'utilisateur n'existe pas", field = "id", entity = User.class)
        Long userId
) {
}
