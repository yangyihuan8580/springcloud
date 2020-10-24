package com.yyh.retry;

/**
 * @Classname RetryAspect
 * @Description TODO
 * @Date 2020/10/24 13:37
 * @Created by yangyihuan@aipark.com
 */

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RetryAspect
 * @Deacription TODO
 * @Author Administrator
 * @Date 2020/10/24 13:37
 * @Version 1.0
 **/
@Aspect
@Component
public class RetryAspect {

    private static final Logger log = LoggerFactory.getLogger(RetryAspect.class);

    ExecutorService executorService = new ThreadPoolExecutor(3, 5,
            1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<Runnable>());


    @Around(value = "@annotation(retryDot)")
    public Object execute(ProceedingJoinPoint joinPoint, RetryDot retryDot) throws Exception {
        log.info("执行重试任务:===============================,count:{}, sleep:{}", retryDot.count(), retryDot.sleep());
        RetryTemplate retryTemplate = new RetryTemplate() {
            @Override
            protected RetryResponse<?> doBiz() throws Throwable {
                RetryResponse<Object> retryResponse = new RetryResponse<>();
                try {
                    Object proceed = joinPoint.proceed();
                    retryResponse.setData(proceed);
                    retryResponse.setStatus(200);
                } catch (Throwable throwable) {
                    retryResponse.setStatus(500);
                    retryResponse.setMessage(throwable.getMessage());
                }
                return retryResponse;
            }
        };

        retryTemplate.setRetryCount(retryDot.count())
                .setSleepTime(retryDot.sleep());

        if (retryDot.asyn()) {
            return retryTemplate.submit(executorService);
        } else {
            return retryTemplate.execute();
        }
    }

}
