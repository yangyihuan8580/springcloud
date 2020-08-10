package com.yyh.order.service;

import com.yyh.order.domain.Order;
import com.yyh.order.enums.PayTypeEnum;

import java.util.List;

public interface OrderService {

    void saveOrder(Order order);

    List<PayTypeEnum> queryPayTypeList();

}
