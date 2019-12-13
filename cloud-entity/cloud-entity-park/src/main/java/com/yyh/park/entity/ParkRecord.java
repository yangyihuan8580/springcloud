package com.yyh.park.entity;

import com.yyh.common.base.SuperVO;
import lombok.Data;

import java.util.Date;

@Data
public class ParkRecord extends SuperVO {

    private Long parkRecordId;

    private String plateNumber;

    private String carColor;

    private Long parkId;

    private Date entryTime;

    private Date exitTime;

    private String inCross;

    private String outCross;

    private Long status;
}
