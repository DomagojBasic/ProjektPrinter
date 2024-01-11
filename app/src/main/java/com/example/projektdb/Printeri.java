package com.example.projektdb;

public class Printeri {
    String key, title, content, lokacija;
    Objekti lokObjekti;
    boolean checkBoxInformatika, checkBoxServis, checkBoxLDC;


    public Objekti getLokObjekti() {
        return lokObjekti;
    }

    public void setLokObjekti(Objekti lokObjekti) {
        this.lokObjekti = lokObjekti;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public Printeri(String key, String title, String content, String lokacija, boolean checkBoxInformatika, boolean checkBoxServis, boolean checkBoxLDC, Objekti lokObjekti) {
        this.key = key;
        this.lokObjekti = lokObjekti;
        this.title = title;
        this.content = content;
        this.lokacija = lokacija;
        this.checkBoxInformatika = checkBoxInformatika;
        this.checkBoxServis = checkBoxServis;
        this.checkBoxLDC = checkBoxLDC;
    }


    public boolean isCheckBoxInformatika() {
        return checkBoxInformatika;
    }

    public void setCheckBoxInformatika(boolean checkBoxInformatika) {
        this.checkBoxInformatika = checkBoxInformatika;
    }

    public boolean isCheckBoxServis() {
        return checkBoxServis;
    }

    public void setCheckBoxServis(boolean checkBoxServis) {
        this.checkBoxServis = checkBoxServis;
    }

    public boolean isCheckBoxLDC() {
        return checkBoxLDC;
    }

    public void setCheckBoxLDC(boolean checkBoxLDC) {
        this.checkBoxLDC = checkBoxLDC;
    }

    public Printeri() {
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getKey() {
        return key;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
