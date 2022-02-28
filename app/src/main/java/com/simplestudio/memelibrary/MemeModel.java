package com.simplestudio.memelibrary;

public class MemeModel {
    String memeTitle,memeImageUrl;

    public MemeModel(String memeTitle, String memeImageUrl) {
        this.memeTitle = memeTitle;
        this.memeImageUrl = memeImageUrl;
    }

    public String getMemeTitle() {
        return memeTitle;
    }

    public void setMemeTitle(String memeTitle) {
        this.memeTitle = memeTitle;
    }

    public String getMemeImageUrl() {
        return memeImageUrl;
    }

    public void setMemeImageUrl(String memeImageUrl) {
        this.memeImageUrl = memeImageUrl;
    }
}
