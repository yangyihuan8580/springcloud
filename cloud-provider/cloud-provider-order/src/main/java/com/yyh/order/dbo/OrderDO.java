package com.yyh.order.dbo;

import com.yyh.common.base.SuperVO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDO extends SuperVO {

    private String orderCode;

    private String plateNumber;

    private String plateColor;

    private BigDecimal orderMoney;

    private BigDecimal couponMoney;

    private BigDecimal needMoney;

    private Long parkRecordId;

    private Long terminalId;

    private String terminalName;

    private Long parkingAreaId;

    private String parkingAreaName;

    private Long crossId;

    private String crossName;
}
