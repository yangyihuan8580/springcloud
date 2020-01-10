package com.yyh.common.excel.account.wechat;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AbstractIgnoreExceptionReadListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSON;
import com.yyh.common.excel.account.AccountContext;
import com.yyh.common.excel.account.AccountTotalModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WechatAccountListener extends AbstractIgnoreExceptionReadListener<WechatAccountModel> {

    private Map<String, Map<String, WechatAccountModel>> wechatAccountModelMap = AccountContext.wechatAccountModelMap;
    private Map<String, AccountTotalModel> wechatTotalMoneyMap = AccountContext.wechatTotalMoneyMap;
//    private Map<String, WechatAccountModel> wechatRefundOrderMap = AccountContext.wechatRefundOrderMap;

    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        System.out.println("表格头：" + JSON.toJSONString(headMap));
    }

    @Override
    public void invoke(WechatAccountModel data, AnalysisContext context) {
//        System.out.println("接收到数据:" + JSON.toJSONString(data));
        /** 按天分开 */
        String  date = DateUtils.format(data.getPayTime(), "yyyy-MM-dd");
        Map<String, WechatAccountModel> wa = wechatAccountModelMap.get(date);
        if (wa == null) {
            wechatAccountModelMap.put(date, new HashMap<>());
            wechatTotalMoneyMap.put(date, new AccountTotalModel(date));
        }
        /** 每一天 key : orderCode  value : entity */
        /** 退款数据 */
        if (data.getRefundMoney().compareTo(BigDecimal.ZERO) > 0) {
            /** 不处理，退款数据，单独导入 */
//            wechatRefundOrderMap.put(data.getOrderCode(), data);
        } else {
            wechatAccountModelMap.get(date).put(data.getOrderCode(), data);
            wechatTotalMoneyMap.get(date).add(data.getOrderMoney());
        }
    }



    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("汇总数据: " + JSON.toJSONString(wechatTotalMoneyMap));
        System.out.println("微信对账单处理完毕");

    }


}
