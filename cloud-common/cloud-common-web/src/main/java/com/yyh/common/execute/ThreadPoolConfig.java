package com.yyh.common.execute;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class ThreadPoolConfig {

    @Value("${executor.corePoolSize}")
    private int corePoolSize;//核心线程数
 
    @Value("${executor.maximumPoolSize}")
    private int maximumPoolSize;//最大线程数
 
    @Value("${executor.queueCapacity}")
    private int queueCapacity;//队列最大长度
 
    @Value("${executor.keepAliveSeconds}")
    private int keepAliveSeconds;//线程池维护线程所允许的空闲时间

    @Value("${executor.poolName}")
    private String poolName;//线程池名称
 
    private ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();//线程池对拒绝任务(无线程可用)的处理策略
 
    @Bean(name = "taskExecutor")
    public ThreadPoolExecutor executor() {
        log.info("启动线程池==poolName:{},corePoolSize:{},maxPoolSize:{},keepAliveSeconds:{},queueCapacity:{}",
                poolName, corePoolSize, maximumPoolSize, keepAliveSeconds, queueCapacity);
        ThreadPoolExecutor executor = new ExecuteMonitor(
                corePoolSize,
                maximumPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(queueCapacity),
                abortPolicy,
                poolName);
        return executor;
    }
 
}
