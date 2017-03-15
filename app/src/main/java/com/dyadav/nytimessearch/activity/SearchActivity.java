package com.dyadav.nytimessearch.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.dyadav.nytimessearch.R;
import com.dyadav.nytimessearch.adapter.ArticlesAdapter;
import com.dyadav.nytimessearch.modal.Article;
import com.dyadav.nytimessearch.modal.ResponseWrapper;
import com.dyadav.nytimessearch.rest.nyTimesAPI;

import java.util.ArrayList;
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
        RecyclerView mView;
        final ArticlesAdapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final List<Article> articleList = new ArrayList<>();

        mView = (RecyclerView) findViewById(R.id.rView);
        mAdapter = new ArticlesAdapter(this, articleList);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mView.setLayoutManager(mLayoutManager);
        mView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nyTimesAPI apiCall = retrofit.create(nyTimesAPI.class);

        Call<ResponseWrapper> call = apiCall.loadArticles(API_KEY);

        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if(response.isSuccessful()) {
                    List<Article> rlist = response.body().getResponse().getArticles();
                    articleList.clear();
                    articleList.addAll(rlist);
                    mAdapter.notifyDataSetChanged();
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                // TODO: handle error
            }
        });
    }
}
