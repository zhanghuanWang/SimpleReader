package com.ywg.simplereader.bean;

/**
 * Created by cnvp on 15/11/27.
 */
public class Item {

    private int imgResId;

    private String tvTitle;

    private String tvContent;

    public Item(int imgResId, String tvTitle, String tvContent) {
        this.imgResId = imgResId;
        this.tvTitle = tvTitle;
        this.tvContent = tvContent;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }

    public String getTvTitle() {
        return tvTitle;
    }


    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public String getTvContent() {
        return tvContent;
    }

    public void setTvContent(String tvContent) {
        this.tvContent = tvContent;
    }

}
