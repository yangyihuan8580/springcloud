package com.yyh.common.excel.account.local;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class CodeKey {

    private String orderCode;

    private String payCode;

    public CodeKey(String orderCode, String payCode) {
        this.payCode = payCode;
        this.orderCode = orderCode;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof CodeKey)) {
            return false;
        }
        CodeKey key = (CodeKey)o;
        if (key.getOrderCode().equals(this.orderCode)
                || key.getPayCode().equals(this.payCode)
        ) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
//        result = 31 * result + (StringUtils.isEmpty(orderCode) ? 0 : orderCode.hashCode());
        result = 31 * result + (StringUtils.isEmpty(payCode) ? 0 : payCode.hashCode());
        return result;
    }
}
