package com.yyh.common.execute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;


public class ExecuteMonitor extends ThreadPoolExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteMonitor.class);

    private ConcurrentHashMap<String, Long> startTimes = new ConcurrentHashMap<>();

    private String poolName;

    public ExecuteMonitor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ExecuteMonitor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ExecuteMonitor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler, String poolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.poolName = poolName;
    }

    public ExecuteMonitor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, String poolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.poolName = poolName;
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        startTimes.put(String.valueOf(r.hashCode()), System.currentTimeMillis());
    }


    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        Long startDate = startTimes.remove(String.valueOf(r.hashCode()));
        Long finishDate = System.currentTimeMillis();
        long diff = finishDate - startDate;
        /** 统计任务耗时、初始线程数、核心线程数、正在执行的任务数量、已完成任务数量、任务总数、队列里缓存的任务数量、池中存在的最大线程数、最大允许的线程数、线程空闲时间、线程池是否关闭、线程池是否终止 */
        logger.info(String.format(poolName + "-pool-monitor: Duration: %d ms, PoolSize: %d, CorePoolSize: %d, Active: %d, Completed: %d, Task: %d, Queue: %d, LargestPoolSize: %d, MaximumPoolSize: %d,  KeepAliveTime: %d, isShutdown: %s, isTerminated: %s",
                diff, this.getPoolSize(), this.getCorePoolSize(), this.getActiveCount(), this.getCompletedTaskCount(), this.getTaskCount(),
                this.getQueue().size(), this.getLargestPoolSize(), this.getMaximumPoolSize(), this.getKeepAliveTime(TimeUnit.MILLISECONDS),
                this.isShutdown(), this.isTerminated()));
    }
}
