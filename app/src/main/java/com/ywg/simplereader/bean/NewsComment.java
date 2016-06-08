package com.ywg.simplereader.bean;

/**
 * Created by cnvp on 15/12/22.
 */
public class NewsComment {
    /**
     * "author": "EleganceWorld",
     * "id": 545442,
     * "content": "上海到济南，无尽的猪排盖饭… （后略）",
     * "likes": 0,
     * "time": 1413589303,
     * "avatar": "http://pic2.zhimg.com/1f76e6a25_im.jpg"
     */

    private String author;

    public String getLikes() {
        return likes;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {


        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {

        this.id = id;
    }

    private String id;

    private String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private String likes;

    private String time;

    private String avatar;


}
