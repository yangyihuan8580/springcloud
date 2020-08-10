package com.yyh.cps.executor;

import com.alibaba.fastjson.JSON;
import com.yyh.common.base.Result;
import com.yyh.common.context.SpringContextUtils;
import com.yyh.cps.constant.CpsApiConstant;
import com.yyh.cps.constant.CpsResultCodeEnum;
import com.yyh.cps.executor.request.RequestMapping;
import com.yyh.cps.socket.ChannelRepository;
import com.yyh.cps.socket.ServerConfig;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Component
public class TextSocketHelper {

    private static Logger logger = LoggerFactory.getLogger(TcpMessageService.class);

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private ServerConfig serverConfig;

    public void process(final TcpMessage tcpMessage) {
        logger.info("消息放入业务池中message:{},parkId:{}", tcpMessage.getMessage(), tcpMessage.getParkId());
        executor.execute(new Runnable() {

            @Override
            public void run() {
                logger.info("开始处理tcp消息,message:{},parkId:{}", tcpMessage.getMessage(), tcpMessage.getParkId());
                UploadMessage uploadMessage = tcpMessage.parseData();
                boolean checked = checkMessage(uploadMessage);
                if (!checked) {
                    logger.info("==========报文格式错误=======");
                    String result = TcpResult.result(CpsResultCodeEnum.PARAM_ERROR, uploadMessage.getCode(), uploadMessage.getMsgId()).toString();
                    setResult(tcpMessage, result);
                    return;
                }
                /**  判断车场是否注册 */
                if (!uploadMessage.getCode().equals(CpsApiConstant.CPS_001)) {
                    Channel channel = ChannelRepository.getInstance().getChannel(tcpMessage.getParkId());
                    if (channel == null) {
                        logger.info("==========车场未注册=======");
                        String result = TcpResult.result(CpsResultCodeEnum.UNREGISTERED, uploadMessage.getCode(), uploadMessage.getMsgId()).toString();
                        setResult(tcpMessage, result);
                        return;
                    } else {
                        /** 更新车场token信息 */
                        ChannelRepository.getInstance().updateChannelInfo(channel);
                    }
                }
                Class<?> tcpMessageServiceClass = RequestMapping.getClassByCode(uploadMessage.getCode());
                if (tcpMessageServiceClass == null) {
                    String result = TcpResult.result(CpsResultCodeEnum.CODE_ERROR, uploadMessage.getCode(), uploadMessage.getMsgId()).toString();
                    setResult(tcpMessage, result);
                    return;
                }
                TcpMessageService tcpMessageService = (TcpMessageService) SpringContextUtils.getBean(tcpMessageServiceClass);
                if (tcpMessageService == null) {
                    String result = TcpResult.result(CpsResultCodeEnum.CODE_ERROR, uploadMessage.getCode(), uploadMessage.getMsgId()).toString();
                    setResult(tcpMessage, result);
                    return;
                }
                TcpResult tcpResult = tcpMessageService.execute(uploadMessage);
                String result;
                if (tcpResult != null) {
                    result = tcpResult.toString();
                } else {
                    result = TcpResult.success(uploadMessage.getCode(), uploadMessage.getMsgId()).toString();
                }
                if (tcpResult != null) {
                    setResult(tcpMessage, result);
                }
            }
        });
    }

    private void setResult(TcpMessage tcpMessage, String s) {
        logger.info("请求返回报文:{}", s);
        tcpMessage.getCtx().writeAndFlush(s + serverConfig.getDelimiter());
    }

    private boolean checkMessage(UploadMessage uploadMessage) {
        if (uploadMessage == null
                || StringUtils.isEmpty(uploadMessage.getCode())
                || StringUtils.isEmpty(uploadMessage.getMsgId())
                || null == uploadMessage.getCurrentTime()
            ) {
            return false;
        }
        return true;
    }
}