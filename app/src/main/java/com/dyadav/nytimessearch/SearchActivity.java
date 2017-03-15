package com.dyadav.nytimessearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dyadav.nytimessearch.modal.Article;
import com.dyadav.nytimessearch.modal.ArticleResponse;
import com.dyadav.nytimessearch.rest.nyTimesAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    static final String BASE_URL = "https://api.nytimes.com/";
    private final static String API_KEY = "d305d9a469b6430b8d80b7d577c7a183";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nyTimesAPI apiCall = retrofit.create(nyTimesAPI.class);

        Call<ArticleResponse> call = apiCall.loadArticles(API_KEY);

        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                if(response.isSuccessful()) {
                    List<Article> articleList = response.body().getDocs();
                    Log.d("NY", "Number of articles received: " + articleList.size());
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                // TODO: handle error
            }
        });
    }
}
