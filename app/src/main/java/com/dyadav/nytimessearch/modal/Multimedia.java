package com.dyadav.nytimessearch.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Multimedia {

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("width")
    @Expose
    private String width;

    public String getUrl() {
        return "http://www.nytimes.com/" + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
