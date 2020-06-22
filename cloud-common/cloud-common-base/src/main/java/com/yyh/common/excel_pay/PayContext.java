package com.yyh.common.excel_pay;

import com.yyh.common.excel_pay.local.Order;
import com.yyh.common.excel_pay.xinlibo.ThirdOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PayContext {

    public static final Map<Key, Map<OrderKey , Order>> localPayMap = new HashMap<>();

    public static final Map<String, OrderKey> localOrderMap = new HashMap<>();

    public static final Map<Key, Map<OrderKey , ThirdOrder>> thirdPayMap = new HashMap<>();

    public static final Map<Key, TotalOrder> totalOrderMap = new HashMap<>();

    public static final List<Order> repeatOrderList = new ArrayList<>();

    public static final String BASE_PATH = "D:\\zhht\\对账\\电子支付\\";

    public static final String LOCAL_PATH = BASE_PATH + "互通原数据";

    public static final String THIRD_AIPARK_PATH = BASE_PATH + "辛利泊数据\\互通报表2019";

    public static final String THIRD_XLB_PATH = BASE_PATH + "辛利泊数据\\新利泊表2019";

    public static final String WECHAT_PATH = BASE_PATH + "9-11月差异天数明细\\微信";

    public static final String ALIPAY_PATH = BASE_PATH + "9-11月差异天数明细\\支付宝";


}