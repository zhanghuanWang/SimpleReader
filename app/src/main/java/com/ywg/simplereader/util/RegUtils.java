package com.ywg.simplereader.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/1/12.
 */
public class RegUtils {
    /**
     * 正则表达式：验证手机号
     */
    private static final String REG_PHONE_NUMBER = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,1,6,7,8]))\\d{8}$";
    /**
     * 正则表达式：验证邮箱
     */
    private static final String REG_EMAIL = "^[a-zA-Z][\\\\w\\\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\\\w\\\\.-]*[a-zA-Z0-9]\\\\.[a-zA-Z][a-zA-Z\\\\.]*[a-zA-Z]$";
    /**
     * 正则表达式：验证账号
     */
    private static final String REG_ACCOUNT = "^[a-zA-Z]\\w{5,17}$";
    /**
     * 正则表达式：验证密码
     */
    private static final String REG_PWD = "^[a-zA-Z0-9_]{6,16}$";
    /**
     * 正则表达式：验证汉字
     */
    public static final String REG_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
    /**
     * 正则表达式：验证身份证
     */
    public static final String REG_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
    /**
     * 正则表达式：验证URL
     */
    public static final String REG_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    /**
     * 正则表达式：验证IP地址
     */
    public static final String REG_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    /**
     * 验证手机号是否合法
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isMobileNumber(String phoneNumber) {
        return isValid(REG_PHONE_NUMBER, phoneNumber);
    }

    /**
     * 验证邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        return isValid(REG_EMAIL, email);
    }

    /**
     * 验证账号是否合法
     * @param account
     * @return
     */
    public static boolean isAccount(String account) {
        return isValid(REG_ACCOUNT, account);
    }

    /**
     * 验证密码是否合法
     * @param pwd
     * @return
     */
    public static boolean isPwd(String pwd) {
        return isValid(REG_PWD, pwd);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return isValid(REG_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return isValid(REG_ID_CARD, idCard);
    }

    /**
     * 校验URL
     *
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return isValid(REG_URL, url);
    }

    /**
     * 校验IP地址
     *
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return isValid(REG_IP_ADDR, ipAddr);
    }

    /**
     * 正则校验
     *
     * @param pattern 正则表达式
     * @param str     需要校验的字符串
     * @return 验证通过返回true
     */
    public static boolean isValid(String pattern, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(str);
            return m.matches();
        }
    }


}
