package com.eminyidle.auth.service;

public interface AuthService {
    void logoutUser(String userId);

    void deleteUser(String userId);
}
