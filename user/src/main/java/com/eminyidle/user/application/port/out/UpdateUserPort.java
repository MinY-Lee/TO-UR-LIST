package com.eminyidle.user.application.port.out;

import com.eminyidle.user.domain.User;

public interface UpdateUserPort {
    void send(User user);
}
