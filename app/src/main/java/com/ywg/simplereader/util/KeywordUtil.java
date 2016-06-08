package com.ywg.simplereader.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 关键字高亮的工具类
 */
public class KeywordUtil {

    /**
     * 某个关键字高亮变色 并设置超链接
     * 调用该方法后 需 TextView.setLinksClickable(true);TextView.setMovementMethod(LinkMovementMethod.getInstance());
     *
     * @param color   变化的色值
     * @param text    文字
     * @param keyword 文字中的关键字
     * @param link    超链接 "tel:4155551212"电话
     *                "mailto:webmaster@google.com"邮件
     *                "http://www.baidu.com"网络
     *                "sms:4155551212"短信 使用sms:或者smsto:
     *                "mms:4155551212"彩信 使用mms:或者mmsto:
     *                "geo:38.899533,-77.036476"地图
     * @return
     */
    public static SpannableString highlightKeyword(int color, String text,
                                                   String keyword, String link) {
        SpannableString s = new SpannableString(text);
        CharacterStyle span = null;
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) { //通过正则查找，逐个高亮
            span = new ForegroundColorSpan(color);
            int start = m.start();
            int end = m.end();
            s.setSpan(span, start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 设置超链接
            s.setSpan(new URLSpan(link), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    /**
     * 多个关键字高亮变色
     *
     * @param color   变化的色值
     * @param text    文字
     * @param keyword 文字中的关键字数组
     * @return
     */
    public static SpannableString highlightKeywords(int color, String text,
                                                    String[] keyword) {
        SpannableString s = new SpannableString(text);
        CharacterStyle span = null;
        for (int i = 0; i < keyword.length; i++) {
            Pattern p = Pattern.compile(keyword[i]);
            Matcher m = p.matcher(s);
            while (m.find()) { //通过正则查找，逐个高亮
                span = new ForegroundColorSpan(color);
                int start = m.start();
                int end = m.end();
                s.setSpan(span, start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return s;
    }

    /**
     * 设置删除线
     */
    public static SpannableString setStrikethrough(String text,
                                                   String keyword) {
        // 创建一个 SpannableString对象
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) { //通过正则查找，逐个高亮
            int start = m.start();
            int end = m.end();
            //设置背景颜色
            s.setSpan(new StrikethroughSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //设置TextView可点击
        //tv_content6.setMovementMethod(LinkMovementMethod.getInstance());
        return s;
    }

    /**
     * 设置下划线
     */
    public static SpannableString setUnderLine(String text,
                                               String keyword) {
        // 创建一个 SpannableString对象
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) { //通过正则查找，逐个高亮
            int start = m.start();
            int end = m.end();
            //设置背景颜色
            s.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //设置TextView可点击
        //tv_content6.setMovementMethod(LinkMovementMethod.getInstance());
        return s;
    }

    /**
     * 设置字体样式 正常Typeface.NORMAL，粗体Typeface.BOLD，斜体Typeface.ITALIC，粗斜体Typeface.BOLD_ITALIC
     */
    public static SpannableString setFontStyle(int typeface, String text,
                                               String keyword) {
        // 创建一个 SpannableString对象
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) { //通过正则查找，逐个高亮
            int start = m.start();
            int end = m.end();
            //设置背景颜色
            s.setSpan(new StyleSpan(typeface), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    /**
     * 设置字体样式 (default,default-bold,monospace,serif,sans-serif)
     */
    public static SpannableString setFontStyle(String typeface, String text,
                                               String keyword) {
        // 创建一个 SpannableString对象
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) { //通过正则查找，逐个高亮
            int start = m.start();
            int end = m.end();
            //设置背景颜色
            s.setSpan(new TypefaceSpan(typeface), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    /**
     * 设置字体大小
     */
    public static SpannableString setFontSize(Context context, int fontSize, String text,
                                              String keyword) {
        // 创建一个 SpannableString对象
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) { //通过正则查找，逐个高亮
            int start = m.start();
            int end = m.end();
            //设置背景颜色
            s.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(context, fontSize)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    /**
     * 设置字体大小
     */
    public static SpannableString setFontSize(float fontSize, String text,
                                              String keyword) {
        // 创建一个 SpannableString对象
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) { //通过正则查找，逐个高亮
            int start = m.start();
            int end = m.end();
            //设置背景颜色
            s.setSpan(new RelativeSizeSpan(fontSize), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    /**
     * 设置关键字的背景色
     */
    public static SpannableString setBackgroundColor(int color, String text,
                                                     String keyword) {
        SpannableString s = new SpannableString(text);
        CharacterStyle span = null;
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) { //通过正则查找，逐个高亮
            span = new BackgroundColorSpan(color);
            int start = m.start();
            int end = m.end();
            s.setSpan(span, start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

}