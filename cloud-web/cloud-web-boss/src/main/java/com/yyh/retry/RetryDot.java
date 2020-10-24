package com.yyh.retry;

import java.lang.annotation.*;

/**
 * @ClassName RetryDot
 * @Deacription TODO
 * @Author Administrator
 * @Date 2020/10/24 13:36
 * @Version 1.0
 **/

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryDot {

    /**
     * 重试次数
     * @return
     */
    int count() default 0;


    /**
     * 重试的间隔时间
     * @return
     */
    int sleep() default 0;


    /**
     * 是否支持异步重试方式
     * @return
     */
    boolean asyn() default false;
}
