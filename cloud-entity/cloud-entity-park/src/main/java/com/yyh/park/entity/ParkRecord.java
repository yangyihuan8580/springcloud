package com.yyh.park.entity;

import com.yyh.common.base.SuperVO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;

@Data
@Document(indexName = "park", type="parkRecord", shards = 3)
public class ParkRecord extends SuperVO {

    @Field
    private String plateNumber;

    @Field
    private String carColor;

    private Long parkId;

    private Date entryTime;

    private Date exitTime;

    private String inCross;

    private String outCross;

    @Field
    private Long status;
}
