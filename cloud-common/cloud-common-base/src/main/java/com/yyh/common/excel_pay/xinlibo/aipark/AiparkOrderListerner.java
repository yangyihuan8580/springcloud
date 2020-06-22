package com.yyh.common.excel_pay.xinlibo.aipark;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AbstractIgnoreExceptionReadListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.yyh.common.excel.account.AccountContext;
import com.yyh.common.excel.account.alipay.AlipayAccountModel;
import com.yyh.common.excel.account.wechat.WechatAccountModel;
import com.yyh.common.excel_pay.*;
import com.yyh.common.excel_pay.local.Order;
import com.yyh.common.excel_pay.xinlibo.ThirdOrder;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yyh
 * \* Date: 2020/3/23
 * \* Time: 12:19
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class AiparkOrderListerner implements ReadListener<AiparkOrder> {

    private Map<Key, Map<OrderKey, ThirdOrder>> thirdPayMap = PayContext.thirdPayMap;

    private Map<Key /** date */,TotalOrder> totalOrderMap = PayContext.totalOrderMap;

    private Map<String, Map<String, WechatAccountModel>> wechatAccountModelMap = AccountContext.wechatAccountModelMap;

    private Map<String, Map<String, AlipayAccountModel>> alipayAccountModelMap = AccountContext.alipayAccountModelMap;

    private Integer terminalId;

    private Integer count = 0;

    public AiparkOrderListerner(Integer terminalId) {
        this.terminalId = terminalId;
    }

    @Override
    public void onException(Exception e, AnalysisContext analysisContext) throws Exception {
        e.printStackTrace();
    }

    @Override
    public void invokeHead(Map<Integer, CellData> map, AnalysisContext analysisContext) {
        System.out.println("excel头:" + JSON.toJSONString(map));
    }

    @Override
    public void invoke(AiparkOrder order, AnalysisContext analysisContext) {
        ThirdOrder thirdOrder = new ThirdOrder();
        thirdOrder.setTerminalId(terminalId);
        BeanUtils.copyProperties(order, thirdOrder);
        if (!Filter.filter(order.getKey(terminalId), thirdOrder.getOrderKey())) {
            return;
        }
        if (thirdPayMap.isEmpty()) {
            System.out.println(JSON.toJSONString(order));
        }
        Map<OrderKey, ThirdOrder> dateMap = thirdPayMap.get(order.getKey(terminalId));
        if (dateMap == null) {
            thirdPayMap.put(order.getKey(terminalId),new HashMap<>());
        }
        ThirdOrder thirdOrderTemp = thirdPayMap.get(order.getKey(terminalId)).get(thirdOrder.getOrderKey());
        if (order.getPayType().contains("微信")) {
            Map<String, WechatAccountModel> stringWechatAccountModelMap = wechatAccountModelMap.get(order.getPayDate());
            if (stringWechatAccountModelMap != null) {
                WechatAccountModel wechatAccountModel = stringWechatAccountModelMap.get(order.getOrderCode());
                if (wechatAccountModel == null) {
                    System.out.println("数据在微信订单中不存在，直接过滤 orderCode：" + order.getOrderCode());
                    return;
                }
            }
        }

        if (order.getPayType().contains("支付宝")) {
            Map<String, AlipayAccountModel> stringAlipayAccountModelMap = alipayAccountModelMap.get(order.getPayDate());
            if (stringAlipayAccountModelMap != null) {
                AlipayAccountModel alipayAccountModel = stringAlipayAccountModelMap.get(order.getOrderCode());
                if (alipayAccountModel == null) {
                    System.out.println("数据在支付宝订单中不存在，直接过滤 orderCode：" + order.getOrderCode());
                    return;
                }
            }
        }
        if (null != thirdOrderTemp) {
            if (!thirdOrderTemp.getOrderCode().equals(order.getOrderCode())) {
                thirdOrderTemp.setCount(thirdOrderTemp.getCount() + 1);
            } else {
                return;
            }
        } else {
            thirdPayMap.get(order.getKey(terminalId)).put(thirdOrder.getOrderKey(), thirdOrder);
            addTotal(thirdOrder, order.getKey(terminalId));
        }
        count++;
    }

    private void addTotal(ThirdOrder order, Key key) {
        TotalOrder totalOrder = totalOrderMap.get(key);
        if (totalOrder == null) {
            totalOrderMap.put(key, new TotalOrder(key.getTerminalId(), key.getPayDate()));
        }
        totalOrderMap.get(key).addThirdMoney(order.getNeedMoney());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("localTotalMoneyMap" + JSON.toJSONString(thirdPayMap.size()));
        System.out.println("count:" + count);
        System.out.println("辛利泊对账单导入完毕");
    }

    @Override
    public boolean hasNext(AnalysisContext analysisContext) {
        return true;
    }
}