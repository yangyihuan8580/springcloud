package com.yyh.cps.executor;


public interface TcpMessageService {

     TcpResult execute(TcpUploadMessage tcpMessage) throws RuntimeException;

}