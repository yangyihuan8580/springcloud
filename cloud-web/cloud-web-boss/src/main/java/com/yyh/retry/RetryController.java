package com.yyh.retry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Classname RetryController
 * @Description TODO
 * @Date 2020/10/24 14:50
 * @Created by yangyihuan@aipark.com
 */
@RestController
@RequestMapping("/retry")
public class RetryController {

    @Autowired
    private RetryComponent retryComponent;

    @GetMapping(value = "success")
    public Object success() throws Exception {
        return retryComponent.retryTestSuccess();
    }


    @GetMapping(value = "failed")
    public Object failed() throws Exception {
        return retryComponent.retryTestFailed();
    }

}
