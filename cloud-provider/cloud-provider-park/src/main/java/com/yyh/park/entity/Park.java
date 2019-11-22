package com.yyh.park.entity;

import com.yyh.common.base.SuperVO;
import lombok.Data;

/**
 * 
 * <br>
 * <b>功能：</b>ParkEntity<br>
 */
@Data
public class Park extends SuperVO {
	
	private String parkName;//   车场名称
	private String parkCode;//   车场编号
	private String address;//   车场地址
}

