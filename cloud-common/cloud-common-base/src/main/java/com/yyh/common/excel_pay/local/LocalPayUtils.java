package com.yyh.common.excel_pay.local;


import com.alibaba.excel.EasyExcel;
import com.yyh.common.excel.account.local.LocalAccountListener;
import com.yyh.common.excel.account.local.LocalAccountModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LocalPayUtils {

    public static void readLocalPayAccountExcel(String localAccountPath) {
        try {
            EasyExcel.read(new FileInputStream(localAccountPath), Order.class, new LocalPayListerner()).sheet(0).doRead();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}