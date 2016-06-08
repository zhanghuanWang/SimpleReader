package com.ywg.simplereader.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cnvp on 15/12/22.
 */
public class TimeUtils {


    public static String convertDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowData = sdf.format(new Date());
        int compareDate = Integer.parseInt(nowData) - Integer.parseInt(date);
        switch (compareDate) {
            case 0:
                return "今日热闻";
            case 1:
                return "昨天";
            case 2:
                return "前天";
            default:
                String result = date.substring(0, 4);
                result += "年";
                result += date.substring(4, 6);
                result += "月";
                result += date.substring(6, 8);
                result += "日";
                return result;
        }

    }

    /**
     * 时间戳格式化
     * @param millis
     * @return
     */
    public static String timestampFormat(long millis) {

        SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(millis * 1000);
        return sdf.format(date);
    }
}
