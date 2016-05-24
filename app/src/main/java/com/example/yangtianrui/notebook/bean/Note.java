package com.example.yangtianrui.notebook.bean;

import java.io.Serializable;

/**
 * Created by yangtianrui on 16-5-22.
 */
public class Note implements Serializable{

    private String title;
    private String content;
    private String createTime;

    public Note(String title, String content, String createTime) {
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
