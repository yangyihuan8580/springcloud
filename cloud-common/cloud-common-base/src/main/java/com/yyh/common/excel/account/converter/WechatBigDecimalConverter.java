package com.yyh.common.excel.account.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;
import com.yyh.common.excel.account.wechat.WechatUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

public class WechatBigDecimalConverter implements Converter<BigDecimal> {
    @Override
    public Class supportJavaTypeKey() {
        return BigDecimal.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public BigDecimal convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new BigDecimal(WechatUtils.strSub(cellData.getStringValue()));
    }

    @Override
    public CellData convertToExcelData(BigDecimal value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }
}
