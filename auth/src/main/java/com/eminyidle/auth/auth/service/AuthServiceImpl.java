package com.eminyidle.auth.auth.service;

import com.eminyidle.auth.game.service.RedisGameInfoService;
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
    private final RedisGameInfoService redisGameInfoService;

    @Transactional
    public void logoutUser(String userId) {
        //관련 토큰 모두 레디스에서 제거
        String tokenKey = RedisPrefix.REFRESH_TOKEN.prefix() + userId;
        redisService.deleteValues(tokenKey);
        log.debug("Refresh 토큰 제거");

        String userInfoKey = RedisPrefix.USERINFO.prefix() + userId;
        redisService.deleteValues(userInfoKey);
        log.debug("유저 정보 제거");

        String webSocketKey = RedisPrefix.WEBSOCKET.prefix() + userId;
        redisService.deleteValues(webSocketKey);
        log.debug("웹소켓 제거");

        // DB에 인게임 정보 저장
        redisGameInfoService.saveRedisGameInfo(userId);

        // 레디스에서 인게임 정보 제거
        String inGameInfoKey = RedisPrefix.INGAMEINFO.prefix() + userId;
        redisService.deleteValues(inGameInfoKey);
        log.debug("인게임 정보 제거");
    }

}