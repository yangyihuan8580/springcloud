package com.yyh.common.excel.account.local;

import com.alibaba.excel.EasyExcel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LocalUtils {


    public static void readLocalAccountExcel(String localAccountPath) {
        try {
            EasyExcel.read(new FileInputStream(localAccountPath), LocalAccountModel.class, new LocalAccountListener()).sheet(0).doRead();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
