package com.dyadav.nytimessearch.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dyadav.nytimessearch.R;
import com.dyadav.nytimessearch.adapter.ArticlesAdapter;
import com.dyadav.nytimessearch.modal.Article;
import com.dyadav.nytimessearch.modal.ResponseWrapper;
import com.dyadav.nytimessearch.rest.nyTimesAPI;
import com.dyadav.nytimessearch.utility.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.dyadav.nytimessearch.R.id.rView;

public class SearchActivity extends AppCompatActivity {

    List<Article> articleList = new ArrayList<>();
    RecyclerView mView;
    ArticlesAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    static final String BASE_URL = "https://api.nytimes.com/";
    private final static String API_KEY = "d305d9a469b6430b8d80b7d577c7a183";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mView = (RecyclerView) findViewById(rView);
        mAdapter = new ArticlesAdapter(this, articleList);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //mLayoutManager.setGapStrategy();
        mView.setLayoutManager(mLayoutManager);

        ItemClickSupport.addTo(mView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Launch Custom Chrome Tab
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

                //TODO:Set Toolbar and custom actions
                builder.setToolbarColor(ContextCompat.getColor(SearchActivity.this, R.color.colorPrimary));
                builder.addDefaultShareMenuItem();

                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(SearchActivity.this, Uri.parse(articleList.get(position).getWebUrl()));
            }
        });

        mView.setAdapter(mAdapter);
        fetchArticles("");
    }

    private void fetchArticles(String query) {
        Call<ResponseWrapper> call;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nyTimesAPI apiCall = retrofit.create(nyTimesAPI.class);

        if (query.isEmpty()) {
            call = apiCall.loadArticles(API_KEY);
        } else {
            call = apiCall.loadArticlesWithQuery(API_KEY, query);
        }

        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if(response.isSuccessful()) {
                    List<Article> rlist = response.body().getResponse().getArticles();
                    articleList.clear();
                    articleList.addAll(rlist);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(findViewById(R.id.cLayout), "Response Unsuccessful", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                Snackbar.make(findViewById(R.id.cLayout), "Error fetching article list", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                fetchArticles(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
