package com.yyh.common.excel_pay;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yyh
 * \* Date: 2020/3/23
 * \* Time: 12:58
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
public class Key {

    private Integer terminalId;

    private String payDate;

    public Key (Integer terminalId, String payDate) {
        this.terminalId = terminalId;
        this.payDate = payDate;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Key)) {
            return false;
        }
        Key key = (Key)o;
        if (key.getPayDate().equals(this.payDate)
                && key.getTerminalId().equals(this.terminalId)
                ) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (terminalId == null ? 0 : terminalId.hashCode());
        result = 31 * result + (StringUtils.isEmpty(payDate) ? 0 : payDate.hashCode());
        return result;
    }
}