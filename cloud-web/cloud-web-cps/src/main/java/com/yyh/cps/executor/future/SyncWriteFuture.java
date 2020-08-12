package com.yyh.cps.executor.future;

import java.sql.Time;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SyncWriteFuture {

    private CountDownLatch latch = new CountDownLatch(1);
    private final long begin = System.currentTimeMillis();
    private long timeout;
    private Object response;
    private final String requestId;
    private Throwable cause;

    public SyncWriteFuture(String requestId, long timeout) {
        this.requestId = requestId;
        this.timeout = timeout;
    }


    public String requestId() {
        return requestId;
    }

    public Object response() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
        latch.countDown();
    }

    public Object get() throws InterruptedException, ExecutionException {
        latch.await();
        return response;
    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return response;
        }
        return null;
    }

    public boolean timeout() {
        return (System.currentTimeMillis() - begin) >= TimeUnit.SECONDS.toMillis(timeout);
    }

}
