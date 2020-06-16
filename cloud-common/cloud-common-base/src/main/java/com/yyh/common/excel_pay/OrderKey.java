package com.yyh.common.excel_pay;

import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSON;
import com.yyh.common.excel_pay.local.Order;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Data
public class OrderKey {

    private String orderCode;

    private Date exitTime;

    private Date entryTime;

    private String plateNumber;

    private BigDecimal needMoney;

    private Integer payType;

    public OrderKey (String orderCode, String plateNumber, Date exitTime, Date entryTime, BigDecimal needMoney, Integer payType) {
        this.exitTime = exitTime;
        this.orderCode = orderCode;
        this.plateNumber = plateNumber;
        this.needMoney = needMoney;
        this.entryTime = entryTime;
        this.payType = payType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof OrderKey)) {
            return false;
        }
        OrderKey key = (OrderKey)o;
        if (key.getOrderCode().equals(orderCode)) {
            return true;
        }
        try {
            Date d1 = DateUtils.parseDate(DateUtils.format(key.getExitTime(), "yyyy-MM-dd HH:mm:00"));
            Date d2 = DateUtils.parseDate(DateUtils.format(this.exitTime, "yyyy-MM-dd HH:mm:00"));

            Date d3 = DateUtils.parseDate(DateUtils.format(key.getEntryTime(), "yyyy-MM-dd HH:mm:00"));
            Date d4 = DateUtils.parseDate(DateUtils.format(this.entryTime, "yyyy-MM-dd HH:mm:00"));
//            System.out.println(Math.abs(dateDiff(d1, d2)) <= 5);
            if ((Math.abs(dateDiff(d1, d2)) <= 5 || d3.compareTo(d4) == 0)
                    && key.getPlateNumber().equals(this.plateNumber)
                    && key.getNeedMoney().compareTo(this.needMoney) == 0
                    && key.getPayType().equals(this.payType)
                    ) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
//        result = 31 * result + (StringUtils.isEmpty(orderCode) ? 0 : orderCode.hashCode());
        try {
            result = 31 * result + (null == plateNumber || plateNumber.length() <= 3 ? 0 : plateNumber.substring(0,4).hashCode());
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
//        result = 31 * result + (entryTime == null ? 0 : entryTime.hashCode());
        return result;
    }


    public static long dateDiff(Date startTime, Date endTime) throws Exception {
        //按照传入的格式生成一个simpledateformate对象
        long nd = 1000*24*60*60;//一天的毫秒数
        long nh = 1000*60*60;//一小时的毫秒数
        long nm = 1000*60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        long diff;
        //获得两个时间的毫秒时间差异
        diff = endTime.getTime() - startTime.getTime();
//        long day = diff/nd;//计算差多少天
//        long hour = diff%nd/nh;//计算差多少小时
        long min = diff/nm;//计算差多少分钟
//        long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果
        return min ;
    }

//    public static void main(String[] args) throws Exception {
//        Map<OrderKey, String> map1 = new HashMap<>();
//        Date d1 = DateUtils.parseDate("2019-09-01 00:45:15");
//        Date d2 = DateUtils.parseDate("2019-09-01 02:49:54");
//        OrderKey o1 = new OrderKey("111", "aaa" , d1, d1, new BigDecimal(2.5), 1);
//        map1.put(o1,"111");
//
//        Map<OrderKey, String> map2 = new HashMap<>();
//        OrderKey o2 = new OrderKey("222", "aaa" , d2, d2,new BigDecimal(2.5), 1);
//        map2.put(o2,"222");
//
//        String  order = map1.get(o2);
////        System.out.println(order);
//
//        System.out.println(dateDiff(d1,d2));
//    }


    public static void main(String[] args) {
        String a = "京N89DW7";
        System.out.println(a.substring(0,4));

    }

}