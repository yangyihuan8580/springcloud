package com.yyh.order.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class AreaInfo {

    private Long terminalId;

    private String terminalName;

    private Long parkingAreaId;

    private String parkingAreaName;

    private Long crossId;

    private String crossName;


    /**
     * 航站楼-场区-通道拼接
     * @return
     */
    public String getAreaInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(terminalName).append("-").append(parkingAreaName);
        if (StringUtils.isNotEmpty(crossName)) {
            stringBuilder.append("-").append(crossName);
        }
        return stringBuilder.toString();
    }
}
