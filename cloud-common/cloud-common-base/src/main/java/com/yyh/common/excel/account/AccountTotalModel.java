package com.yyh.common.excel.account;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Data
public class AccountTotalModel {

    private String date;

    private BigDecimal totalMoney;

    private Integer count;


    public AccountTotalModel(String date) {
        this.date = date;
        totalMoney = BigDecimal.ZERO;
        count = 0;
    }

    public void add(BigDecimal money) {
        totalMoney = totalMoney.add(money);
        count = count + 1;
    }


    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof AccountTotalModel)) {
            return false;
        }
        AccountTotalModel accountTotalModel = (AccountTotalModel)o;
        if (accountTotalModel.getCount().equals(this.count)
                && accountTotalModel.getDate().equals(this.date)
                && accountTotalModel.getTotalMoney().compareTo(this.totalMoney) == 0
            ) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (StringUtils.isEmpty(date) ? 0 : date.hashCode());
        result = 31 * result + (count == null ? 0 : count.hashCode());
        result = 31 * result + (totalMoney == null ? 0 : totalMoney.hashCode());
        return result;
    }

//    public static void main(String[] args) {
//        AccountTotalModel accountTotalModel = new AccountTotalModel("2019-12-20");
//        accountTotalModel.setCount(20);
//        accountTotalModel.setTotalMoney(BigDecimal.ONE);
//        AccountTotalModel accountTotalModel1 = new AccountTotalModel("2019-12-20");
//        accountTotalModel1.setCount(21);
//        accountTotalModel1.setTotalMoney(BigDecimal.ONE);
//        System.out.println(accountTotalModel.equals(accountTotalModel1));
//    }

}
