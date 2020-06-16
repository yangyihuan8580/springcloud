package com.yyh.common.excel_pay.xinlibo.local;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AbstractIgnoreExceptionReadListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.yyh.common.excel_pay.*;
import com.yyh.common.excel_pay.xinlibo.ThirdOrder;
import com.yyh.common.excel_pay.xinlibo.aipark.AiparkOrder;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;

public class XlbOrderListerner implements ReadListener<XlbOrder> {

    private Map<Key, Map<OrderKey, ThirdOrder>> thirdPayMap = PayContext.thirdPayMap;

    private Map<Key /** date */,TotalOrder> totalOrderMap = PayContext.totalOrderMap;

    private Integer terminalId;

    private Integer count = 0;

    public  XlbOrderListerner(Integer terminalId) {
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
    public void invoke(XlbOrder order, AnalysisContext analysisContext) {
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