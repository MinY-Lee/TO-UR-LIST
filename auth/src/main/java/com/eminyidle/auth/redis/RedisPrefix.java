package com.eminyidle.auth.redis;

public enum RedisPrefix {
    REFRESH_TOKEN("loginRefresh:"),
    REGIST("regist:"),
    USERINFO("userInfo:");

    private String prefix;

    RedisPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String prefix() {
        return prefix;
    }
}
