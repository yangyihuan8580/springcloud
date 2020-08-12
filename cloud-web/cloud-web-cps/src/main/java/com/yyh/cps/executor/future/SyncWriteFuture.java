package com.yyh.cps.executor.future;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SyncWriteFuture {

    private CountDownLatch responseLatch = new CountDownLatch(1);

    private CountDownLatch statusLatch = new CountDownLatch(1);

    private final long begin = System.currentTimeMillis();
    private long timeout;
    private Object response;
    /** 发送状态， true 发送成功 false 发送失败 */
    private boolean status;
    private final String msgId;

    public SyncWriteFuture(String msgId, long timeout) {
        this.msgId = msgId;
        this.timeout = timeout;
    }

    public void setStatus(boolean status) {
        this.status = status;
        statusLatch.countDown();
    }

    public boolean isStatus(long timeout, TimeUnit unit) throws InterruptedException {
        if (statusLatch.await(timeout, unit)) {
            return status;
        }
        return false;
    }

    public String msgId() {
        return msgId;
    }

    public Object response() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
        responseLatch.countDown();
    }

    public Object getResponse(long timeout, TimeUnit unit) throws InterruptedException {
        if (responseLatch.await(timeout, unit)) {
            return response;
        }
        return null;
    }

    public boolean timeout() {
        return (System.currentTimeMillis() - begin) >= TimeUnit.SECONDS.toMillis(timeout);
    }

}
