package com.example.projektdb;

public class Objekti {

    String key, title, content;

    public Objekti(String key, String title, String content) {
        this.key = key;
        this.title = title;
        this.content = content;


    }

    public Objekti() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

}
