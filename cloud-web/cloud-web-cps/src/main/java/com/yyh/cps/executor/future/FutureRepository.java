package com.yyh.cps.executor.future;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FutureRepository {

    public static Map<String, SyncWriteFuture> futureMap = new ConcurrentHashMap<>();


    public static SyncWriteFuture createFuture(String msgId, long timeout) {
        SyncWriteFuture future = new SyncWriteFuture(msgId, timeout);
        futureMap.put(msgId, future);
        return future;
    }

}
