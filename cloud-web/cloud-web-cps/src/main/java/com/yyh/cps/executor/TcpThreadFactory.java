package com.yyh.cps.executor;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TcpThreadFactory implements ThreadFactory{

    private AtomicInteger atomicInteger = new AtomicInteger();

    private static final String PREFIX = "tcp-message-thread-";

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, PREFIX+atomicInteger.get());
    }
}