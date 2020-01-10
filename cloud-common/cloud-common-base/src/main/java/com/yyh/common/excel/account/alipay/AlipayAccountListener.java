package com.yyh.common.excel.account.alipay;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AbstractIgnoreExceptionReadListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSON;
import com.yyh.common.excel.account.AccountContext;
import com.yyh.common.excel.account.AccountTotalModel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class AlipayAccountListener extends AbstractIgnoreExceptionReadListener<AlipayAccountModel> {

    private Map<String, Map<String, AlipayAccountModel>> alipayAccountModelMap = AccountContext.alipayAccountModelMap;
    private Map<String, AccountTotalModel> alipayTotalMoneyMap = AccountContext.alipayTotalMoneyMap;


    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        System.out.println("打印请求头" + JSON.toJSONString(headMap));
    }

    @Override
    public void invoke(AlipayAccountModel data, AnalysisContext context) {
        System.out.println("接收到数据" + JSON.toJSONString(data));
        if (data != null) {
            /** 按天分开 */
            String date = DateUtils.format(data.getPayTime(), "yyyy-MM-dd");
            if (StringUtils.isNotEmpty(date)) {
                Map<String, AlipayAccountModel> wa = alipayAccountModelMap.get(date);
                if (wa == null) {
                    alipayAccountModelMap.put(date, new HashMap<>());
                    alipayTotalMoneyMap.put(date, new AccountTotalModel(date));
                }
                /** 每一天 key : orderCode  value : entity */
                /** 退款数据 */
                alipayAccountModelMap.get(date).put(data.getOrderCode(), data);
                alipayTotalMoneyMap.get(date).add(data.getOrderMoney());
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("支付宝汇总统计" + JSON.toJSONString(alipayTotalMoneyMap));
        System.out.println("支付宝统计完毕");

    }

}
