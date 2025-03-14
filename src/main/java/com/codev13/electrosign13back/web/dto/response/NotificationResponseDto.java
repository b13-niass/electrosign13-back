package com.codev13.electrosign13back.web.dto.response;

import com.codev13.electrosign13back.enums.StatusNotification;
import com.codev13.electrosign13back.enums.TypeNotification;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String message;
    private TypeNotification type;
    private StatusNotification status;
    private UserResponseDto user;
}
