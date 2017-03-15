package com.dyadav.nytimessearch.modal;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleResponse {
    @SerializedName("docs")
    private List<Article> docs;

    public List<Article> getDocs() {
        Log.d("NY", "Get docs");
        return docs;
    }

    public void setDocs(List<Article> docs) {
        Log.d("NY", "Set docs");
        this.docs = docs;
    }
}
