package com.yyh.common.excel.account.local;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AbstractIgnoreExceptionReadListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.yyh.common.excel.account.AccountContext;
import com.yyh.common.excel.account.AccountTotalModel;

import java.util.HashMap;
import java.util.Map;

public class LocalAccountListener extends AbstractIgnoreExceptionReadListener<LocalAccountModel> {

    private Map<LocalKey, Map<CodeKey, LocalAccountModel>> localAccountModelMap = AccountContext.localAccountModelMap;
    private Map<LocalKey, AccountTotalModel> localTotalMoneyMap = AccountContext.localTotalMoneyMap;


    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        System.out.println("excel头:" + JSON.toJSONString(headMap));
    }

    @Override
    public void invoke(LocalAccountModel data, AnalysisContext context) {
//        System.out.println("接收到本地数据:" + JSON.toJSONString(data));
        LocalKey localKey = data.localKey();
        Map<CodeKey, LocalAccountModel> la = localAccountModelMap.get(localKey);
        if (la == null) {
            localAccountModelMap.put(localKey, new HashMap<>());
            localTotalMoneyMap.put(localKey, new AccountTotalModel(localKey.getDate()));
        }
        /** 每一天 key : orderCode  value : entity */
        localAccountModelMap.get(localKey).put(data.codeKey(), data);
        localTotalMoneyMap.get(localKey).add(data.getOrderMoney());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("localTotalMoneyMap" + JSON.toJSONString(localTotalMoneyMap));
        System.out.println("本地对账单导入完毕");
    }
}
