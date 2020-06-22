package com.yyh.common.excel_pay.local;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AbstractIgnoreExceptionReadListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.yyh.common.excel.account.local.LocalAccountModel;
import com.yyh.common.excel_pay.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yyh
 * \* Date: 2020/3/23
 * \* Time: 11:41
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class LocalPayListerner  implements ReadListener<Order> {

    private Map<Key /** date */,Map<OrderKey /** orderCode */, Order>> localPayMap = PayContext.localPayMap;

    private Map<Key /** orderCode */,TotalOrder> totalOrderMap = PayContext.totalOrderMap;

    private Map<String, OrderKey> localOrderMap = PayContext.localOrderMap;
    
    private List<Order> repeatOrderList = PayContext.repeatOrderList;

    private Integer count = 0;

    @Override
    public void onException(Exception e, AnalysisContext analysisContext) throws Exception {
        e.printStackTrace();
    }

    @Override
    public void invokeHead(Map<Integer, CellData> map, AnalysisContext analysisContext) {
        System.out.println("excel头:" + JSON.toJSONString(map));
    }

    @Override
    public void invoke(Order order, AnalysisContext analysisContext) {
        if (!Filter.filter(order.getKey(), order.getOrderKey())) {
            return;
        }
        if (localPayMap.isEmpty()) {
            System.out.println(JSON.toJSONString(order));
        }
        if (order != null && order.getD() == 1) {
            return;
        }
        if (order != null && order.getOrderStatus() == 0) {
            return;
        }
        Map<OrderKey, Order> dateMap = localPayMap.get(order.getKey());
        if (dateMap == null) {
            localPayMap.put(order.getKey(), new HashMap<>());
        }
        localOrderMap.put(order.getOrderCode(), order.getOrderKey());
        Map<OrderKey, Order> orderKeyOrderMap = localPayMap.get(order.getKey());
        if (null != orderKeyOrderMap.get(order.getOrderKey())) {
            System.out.println("重复缴费订单：" + JSON.toJSONString(orderKeyOrderMap.get(order.getOrderKey())));
            System.out.println("重复缴费订单：" + JSON.toJSONString(order));
            repeatOrderList.add(order);
        } else {
            orderKeyOrderMap.put(order.getOrderKey(), order);
            addTotal(order, order.getKey());
        }
    }

    private void addTotal(Order order, Key key) {
        TotalOrder totalOrder = totalOrderMap.get(key);
        if (totalOrder == null) {
            totalOrderMap.put(key, new TotalOrder(key.getTerminalId(), key.getPayDate()));
        }
        totalOrderMap.get(key).addLocalMoney(order.getNeedMoney());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("localTotalMoneyMap" + JSON.toJSONString(localPayMap.size()));
        System.out.println("本地对账单导入完毕");
    }

    @Override
    public boolean hasNext(AnalysisContext analysisContext) {
        return true;
    }


}