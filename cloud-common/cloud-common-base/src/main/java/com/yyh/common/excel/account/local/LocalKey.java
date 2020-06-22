package com.yyh.common.excel.account.local;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class LocalKey {

    private String date;

    /** @see PayTypeEnum  */
    private Integer payType;

    private Integer terminalId;

    public LocalKey(String date, Integer payType, Integer terminalId) {
        this.terminalId = terminalId;
        this.payType = payType;
        this.date = date;
    }


    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof LocalKey)) {
            return false;
        }
        LocalKey key = (LocalKey)o;
        if (key.getDate().equals(this.date)
                && key.getPayType().equals(this.payType)
                && key.getTerminalId().equals(this.terminalId)
        ) {
           return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (StringUtils.isEmpty(date) ? 0 : date.hashCode());
        result = 31 * result + (payType == null ? 0 : payType.hashCode());
        result = 31 * result + (terminalId == null ? 0 : terminalId.hashCode());
        return result;
    }

//    public static void main(String[] args) {
//        LocalKey localKey = new LocalKey("2019-01-09",1,1);
//        LocalKey localKey1 = new LocalKey("2019-01-09",2,1);
//        System.out.println(localKey.equals(localKey1));
//    }
}
