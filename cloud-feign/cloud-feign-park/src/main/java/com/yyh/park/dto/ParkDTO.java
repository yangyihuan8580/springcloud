package com.yyh.park.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "车场实体")
public class ParkDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车场id")
    private Long id;

    @NotBlank(message = "车场名称不能为空")
    @ApiModelProperty(value = "车场名称")
    private String parkName;

    @NotBlank(message = "车场编号不能为空")
    @ApiModelProperty(value = "车场编号")
    private String parkCode;

    @NotBlank(message = "地址不能为空")
    @ApiModelProperty(value = "车场地址")
    private String address;

}
