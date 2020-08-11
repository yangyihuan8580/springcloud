package com.yyh.cps.controller;

import com.alibaba.fastjson.JSON;
import com.yyh.common.base.Result;
import com.yyh.cps.entity.TcpPushMessage;
import com.yyh.cps.executor.TcpResult;
import com.yyh.cps.service.CpsMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/message")
public class CpsMessageController {

    @Autowired
    private CpsMessageService cpsMessageService;

    @RequestMapping(value = "/send", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public TcpResult send(@RequestBody TcpPushMessage<Object> message) {
        log.info("cps数据下发到MS：{}", JSON.toJSONString(message));
        return cpsMessageService.sendMessage(message);

    }
}
