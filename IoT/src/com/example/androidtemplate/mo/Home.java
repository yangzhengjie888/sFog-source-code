package com.example.androidtemplate.mo;

/**
 *
 */
public class Home {
    private int resourId;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResourId() {
        return resourId;
    }

    public void setResourId(int resourId) {
        this.resourId = resourId;
    }

    public Home(int resourId, String title) {
        this.resourId = resourId;
        this.title = title;
    }
}
