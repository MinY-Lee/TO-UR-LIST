package com.eminyidle.tour.application.port;

import com.eminyidle.tour.application.dto.User;

public interface UserUpdateUsecase {

    void updateUser(User user);

    void deleteUser(String userId);
}
