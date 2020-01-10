package com.yyh.common.excel.account.wechat;

import com.alibaba.excel.EasyExcel;
import com.yyh.common.excel.account.AccountContext;
import com.yyh.common.excel.account.AccountTotalModel;
import com.yyh.common.excel.account.DiffAccountModel;
import com.yyh.common.excel.account.DiffTotalAccountModel;
import com.yyh.common.excel.account.local.CodeKey;
import com.yyh.common.excel.account.local.LocalAccountModel;
import com.yyh.common.excel.account.local.LocalKey;
import com.yyh.common.excel.account.local.PayTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WechatUtils {

    public static String strSub(String s) {
        if (StringUtils.isNotEmpty(s) && s.startsWith("`")) {
            return  s.substring(1);
        }
        return s;
    }

    public static void readWechatAccountExcel(String wechatAccountPath) {
        try {
            EasyExcel.read(new FileInputStream(wechatAccountPath), WechatAccountModel.class, new WechatAccountListener()).sheet(0).doRead();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void batchReadWechatAccountExcel(Set<String> paths) {
        if (CollectionUtils.isNotEmpty(paths)) {
            for (String path : paths) {
                readWechatAccountExcel(path);
            }
        }
    }


    public static List<DiffAccountModel> diffAccountModelList = AccountContext.diffAccountModelList;

    public static List<DiffTotalAccountModel> diffAccountTotalList = AccountContext.diffAccountTotalList;

    public static void diffWechatAccount(Integer terminalId) {
        Map<String, Map<String, WechatAccountModel>> wechatAccountModelMap = AccountContext.wechatAccountModelMap;
        Map<String, AccountTotalModel> wechatTotalMoneyMap = AccountContext.wechatTotalMoneyMap;
        Map<LocalKey, Map<CodeKey, LocalAccountModel>> localAccountModelMap = AccountContext.localAccountModelMap;
        Map<LocalKey, AccountTotalModel> localTotalMoneyMap = AccountContext.localTotalMoneyMap;
        Iterator<Map.Entry<String, Map<String, WechatAccountModel>>> iterator = wechatAccountModelMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Map<String, WechatAccountModel>> next = iterator.next();
            String date = next.getKey();
            /** 先比对总金额和笔数 */
            AccountTotalModel wechatTotalModel = wechatTotalMoneyMap.get(date);
            AccountTotalModel localTotalModel = localTotalMoneyMap.get(new LocalKey(date, PayTypeEnum.WECHAT.getPayType(), terminalId));
            diffAccountTotalList.add(new DiffTotalAccountModel(localTotalModel, wechatTotalModel, PayTypeEnum.WECHAT, terminalId));
            if (wechatTotalModel.equals(localTotalModel)) {
                System.out.println(localTotalModel.getDate() + "  数据一致");
            } else {
                Map<CodeKey, LocalAccountModel> localAccount = localAccountModelMap.get(new LocalKey(date, PayTypeEnum.WECHAT.getPayType(), terminalId));
                if (localAccount == null) {
                    System.out.println("本地不存在 " + date + " 的数据");
                    continue;
                }
                Map<String, WechatAccountModel> wechatAccount = next.getValue();
                if (wechatAccount != null) {
                    Iterator<Map.Entry<String, WechatAccountModel>> iterator1 = wechatAccount.entrySet().iterator();
                    while (iterator1.hasNext()) {
                        Map.Entry<String, WechatAccountModel> we = iterator1.next();
                        WechatAccountModel wechatAccountModel = we.getValue();
                        CodeKey key = new CodeKey(we.getKey(), wechatAccountModel.getWechatPayCode());
                        if (localAccount.containsKey(key)) {
                            LocalAccountModel localAccountModel = localAccount.get(key);
                            /** 存在订单 */
                            if (wechatAccountModel.getOrderMoney().compareTo(localAccountModel.getOrderMoney()) == 0) {

                            } else {
                                /** 金额不一致，需要记录 */
                                diffAccountModelList.add(new DiffAccountModel(wechatAccountModel, localAccountModel, "金额不一致", terminalId));
                            }
                        } else {
                            /** 不存在订单，本地少记录 */
                            diffAccountModelList.add(new DiffAccountModel(wechatAccountModel, null, "本地少记录", terminalId));
                        }
                    }
                } else {
                    System.out.println("微信不存在 " + date + " 的数据");
                }
            }
        }

        /** 写入差异明细 */
        if (CollectionUtils.isNotEmpty(diffAccountModelList)) {
            EasyExcel.write(new File("C:\\Users\\Administrator\\Desktop\\测试\\T" + terminalId + "微信差异明细.xlsx"))
                    .sheet(0, "差异明细")
                    .head(DiffAccountModel.class)
                    .doWrite(diffAccountModelList);
        }
        /** 写入汇总 */
        if (CollectionUtils.isNotEmpty(diffAccountTotalList)) {
            EasyExcel.write(new File("C:\\Users\\Administrator\\Desktop\\测试T" + terminalId + "微信汇总数据.xlsx"))
                    .sheet(1, "汇总数据")
                    .head(DiffTotalAccountModel.class)
                    .doWrite(diffAccountTotalList);
        }
    }

}
