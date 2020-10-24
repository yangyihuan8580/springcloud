package com.yyh.retry;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @ClassName RetryTemplate
 * @Deacription TODO
 * @Author Administrator
 * @Date 2020/10/24 13:38
 * @Version 1.0
 **/
public abstract class RetryTemplate {

    private static final Logger log = LoggerFactory.getLogger(RetryTemplate.class);

    private static final int DEFAULT_RETRY_TIME = 1;

    private int retryTime = DEFAULT_RETRY_TIME;

    // 重试的睡眠时间
    private int sleepTime = 0;

    private int retryCount = 1;

    public int getSleepTime() {
        return sleepTime;
    }

    public RetryTemplate setSleepTime(int sleepTime) {
        if(sleepTime < 0) {
            throw new IllegalArgumentException("sleepTime should equal or bigger than 0");
        }

        this.sleepTime = sleepTime;
        return this;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public RetryTemplate setRetryTime(int retryTime) {
        if (retryTime <= 0) {
            throw new IllegalArgumentException("retryTime should bigger than 0");
        }

        this.retryTime = retryTime;
        return this;
    }

    /**
     * 重试的业务执行代码
     * 失败时请抛出一个异常
     *
     * todo 确定返回的封装类，根据返回结果的状态来判定是否需要重试
     *
     * @return
     */
    protected abstract RetryResponse doBiz() throws Throwable;


    public Object execute() throws InterruptedException {
        log.info("retryCount:{}, sleepTime:{}", retryCount, sleepTime);
        for (int i = 0; i < retryCount; i++) {
            try {
                RetryResponse retryResponse = doBiz();
                log.info("第{}次执行返回,retryResponse:{}", i+1, JSON.toJSONString(retryResponse));
                if (retryResponse != null) {
                    if (retryResponse.getStatus() == 200) {
                        return retryResponse.getData();
                    }
                }
            } catch (Exception e) {
                log.error("业务执行出现异常，e: {}", e);
            } catch (Throwable throwable) {
                log.error("业务执行出现异常，e: {}", throwable);
            }
            Thread.sleep(sleepTime);
        }
        /**  执行指定次数后还没执行成功 */
        log.info("放入数据库中==========================");
        return null;
    }


    public int getRetryCount() {
        return retryCount;
    }

    public RetryTemplate setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public Object submit(ExecutorService executorService) {
        if (executorService == null) {
            throw new IllegalArgumentException("please choose executorService!");
        }

        Future submit = executorService.submit((Callable) () -> execute());

        try {
            return submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
