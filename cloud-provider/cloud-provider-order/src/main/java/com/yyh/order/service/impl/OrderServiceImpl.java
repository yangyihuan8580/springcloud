package com.yyh.order.service.impl;

import com.yyh.order.domain.Order;
import com.yyh.order.enums.PayTypeEnum;
import com.yyh.order.process.OrderResultProcessService;
import com.yyh.order.repository.OrderRepository;
import com.yyh.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderResultProcessService orderResultProcessService;

    @Override
    public void saveOrder(Order order) {
        order = orderRepository.save(order);

        orderResultProcessService.orderResultProcess(order);


    }

    @Override
    public List<PayTypeEnum> queryPayTypeList() {
        return Arrays.asList(PayTypeEnum.values());
    }


}
