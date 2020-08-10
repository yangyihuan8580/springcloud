package com.yyh.cps.executor;


public interface TcpMessageService {

     TcpResult execute(UploadMessage tcpMessage) throws RuntimeException;

}