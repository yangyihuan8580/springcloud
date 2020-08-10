package com.yyh.order.vo;

import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
public class Money implements Serializable {

    private BigDecimal money;

    public Money(BigDecimal money) {
        if (money == null
            || money.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("金额不能小于0");
        }
        this.money = money;
    }

    public BigDecimal getValue() {
        return this.money;
    }


    public void setValue(BigDecimal money) {
        this.money = money;
    }
    /**
     * 返回单位是分的金额
     * @return
     */
    public int intValue() {
        return this.money.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).intValue();
    }


//    public static void main(String[] args) {
////        System.out.println(new BigDecimal(-1).compareTo(BigDecimal.ZERO) < 1);
//        Money money = new Money(new BigDecimal(2.50));
//        System.out.println(money.intValue());
//    }
}
