package com.codev13.electrosign13back.service;

public interface TokenProvider {
    String getToken();
    String getEmailFromToken();
}
