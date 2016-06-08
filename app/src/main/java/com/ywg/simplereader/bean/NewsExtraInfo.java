package com.ywg.simplereader.bean;

/**
 * Created by cnvp on 15/12/22.
 */
public class NewsExtraInfo {

    /**
     * "long_comments": 0,
     * "popularity": 161,
     * "short_comments": 19,
     * "comments": 19,
     */
    //长评论
    private String long_comments;
    //点赞数
    private String popularity;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    //短评论
    public String getLong_comments() {
        return long_comments;
    }

    public String getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(String short_comments) {
        this.short_comments = short_comments;
    }

    public String getPopularity() {
        return popularity;

    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public void setLong_comments(String long_comments) {
        this.long_comments = long_comments;
    }

    private String short_comments;
    //总评论
    private String comments;


}
