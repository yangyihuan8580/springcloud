package com.yyh.common.excel_pay;

import com.yyh.common.excel.account.alipay.AlipayUtils;
import com.yyh.common.excel.account.wechat.WechatUtils;
import com.yyh.common.excel_pay.local.LocalPayUtils;
import com.yyh.common.excel_pay.xinlibo.aipark.AiparkOrderPayUtils;
import com.yyh.common.excel_pay.xinlibo.local.XlbOrderPayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yyh
 * \* Date: 2020/3/23
 * \* Time: 11:54
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class main {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        loadData();

        dataDiff();
        long end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");


//        LocalPayUtils.readLocalPayAccountExcel("D:\\zhht\\对账\\9-12月份电子支付\\互通原数据\\tob_order_9月T1_微信.xlsx");
//        AiparkOrderPayUtils.readLocalPayAccountExcel("D:\\zhht\\对账\\9-12月份电子支付\\辛利泊数据\\T1T3互通报表2019\\T1互通电子支付\\T1-微信互通流水20191001-210258.xlsx");
//        XlbOrderPayUtils.readLocalPayAccountExcel("D:\\zhht\\对账\\9-12月份电子支付\\辛利泊数据\\新利泊表2019\\T1新利泊电子支付\\T1-微信新利泊流水20190901-112129.xlsx");

    }

    private static void dataDiff() throws ParseException {
        DataDiff.diff();

    }

    public static void loadData() throws IOException {
        for (String path: getFilePathList(PayContext.LOCAL_PATH)) {
            if (!Filter.filter(path)) {
                continue;
            }
            LocalPayUtils.readLocalPayAccountExcel(path);
        }

//        for (String path: getFilePathList(PayContext.WECHAT_PATH)) {
//            WechatUtils.readWechatAccountExcel(path);
//        }
//
//        for (String path: getFilePathList(PayContext.ALIPAY_PATH)) {
//            AlipayUtils.readAlipayAccountExcel(path);
//        }


        for (String path: getFilePathList(PayContext.THIRD_AIPARK_PATH)) {
            if (!Filter.filter(path)) {
                continue;
            }
            AiparkOrderPayUtils.readLocalPayAccountExcel(path);
        }

        for (String path: getFilePathList(PayContext.THIRD_XLB_PATH)) {
            if (!Filter.filter(path)) {
                continue;
            }
            XlbOrderPayUtils.readLocalPayAccountExcel(path);
        }
    }

    public static List<String> getFilePathList (String path) throws IOException {
        List<String> pathList = new ArrayList<>();
        readfile(path, pathList);
        return pathList;
    }


    public static void readfile(String filepath, List<String> path) throws FileNotFoundException, IOException {
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                path.add(file.getPath());
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        path.add(readfile.getPath());
                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "\\" + filelist[i], path);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
    }
}