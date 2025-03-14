package com.codev13.electrosign13back.web.controller.impl;

import com.codev13.electrosign13back.data.entity.Notification;
import com.codev13.electrosign13back.data.repository.NotificationRepository;
import com.codev13.electrosign13back.service.NotificationService;
import com.codev13.electrosign13back.web.controller.NotificationController;
import com.codev13.electrosign13back.web.dto.request.NotificationRequestDto;
import com.codev13.electrosign13back.web.dto.response.NotificationResponseDto;
import com.core.communs.core.GenericController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/notifications")
public class NotificationControllerImpl extends GenericController<Notification, NotificationResponseDto, NotificationRequestDto> implements NotificationController {
    private final NotificationService service;
    public NotificationControllerImpl(NotificationRepository repository, NotificationService service) {
        super(repository, Notification.class, NotificationResponseDto.class);
        this.service = service;
    }
}