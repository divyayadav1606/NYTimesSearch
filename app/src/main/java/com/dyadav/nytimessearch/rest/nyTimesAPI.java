package com.dyadav.nytimessearch.rest;

import com.dyadav.nytimessearch.modal.ResponseWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface nyTimesAPI {

    @GET("svc/search/v2/articlesearch.json")
    Call<ResponseWrapper> loadArticles(@Query("api-key") String key);

    @GET("svc/search/v2/articlesearch.json")
    Call<ResponseWrapper> loadArticlesWithQuery(@Query("api-key") String key
                                                    , @Query("q") String searchQuery);
}
