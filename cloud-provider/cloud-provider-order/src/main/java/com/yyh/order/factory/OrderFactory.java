package com.yyh.order.factory;

import com.yyh.common.base.ID;
import com.yyh.common.factory.IDFactory;
import com.yyh.common.util.SnowFlakeStrategy;
import com.yyh.order.domain.Order;
import com.yyh.order.vo.AreaInfo;
import com.yyh.order.vo.OrderCode;
import com.yyh.order.vo.Plate;

import java.util.Date;

public class OrderFactory {

    public static final OrderCode createOrderCode() {
        return new OrderCode(SnowFlakeStrategy.getPrimaryId().toString());
    }

    public static final Order createOrder(ID parkRecordId, AreaInfo areaInfo, Date entryTime, Plate plate) {
        Order order = new Order();
        order.setId(IDFactory.createID());
        order.setOrderCode(createOrderCode());
        order.setParkRecordId(parkRecordId);
        order.setEntryTime(entryTime);
        order.setPlate(plate);
        order.setAreaInfo(areaInfo);
        return order;
    }
}
