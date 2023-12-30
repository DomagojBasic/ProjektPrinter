package com.example.projektdb;

import android.widget.Button;

public class Printeri {
    String key, title, content;

    Button btnLDC;
    Button btnServis;


    public Printeri() {

    }

    public Printeri(Button btnLDC, Button btnServis) {
        this.btnLDC = btnLDC;
        this.btnServis = btnServis;
    }

    public Button getBtnLDC() {
        return btnLDC;
    }

    public void setBtnLDC(Button btnLDC) {
        this.btnLDC = btnLDC;
    }

    public Button getBtnServis() {
        return btnServis;
    }

    public void setBtnServis(Button btnServis) {
        this.btnServis = btnServis;
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
