package com.yyh.cps.service;

import com.yyh.cps.entity.TcpPushMessage;
import com.yyh.cps.executor.TcpResult;

public interface CpsMessageService {

    TcpResult sendMessage(TcpPushMessage tcpPushMessage);
}
