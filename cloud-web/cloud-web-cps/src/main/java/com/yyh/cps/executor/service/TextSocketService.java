package com.yyh.cps.executor.service;


import com.yyh.common.base.Result;
import com.yyh.cps.executor.TcpMessage;

public interface TextSocketService {

    Result process(TcpMessage tcpMessage);
}