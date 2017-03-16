package com.dyadav.nytimessearch.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dyadav.nytimessearch.R;
import com.dyadav.nytimessearch.adapter.ArticlesAdapter;
import com.dyadav.nytimessearch.modal.Article;
import com.dyadav.nytimessearch.modal.ResponseWrapper;
import com.dyadav.nytimessearch.rest.nyTimesAPI;
import com.dyadav.nytimessearch.utility.EndlessRecyclerViewScrollListener;
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
    StaggeredGridLayoutManager mLayoutManager;
    EndlessRecyclerViewScrollListener scrollListener;

    static final String BASE_URL = "https://api.nytimes.com/";
    private final static String API_KEY = "d305d9a469b6430b8d80b7d577c7a183";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_title);
        setSupportActionBar(toolbar);

        mView = (RecyclerView) findViewById(rView);
        mAdapter = new ArticlesAdapter(this, articleList);
        mView.setAdapter(mAdapter);

        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mView.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchArticles("warriors", page);
            }
        };

        mView.addOnScrollListener(scrollListener);

        ItemClickSupport.addTo(mView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, articleList.get(position).getWebUrl());

                int requestCode = 100;

                PendingIntent pendingIntent = PendingIntent.getActivity(SearchActivity.this,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(SearchActivity.this, R.color.colorPrimary));
                builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(SearchActivity.this, Uri.parse(articleList.get(position).getWebUrl()));
            }
        });

        fetchArticles("warriors", 0);
    }

    private void fetchArticles(String query, int pNum) {
        Call<ResponseWrapper> call;
        //Pick up these values from Shared prefrences
        String beginDate = "20160316";
        String categories = "news_desk:(\"Sports\")";
        String sortOrder = "oldest";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nyTimesAPI apiCall = retrofit.create(nyTimesAPI.class);

        call = apiCall.loadArticles(API_KEY, query, String.valueOf(pNum), sortOrder, beginDate, categories);

        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response.isSuccessful()) {
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
                scrollListener.resetState();
                fetchArticles(query, 1);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                FilterDialog fDialog = new FilterDialog();
                fDialog.show(getSupportFragmentManager(),"");

            case R.id.action_search:
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
