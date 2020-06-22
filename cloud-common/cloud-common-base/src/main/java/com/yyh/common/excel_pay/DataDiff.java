package com.yyh.common.excel_pay;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer;
import com.yyh.common.excel.account.DiffAccountModel;
import com.yyh.common.excel.account.local.PayTypeEnum;
import com.yyh.common.excel_pay.local.Order;
import com.yyh.common.excel_pay.xinlibo.ThirdOrder;
import com.yyh.common.util.SnowFlakeStrategy;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.*;

import static com.yyh.common.excel.account.AccountContext.ACCOUNT_PATH;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yyh
 * \* Date: 2020/3/23
 * \* Time: 14:12
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class DataDiff {

    private static Map<Key,Map<OrderKey, Order>> localPayMap = PayContext.localPayMap;

    private static Map<Key,Map<OrderKey, ThirdOrder>> thirdPayMap = PayContext.thirdPayMap;

    private static Map<String, OrderKey>  localTotalOrderMap =  PayContext.localOrderMap;

    public static void diff() throws ParseException {
        List<ThirdOrder> list = new ArrayList<>();
        for (Map.Entry<Key, Map<OrderKey, ThirdOrder>> dataMap : thirdPayMap.entrySet()) {
            Key key = dataMap.getKey();
            Map<OrderKey, ThirdOrder> thirdOrderMap = dataMap.getValue();
            Map<OrderKey, Order> map = new HashMap<>();
            map.putAll(localPayMap.get(key));
            Map<OrderKey, Order> afterlocalOrderMap = localPayMap.get(new Key(key.getTerminalId(), addDate(key.getPayDate(), 1)));
            Map<OrderKey, Order> beforelocalOrderMap = localPayMap.get(new Key(key.getTerminalId(), addDate(key.getPayDate(), -1)));
            if (afterlocalOrderMap != null) {
                map.putAll(afterlocalOrderMap);
            }
            if (beforelocalOrderMap != null) {
                map.putAll(beforelocalOrderMap);
            }
            if (thirdOrderMap != null && map != null) {
                for (Map.Entry<OrderKey, ThirdOrder> thirdOrderEntry : thirdOrderMap.entrySet()) {

                    Order order = map.get(thirdOrderEntry.getKey());
                    if (order == null) {
                        OrderKey orderKey = localTotalOrderMap.get(thirdOrderEntry.getKey().getOrderCode());
                        if (orderKey == null) {
                            thirdOrderEntry.getValue().setRemark("互通少数据");
                            PayContext.totalOrderMap.get(key).addLessMoney(thirdOrderEntry.getValue().getNeedMoney());
                            list.add(thirdOrderEntry.getValue());
                            exportInsertSql(thirdOrderEntry.getValue());
                        } else {
                            map.get(orderKey).setD(1);
                        }
                    } else if (!order.getNeedMoney().equals(thirdOrderEntry.getValue().getNeedMoney())) {
                        thirdOrderEntry.getValue().setRemark("金额不一致 orderCode : " + order.getOrderCode() + " localMoney:" + order.getNeedMoney() + " thirdMoney:" + thirdOrderEntry.getValue().getNeedMoney());
                        list.add(thirdOrderEntry.getValue());
                        order.setD(1);
                    } else if (!order.getPayDate().equals(thirdOrderEntry.getValue().getPayDate())) {
                        thirdOrderEntry.getValue().setOrderId(order.getOrderId());
                        thirdOrderEntry.getValue().setRemark("订单不在同一天 localDate :" + order.getPayDate() + " thirdDate:" + thirdOrderEntry.getValue().getPayDate());
                        list.add(thirdOrderEntry.getValue());
                        PayContext.totalOrderMap.get(key).addLastDayMoney(thirdOrderEntry.getValue().getNeedMoney());
                        PayContext.totalOrderMap.get(new Key(order.getTerminalId(), order.getPayDate())).addLastDayMoney(thirdOrderEntry.getValue().getNeedMoney().negate());
                        order.setD(1);
                        exportUpdateSql(order.getOrderId(), thirdOrderEntry.getValue().getPayDate(), order.getPayDate());
                    } else if (thirdOrderEntry.getValue().getCount() > 1) {
                        thirdOrderEntry.getValue().setRemark("新利泊存在重复缴费订单，确认互通是否存在");
                        list.add(thirdOrderEntry.getValue());
                        order.setD(1);
                    } else {
                        order.setD(1);
                    }
                    thirdOrderEntry.getValue().setCount(thirdOrderEntry.getValue().getCount() - 1);
                }
            }
        }

        for (Map.Entry<Key, Map<OrderKey, Order>> dataMap : localPayMap.entrySet()) {
            for (Map.Entry<OrderKey, Order> orderMap : dataMap.getValue().entrySet()) {
                if (orderMap.getValue().getD().equals(0)) {
                    Order value = orderMap.getValue();
                    ThirdOrder thirdOrder = new ThirdOrder();
                    BeanUtils.copyProperties(value, thirdOrder);
                    thirdOrder.setExitTime(value.getPayTime());
                    thirdOrder.setOrderMoney(value.getOrderMoney());
                    thirdOrder.setRemark("互通多数据，需要删除");
                    list.add(thirdOrder);
                    PayContext.totalOrderMap.get(value.getKey()).addMoreMoney(value.getNeedMoney());
                }
            }
        }

        EasyExcel.write(new File(PayContext.BASE_PATH + "差异明细.xlsx"))
                .sheet(0, "差异明细")
                .head(ThirdOrder.class)
                .doWrite(list);
        EasyExcel.write(new File(PayContext.BASE_PATH + "重复缴费.xlsx"))
                .sheet(0, "重复缴费")
                .head(Order.class)
                .doWrite(PayContext.repeatOrderList);
        EasyExcel.write(new File(PayContext.BASE_PATH + "汇总数据.xlsx"))
                .sheet(0, "汇总数据")
                .head(TotalOrder.class)
                .doWrite(new ArrayList<>(PayContext.totalOrderMap.values()));
    }


    private static void exportUpdateSql(String orderId, String thirdDate, String localDate) throws ParseException {
        StringBuilder sb = new StringBuilder();
        if (DateUtils.parseDate(thirdDate, com.alibaba.excel.util.DateUtils.DATE_FORMAT_10).compareTo(DateUtils.parseDate(localDate, com.alibaba.excel.util.DateUtils.DATE_FORMAT_10)) == 1) {
            thirdDate = thirdDate.concat(" 00:00:10");
        } else {
            thirdDate = thirdDate.concat(" 23:59:10");
        }
        sb.append("update tob_order set pay_time = ").append("\"").append(thirdDate).append("\"")
                .append("  where orderId = ").append(orderId).append(";");
        try{
            File file = new File(PayContext.BASE_PATH + "修改支付时间.sql");
            FileOutputStream fos = null;
            if(!file.exists()){
                file.createNewFile();//如果文件不存在，就创建该文件
                fos = new FileOutputStream(file);//首次写入获取
            }else{
                //如果文件已存在，那么就在文件末尾追加写入
                fos = new FileOutputStream(file,true);//这里构造方法多了一个参数true,表示在文件末尾追加写入
            }
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");//指定以UTF-8格式写入文件
            osw.write(sb.toString());
            //每写入一个Map就换一行
            osw.write("\r\n");
            //写入完成关闭流
            osw.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void exportInsertSql(ThirdOrder thirdOrder) {
        StringBuilder sb = new StringBuilder();
        Integer lane = Integer.valueOf(thirdOrder.getOutCross());
        if (lane > 100) {
            lane = lane - 100;
        }
        CrossEnums crossInfo = CrossEnums.getCrossInfoByLane(lane, thirdOrder.getTerminalId());
        Integer payType = 0;
        if (thirdOrder.getPayType().equals("微信提前支付")) {
            payType = 101;
        } else if (thirdOrder.getPayType().equals("微信付款码")) {
            payType = 105;
        } else if (thirdOrder.getPayType().equals("微信无感")) {
            payType = 103;
        } else if (thirdOrder.getPayType().equals("支付宝提前支付")) {
            payType = 102;
        } else if (thirdOrder.getPayType().equals("支付宝付款码")) {
            payType = 106;
        } else if (thirdOrder.getPayType().equals("支付宝无感")) {
            payType = 104;
        }
        String source = "";
        if (thirdOrder.getPayType().contains("提前")) {
            source = "提前支付H5页面";
        } else {
            source = "岗亭(人工)";
        }
        sb.append("insert into tob_order (ORDER_ID, ORDER_CODE,PARK_RECORD_ID, ENTRY_TIME,EXIT_TIME,PREDICT_EXIT_TIME,ORDER_MONEY,ORDER_STATUS" +
                ",CREATE_TIME,PARK_MSG_STATUS \n" +
                ",D, TS,PLATE_NUMBER,RECORD_CODE, CAR_TYPE,CROSS_ID,CROSS_NAME,PARKINGAREA_ID,PARKINGAREA_NAME,TERMINAL_ID," +
                "TERMINAL_NAME,COUPON_MONEY,\n" +
                "  COUPON_CHANNEL, NEED_MONEY,PAY_TIME,PAY_TYPE,SOURCE,SOURCE_CODE,COLLECTOR_ORDER,UPLOAD_SOURCE) values (")
                .append(SnowFlakeStrategy.getPrimaryId()).append(",")
                .append("\"").append(thirdOrder.getOrderCode()).append("\"").append(",")
                .append(SnowFlakeStrategy.getPrimaryId()).append(",")
                .append("\"").append(com.alibaba.excel.util.DateUtils.format(thirdOrder.getEntryTime(), com.alibaba.excel.util.DateUtils.DATE_FORMAT_19)).append("\"").append(",")
                .append("\"").append(com.alibaba.excel.util.DateUtils.format(thirdOrder.getExitTime(), com.alibaba.excel.util.DateUtils.DATE_FORMAT_19)).append("\"").append(",")
                .append("\"").append(com.alibaba.excel.util.DateUtils.format(thirdOrder.getExitTime(), com.alibaba.excel.util.DateUtils.DATE_FORMAT_19)).append("\"").append(",")
                .append(thirdOrder.getOrderMoney()).append(",")
                .append(1).append(",")
                .append("\"").append(com.alibaba.excel.util.DateUtils.format(thirdOrder.getExitTime(), com.alibaba.excel.util.DateUtils.DATE_FORMAT_19)).append("\"").append(",")
                .append(1).append(",")
                .append(0).append(",")
                .append("\"").append(com.alibaba.excel.util.DateUtils.format(thirdOrder.getExitTime(), com.alibaba.excel.util.DateUtils.DATE_FORMAT_19)).append("\"").append(",")
                .append("\"").append(thirdOrder.getPlateNumber()).append("\"").append(",")
                .append("\"").append(UUID.randomUUID().toString()).append("\"").append(",")
                .append(1).append(",")
                .append(crossInfo.getCrossId()).append(",")
                .append("\"").append(crossInfo.getCrossName()).append("\"").append(",")
                .append(crossInfo.getParkingareaId()).append(",")
                .append("\"").append(crossInfo.getParkingareaName()).append("\"").append(",")
                .append(crossInfo.getTerminalId()).append(",")
                .append("\"").append(crossInfo.getTerminalName()).append("\"").append(",")
                .append(thirdOrder.getOrderMoney().subtract(thirdOrder.getNeedMoney())).append(",")
                .append(0).append(",")
                .append(thirdOrder.getNeedMoney()).append(",")
                .append("\"").append(com.alibaba.excel.util.DateUtils.format(thirdOrder.getExitTime(), com.alibaba.excel.util.DateUtils.DATE_FORMAT_19)).append("\"").append(",")
                .append(payType).append(",")
                .append("null").append(",")
                .append("\"").append(source).append("\"").append(",")
                .append(0).append(",")
                .append(2).append(");");
        String sql = sb.toString();


        try{
            File file = new File(PayContext.BASE_PATH + "缺失数据新增.sql");
            FileOutputStream fos = null;
            if(!file.exists()){
                file.createNewFile();//如果文件不存在，就创建该文件
                fos = new FileOutputStream(file);//首次写入获取
            }else{
                //如果文件已存在，那么就在文件末尾追加写入
                fos = new FileOutputStream(file,true);//这里构造方法多了一个参数true,表示在文件末尾追加写入
            }
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");//指定以UTF-8格式写入文件
            osw.write(sql);
            //每写入一个Map就换一行
            osw.write("\r\n");
            //写入完成关闭流
            osw.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String addDate(String payDate, int amount) throws ParseException {
        return com.alibaba.excel.util.DateUtils.format(DateUtils.addDays(com.alibaba.excel.util.DateUtils.parseDate(payDate, com.alibaba.excel.util.DateUtils.DATE_FORMAT_10), amount)).substring(0,10);
    }

    public static void main(String[] args) throws ParseException {
//        System.out.println(DataDiff.addDate("2019-01-01",-1));
        exportUpdateSql("1111","2019-12-02","2019-12-01");
    }


    public static List<ThirdOrder> aaa() throws ParseException {
        List<ThirdOrder> list = new ArrayList<>();
        for (Map.Entry<Key, Map<OrderKey, Order>> dataMap : localPayMap.entrySet()) {
            Key key = dataMap.getKey();
            Map<OrderKey, Order> localOrderMap = dataMap.getValue();
            Map<OrderKey, ThirdOrder> thirdOrderMap = thirdPayMap.get(key);
            Map<OrderKey, ThirdOrder> afterlocalOrderMap = thirdPayMap.get(new Key(key.getTerminalId(), addDate(key.getPayDate(), 1)));
            Map<OrderKey, ThirdOrder> beforelocalOrderMap = thirdPayMap.get(new Key(key.getTerminalId(), addDate(key.getPayDate(), -1)));
            if (afterlocalOrderMap != null) {
                System.out.println("after:" + afterlocalOrderMap.size());
                thirdOrderMap.putAll(afterlocalOrderMap);
            }
            if (beforelocalOrderMap != null) {
                System.out.println("before:" + beforelocalOrderMap.size());
                thirdOrderMap.putAll(beforelocalOrderMap);
            }
            if (thirdOrderMap != null && localOrderMap != null) {
                for (Map.Entry<OrderKey, Order> localOrderEntry : localOrderMap.entrySet()) {
                    ThirdOrder thirdOrder = thirdOrderMap.get(localOrderEntry.getKey());
                    if (thirdOrder == null) {
                        thirdOrder = new ThirdOrder();
                        thirdOrder.setRemark("互通多，新利泊少");
                        thirdOrder.setPayType(localOrderEntry.getValue().getPayType().toString());
                        thirdOrder.setOrderMoney(localOrderEntry.getValue().getOrderMoney());
                        BeanUtils.copyProperties(localOrderEntry.getValue(), thirdOrder);
                        list.add(thirdOrder);
                    } else if (!thirdOrder.getPayDate().equals(localOrderEntry.getValue().getPayDate())) {
                        thirdOrder.setRemark("订单不在同一天 localDate :" + localOrderEntry.getValue().getPayDate() + " thirdDate:" + thirdOrder.getPayDate());
                        list.add(thirdOrder);
                    }
                }
            }
        }
        return list;
    }
}