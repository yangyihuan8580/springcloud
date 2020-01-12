package com.yyh.common.excel.account.alipay;

import com.alibaba.excel.EasyExcel;
import com.yyh.common.excel.account.AccountContext;
import com.yyh.common.excel.account.AccountTotalModel;
import com.yyh.common.excel.account.DiffAccountModel;
import com.yyh.common.excel.account.DiffTotalAccountModel;
import com.yyh.common.excel.account.local.CodeKey;
import com.yyh.common.excel.account.local.LocalAccountModel;
import com.yyh.common.excel.account.local.LocalKey;
import com.yyh.common.excel.account.local.PayTypeEnum;
import com.yyh.common.excel.account.wechat.WechatAccountModel;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.yyh.common.excel.account.AccountContext.ACCOUNT_PATH;

public class AlipayUtils {



    public static List<DiffAccountModel> diffAccountModelList = AccountContext.diffAccountModelList;

    public static List<DiffTotalAccountModel> diffAccountTotalList = AccountContext.diffAccountTotalList;


    public static void batchReadAlipayAccountExcel(Set<String> paths) {
        if (CollectionUtils.isNotEmpty(paths)) {
            for (String path : paths) {
                readAlipayAccountExcel(path);
            }
        }
    }

    public static void readAlipayAccountExcel(String alipayAccountPath) {
        try {
            EasyExcel.read(new FileInputStream(alipayAccountPath), AlipayAccountModel.class, new AlipayAccountListener()).sheet(0).doRead();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void diffAlipayAccount(Integer terminalId) {
        Map<String, Map<String, AlipayAccountModel>> alipayAccountModelMap = AccountContext.alipayAccountModelMap;
        Map<String, AccountTotalModel> alipayTotalMoneyMap = AccountContext.alipayTotalMoneyMap;
        Map<LocalKey, Map<CodeKey, LocalAccountModel>> localAccountModelMap = AccountContext.localAccountModelMap;
        Map<LocalKey, AccountTotalModel> localTotalMoneyMap = AccountContext.localTotalMoneyMap;

        Iterator<Map.Entry<String, Map<String, AlipayAccountModel>>> iterator = alipayAccountModelMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Map<String, AlipayAccountModel>> next = iterator.next();
            String date = next.getKey();
            /** 先比对总金额和笔数 */
            AccountTotalModel alipayTotalModel = alipayTotalMoneyMap.get(date);
            AccountTotalModel localTotalModel = localTotalMoneyMap.get(new LocalKey(date, PayTypeEnum.ALIPAY.getPayType(), terminalId));
            diffAccountTotalList.add(new DiffTotalAccountModel(localTotalModel, alipayTotalModel, PayTypeEnum.ALIPAY, terminalId));
            if (alipayTotalModel.equals(localTotalModel)) {
                System.out.println(localTotalModel.getDate() + "  数据一致");
            } else {
                Map<CodeKey, LocalAccountModel> localAccount = localAccountModelMap.get(new LocalKey(date, PayTypeEnum.ALIPAY.getPayType(), terminalId));
                if (localAccount == null) {
                    System.out.println("本地不存在 " + date + " 的数据");
                    continue;
                }
                Map<String, AlipayAccountModel> alipayAccount = next.getValue();
                if (alipayAccount != null) {
                    Iterator<Map.Entry<String, AlipayAccountModel>> iterator1 = alipayAccount.entrySet().iterator();
                    while (iterator1.hasNext()) {
                        Map.Entry<String, AlipayAccountModel> we = iterator1.next();
                        AlipayAccountModel alipayAccountModel = we.getValue();
                        CodeKey codeKey = new CodeKey(alipayAccountModel.getOrderCode(), alipayAccountModel.getAlipayPayCode());
                        if (localAccount.containsKey(codeKey)) {
                            LocalAccountModel localAccountModel = localAccount.get(codeKey);
                            /** 存在订单 */
                            if (alipayAccountModel.getOrderMoney().compareTo(localAccountModel.getOrderMoney()) == 0) {

                            } else {
                                /** 金额不一致，需要记录 */
                                diffAccountModelList.add(new DiffAccountModel(alipayAccountModel, localAccountModel, "金额不一致", terminalId));
                            }
                        } else {
                            /** 不存在订单，本地少记录 */
                            diffAccountModelList.add(new DiffAccountModel(alipayAccountModel, null, "本地少记录", terminalId));
                        }
                    }
                } else {
                    System.out.println("支付宝不存在 " + date + " 的数据");
                }
            }
        }

        /** 写入差异明细 */
        if (CollectionUtils.isNotEmpty(diffAccountModelList)) {
            EasyExcel.write(new File(ACCOUNT_PATH+ "T" + terminalId + "支付宝差异明细.xlsx"))
                    .sheet(0, "差异明细")
                    .head(DiffAccountModel.class)
                    .doWrite(diffAccountModelList);
        }
        /** 写入汇总 */
        if (CollectionUtils.isNotEmpty(diffAccountTotalList)) {
            EasyExcel.write(new File(ACCOUNT_PATH + "T" + terminalId + "支付宝汇总数据.xlsx"))
                    .sheet(0, "汇总数据")
                    .head(DiffTotalAccountModel.class)
                    .doWrite(diffAccountTotalList);
        }
    }
}
