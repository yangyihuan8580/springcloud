package com.yyh.pay;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PayContext {

    private static Map<String,StrategyPay> strategyPayMap = new ConcurrentHashMap<String,StrategyPay>();

    static {
        strategyPayMap.put("alipay", new AliStrategyPay());
    }


    public static void pay(String payCode, String param) {
        /** 支付之前的业务逻辑 */
        strategyPayMap.get(payCode).pay(param);
        /** 支付之后的业务逻辑 */
    }


}