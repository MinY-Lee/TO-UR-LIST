package com.eminyidle.auth.auth.service;

import com.eminyidle.auth.redis.RedisPrefix;
import com.eminyidle.auth.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisService redisService;

    @Transactional
    public void logoutUser(String userId) {
        //관련 토큰 모두 레디스에서 제거
        String tokenKey = RedisPrefix.REFRESH_TOKEN.prefix() + userId;
        redisService.deleteValues(tokenKey);
        log.debug("Refresh 토큰 제거");
    }

}