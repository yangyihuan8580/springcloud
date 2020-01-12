package com.yyh.common.excel.account.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.yyh.common.excel.account.wechat.WechatUtils;
import org.apache.commons.lang3.StringUtils;

public class WechatConverter implements Converter<String> {

    @Override
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return WechatUtils.strSub(cellData.getStringValue());
    }


    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

//    public static void main(String[] args) {
//        String stringValue = "`10.00";
//        if (StringUtils.isNotEmpty(stringValue) && stringValue.startsWith("`")) {
//            System.out.println(stringValue.substring(1));
//        }
//    }
}
