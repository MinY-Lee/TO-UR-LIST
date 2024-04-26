package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.User;

public interface UserService {
    void createUser(User user);
    void deleteUser(String userId);
}
