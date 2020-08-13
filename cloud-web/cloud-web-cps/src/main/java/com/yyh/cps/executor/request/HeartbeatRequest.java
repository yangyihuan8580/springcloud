package com.yyh.cps.executor.request;

import com.yyh.cps.constant.CpsApiConstant;
import com.yyh.cps.executor.TcpMessageService;
import com.yyh.cps.executor.TcpResult;
import com.yyh.cps.executor.TcpUploadMessage;
import com.yyh.cps.socket.ChannelRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Request(code = CpsApiConstant.CPS_002)
public class HeartbeatRequest implements TcpMessageService {

    @Override
    public TcpResult execute(TcpUploadMessage uploadMessage) throws RuntimeException {
        log.info("接收到心跳请求============");
        ChannelRepository.getInstance().updateChannelInfo(uploadMessage.getCtx().channel(), uploadMessage.getParkId());

        return TcpResult.success(uploadMessage.getCode(), uploadMessage.getMsgId());
    }
}
