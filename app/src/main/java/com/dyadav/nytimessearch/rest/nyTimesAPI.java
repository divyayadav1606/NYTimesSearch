package com.dyadav.nytimessearch.rest;

import com.dyadav.nytimessearch.modal.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface nyTimesAPI {

    @GET("svc/search/v2/articlesearch.json")
    Call<ArticleResponse> loadArticles(@Query("api-key") String key);
}
