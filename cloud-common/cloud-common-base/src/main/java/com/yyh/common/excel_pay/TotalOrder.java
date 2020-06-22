package com.yyh.common.excel_pay;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TotalOrder {

    @ExcelProperty(index = 0,value="航站楼ID")
    private Integer terminalId;

    @ExcelProperty(index = 1,value="日期")
    private String payDate;

    @ExcelProperty(index = 2,value="新利泊笔数")
    private Integer thirdCount = 0;

    @ExcelProperty(index = 3,value="新利泊金额")
    private BigDecimal thirdMoney = BigDecimal.ZERO;

    @ExcelProperty(index = 4,value="互通笔数")
    private Integer localCount = 0;

    @ExcelProperty(index = 5,value="互通金额")
    private BigDecimal localMoney = BigDecimal.ZERO;

    @ExcelProperty(index = 6,value="互通少款笔数")
    private Integer lessCount = 0;

    @ExcelProperty(index = 7,value="互通少款金额")
    private BigDecimal lessMoney = BigDecimal.ZERO;

    @ExcelProperty(index = 8,value="跨天笔数")
    private Integer lastDayCount = 0;

    @ExcelProperty(index = 9,value="跨天金额")
    private BigDecimal lastDayMoney = BigDecimal.ZERO;

    @ExcelProperty(index = 10,value="互通多款笔数")
    private Integer moreCount = 0;

    @ExcelProperty(index = 11,value="互通多款金额")
    private BigDecimal moreMoney = BigDecimal.ZERO;

    @ExcelProperty(index = 12,value="差异笔数")
    private Integer diffCount = 0;

    @ExcelProperty(index = 13,value="差异金额")
    private BigDecimal diffMoney = BigDecimal.ZERO;

    public TotalOrder(Integer terminalId, String payDate) {
        this.terminalId = terminalId;
        this.payDate = payDate;
    }

    public void addLocalMoney(BigDecimal localMoney) {
        this.localCount += 1;
        this.localMoney = this.localMoney.add(localMoney);
    }

    public void addThirdMoney(BigDecimal thirdMoney) {
        this.thirdCount += 1;
        this.thirdMoney = this.thirdMoney.add(thirdMoney);
    }



    public void addLastDayMoney(BigDecimal lastDayMoney) {
        if (lastDayMoney.compareTo(BigDecimal.ZERO) == 1) {
            this.lastDayCount += 1;
        } else {
            this.lastDayCount -= 1;
        }
        this.lastDayMoney = this.lastDayMoney.add(lastDayMoney);
    }

    public void addLessMoney(BigDecimal lessMoney) {
        this.lessCount += 1;
        this.lessMoney = this.lessMoney.add(lessMoney);
    }

    public void addMoreMoney(BigDecimal moreMoney) {
        this.moreCount += 1;
        this.moreMoney = this.moreMoney.add(moreMoney);
    }

    public BigDecimal getDiffMoney() {
        return this.thirdMoney.subtract(this.localMoney).subtract(this.lessMoney).subtract(this.lastDayMoney).add(moreMoney);
    }

    public Integer getDiffCount() {
        return this.thirdCount - this.localCount - this.lessCount - this.lastDayCount + this.moreCount;
    }
}