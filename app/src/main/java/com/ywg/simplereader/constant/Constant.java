package com.ywg.simplereader.constant;

/**
 * Created by wwjun.wang on 2015/8/11.
 */
public class Constant {
    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/";
    public static final String START = BASE_URL + "start-image/1080*1776";
    public static final String THEMES = "themes";
    public static final String LATEST_NEWS = BASE_URL + "news/latest";
    public static final String BEFORE = BASE_URL + "news/before/";
    public static final String THEME_NEWS = BASE_URL + "theme/";
    public static final String CONTENT = BASE_URL + "news/";
    public static final String NEWS_EXTRA = "story-extra/";
    public static final String COMMENT = "story/";
    public static final int TOPIC = 131;
    public static final String START_LOCATION = "start_location";
    public static final String CACHE = "cache";
    public static final int LATEST_COLUMN = Integer.MAX_VALUE;
    public static final int BASE_COLUMN = 100000000;

    public static final String SERVER_BASE_URL = "http://192.168.155.1/SimpleReaderNews/";
    public static final String THEME_URL = "get_news_category.php";
    public static final String USER_LOGIN_URL = "user_login.php";
    public static final String USER_REGISTER_URL = "user_register.php";
    public static final String PHONE_IS_EXIST_URL = "phone_is_exist.php";
    public static final String SAVE_USER_INFO_URL = "save_user_info.php";
    public static final String UPDATE_URL = "update.json";
    public static final String CHANGE_PWD_URL = "change_pwd.php";
}
