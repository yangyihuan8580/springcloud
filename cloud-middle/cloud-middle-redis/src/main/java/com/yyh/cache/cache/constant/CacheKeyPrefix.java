package com.yyh.cache.cache.constant;

public enum CacheKeyPrefix {

    /**  parkId -> channelId */
    CHANNEL_PARK("channel:park:", 60*5),
    CHANNEL_TCP_LOCK("channel:tcp:lock", 2),
    CHANNEL_HTTP_LOCAL("channel:http:lock", 2),
    ;

    private String prefix;

    /** 单位秒 */
    private long time;

    CacheKeyPrefix(String prefix, long time) {
        this.prefix = prefix;
        this.time = time;
    }


    public String getPrefix() {
        return prefix;
    }

    public long getTime() {
        return time;
    }
}
