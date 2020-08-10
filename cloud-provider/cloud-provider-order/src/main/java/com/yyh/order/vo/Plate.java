package com.yyh.order.vo;

import com.yyh.common.constants.PlateColorConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
public class Plate {

    public static final String WU_CHE_PAI = "无车牌";

    private String plateNumber;

    private Integer plateColor;

    /***
     *
     * @param plateNumber
     * @param plateColor
     * @see com.yyh.common.constants.PlateColorConstant
     */
    public Plate(String plateNumber, Integer plateColor) {
        checkPlateNumber(plateNumber);
        this.plateNumber = plateNumber;
        if (plateColor == null) {
            this.plateColor = PlateColorConstant.BLUE_COLOR;
        } else {
            this.plateColor = plateColor;
        }
    }

    private void checkPlateNumber(String plateNumber) {
        if (StringUtils.isEmpty(plateNumber)
            || plateNumber.length() < 3) {
            throw new RuntimeException("车牌号格式不正确");
        }
    }


    /**
     * 判断车牌是否是无牌车
     * @return
     */
    public boolean isWuchepai() {
        if (plateNumber.equals(WU_CHE_PAI)) {
            return true;
        } else {
            return false;
        }
    }



}
