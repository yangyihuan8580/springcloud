package com.yyh.cps.executor.request;


import com.alibaba.fastjson.JSON;
import com.yyh.cps.constant.CpsApiConstant;
import com.yyh.cps.executor.TcpMessageService;
import com.yyh.cps.executor.TcpResult;
import com.yyh.cps.executor.UploadMessage;
import com.yyh.cps.socket.ChannelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Request(code = CpsApiConstant.CPS_001)
public class ParkRegisterRequest implements TcpMessageService {

    private static Logger logger = LoggerFactory.getLogger(TcpMessageService.class);

    @Override
    public TcpResult execute(UploadMessage tcpMessage) {
        logger.info("车场信息注册:{}", JSON.toJSONString(tcpMessage));
        ChannelRepository.getInstance().putChannel(tcpMessage.getParkId(), tcpMessage.getCtx().channel());

        return TcpResult.success(tcpMessage.getCode(), tcpMessage.getMsgId());
    }
}
