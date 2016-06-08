package com.ywg.simplereader.bean;

import java.util.List;

/**
 * Created by cnvp on 15/12/22.
 */
public class LongComments {
    public List<NewsComment> getComments() {
        return comments;
    }

    public void setComments(List<NewsComment> comments) {
        this.comments = comments;
    }

    private List<NewsComment> comments;

}
