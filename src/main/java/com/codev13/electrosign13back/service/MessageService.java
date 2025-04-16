package com.codev13.electrosign13back.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface MessageService {
    boolean sendMessage(String recipient, String message);
    String sendMailWithThymeleaf(String recipient, String messageType, Map<String, Object> messageData)
            throws MessagingException, UnsupportedEncodingException;
}