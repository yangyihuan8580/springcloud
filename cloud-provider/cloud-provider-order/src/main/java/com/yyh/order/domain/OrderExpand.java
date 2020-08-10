package com.yyh.order.domain;

import com.yyh.common.base.ID;
import lombok.Data;

/**
 * 订单相关的非核心字段
 */
@Data
public class OrderExpand {

    private ID id;

    private ID orderId;

    private Integer collectorOrder;

    private Integer uploadSource;

    private String cardNo;

    //TODO
}
