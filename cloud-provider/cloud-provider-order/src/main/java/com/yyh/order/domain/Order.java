package com.yyh.order.domain;

import com.yyh.common.base.ID;
import com.yyh.order.enums.PayTypeEnum;
import com.yyh.order.vo.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Order implements Serializable {

    private ID id;

    private OrderCode orderCode;

    private Money orderMoney;

    private Money couponMoney;

    private Money needMoney;

    private Integer orderStatus;

    private Date payTime;

    private PayTypeEnum payTypeEnum;

    private ID parkRecordId;

    private Plate plate;

    private String cashier;

    private AreaInfo areaInfo;

    private Date entryTime;

    private Date exitTime;

    private OrderExpand orderExpand;
}
