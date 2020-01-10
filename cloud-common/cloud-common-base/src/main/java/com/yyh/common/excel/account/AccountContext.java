package com.yyh.common.excel.account;

import com.yyh.common.excel.account.alipay.AlipayAccountModel;
import com.yyh.common.excel.account.local.CodeKey;
import com.yyh.common.excel.account.local.LocalAccountModel;
import com.yyh.common.excel.account.local.LocalKey;
import com.yyh.common.excel.account.wechat.WechatAccountModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountContext {

    public static final String ACCOUNT_PATH = "C:\\Users\\Administrator\\Desktop\\对账差异\\";

    /** 微信明细 key date  key orderCode */
    public static Map<String, Map<String, WechatAccountModel>> wechatAccountModelMap = new HashMap<>();

    /** 微信按日汇总数据 key date */
    public static Map<String, AccountTotalModel> wechatTotalMoneyMap = new HashMap<>();

    /** 支付宝明细 key date  key orderCode */
    public static Map<String, Map<String, AlipayAccountModel>> alipayAccountModelMap = new HashMap<>();

    /** 支付宝按日汇总数据 key date */
    public static Map<String, AccountTotalModel> alipayTotalMoneyMap = new HashMap<>();

    /**  退款明细 key orderCode */
    public static Map<String, WechatAccountModel> wechatRefundOrderMap = new HashMap<>();

    /** 本地明细 key date  key codekey */
    public static Map<LocalKey, Map<CodeKey, LocalAccountModel>> localAccountModelMap = new HashMap<>();

    /** 按日汇总数据 key date */
    public static Map<LocalKey, AccountTotalModel> localTotalMoneyMap = new HashMap<>();

    /** 差异明细集合 */
    public static List<DiffAccountModel> diffAccountModelList = new ArrayList<>();

    /** 按天汇总集合 */
    public static List<DiffTotalAccountModel> diffAccountTotalList = new ArrayList<>();

}
