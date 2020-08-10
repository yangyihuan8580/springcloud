package com.yyh.order.repository;

import com.yyh.common.base.BaseRepository;
import com.yyh.common.base.ID;
import com.yyh.common.util.SnowFlakeStrategy;
import com.yyh.order.converter.OrderConverter;
import com.yyh.order.dao.OrderDao;
import com.yyh.order.dbo.OrderDO;
import com.yyh.order.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements BaseRepository<ID, Order> {

    private OrderConverter orderConverter = OrderConverter.INSTANCE;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Order save(Order order) {
        OrderDO orderDO = orderConverter.domain2do(order);
        if (orderDO.getId() != null) {
            /** 数据更新 */
            orderDao.updateByPrimaryKeySelective(orderDO);
        } else {
            /** 数据新增 */
            orderDO.setId(SnowFlakeStrategy.getPrimaryId());
            orderDao.addSelective(orderDO);
        }
        order.setId(new ID(orderDO.getId()));
        return order;
    }

    @Override
    public void remove(ID id) {
        int i = orderDao.deleteByIdFalse(id.getValue());
    }

    @Override
    public Order queryOne(ID id) {
        OrderDO orderBO = orderDao.selectByPrimaryKey(id.getValue());
        return orderConverter.do2domain(orderBO);
    }


}
