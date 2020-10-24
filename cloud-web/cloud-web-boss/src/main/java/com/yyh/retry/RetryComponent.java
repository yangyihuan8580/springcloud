package com.yyh.retry;

/**
 * @Classname RetryComponent
 * @Description TODO
 * @Date 2020/10/24 15:07
 * @Created by yangyihuan@aipark.com
 */

import org.springframework.stereotype.Component;


@Component
public class RetryComponent {


    @RetryDot(count = 3, sleep = 100, asyn = true)
    public Object retryTestSuccess() throws Exception {
//        Response response = HttpUtil.httpGet("www.baidu.com", null, 100000, null, null, null, null, null);
//        return response.getBody();
        return null;
    }


    @RetryDot(count = 3, sleep = 100, asyn = true)
    public Object retryTestFailed() throws Exception {
        throw new Exception("失败~");
    }
}
