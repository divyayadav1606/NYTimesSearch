package com.dyadav.nytimessearch.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dyadav.nytimessearch.R;
import com.dyadav.nytimessearch.adapter.ArticlesAdapter;
import com.dyadav.nytimessearch.databinding.ActivitySearchBinding;
import com.dyadav.nytimessearch.modal.Article;
import com.dyadav.nytimessearch.modal.Filter;
import com.dyadav.nytimessearch.modal.ResponseWrapper;
import com.dyadav.nytimessearch.rest.nyTimesAPI;
import com.dyadav.nytimessearch.utility.EndlessRecyclerViewScrollListener;
import com.dyadav.nytimessearch.utility.ItemClickSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    List<Article> articleList = new ArrayList<>();
    RecyclerView mView;
    ArticlesAdapter mAdapter;
    StaggeredGridLayoutManager mLayoutManager;
    EndlessRecyclerViewScrollListener scrollListener;
    String mQuery = null;
    String mBeginDate;
    String mSortOrder;
    String mNewsDesk;
    ActivitySearchBinding binding;
    SharedPreferences mSettings;

    static final String BASE_URL = "https://api.nytimes.com/";
    private final static String API_KEY = "d305d9a469b6430b8d80b7d577c7a183";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle(R.string.toolbar_title);
        setSupportActionBar(toolbar);

        //Read Filter settings from SharedPreferences
        mSettings = getSharedPreferences("FilterSettings", Context.MODE_PRIVATE);
        mBeginDate = mSettings.getString("beginDate", null);
        mSortOrder = mSettings.getString("sortOrder", null);
        mNewsDesk =  mSettings.getString("newsDesk", null);

        mView = binding.rView;
        mAdapter = new ArticlesAdapter(this, articleList);
        mView.setAdapter(mAdapter);

        switch(getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                break;
        }


        mView.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchArticles(page);
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

            PendingIntent pendingIntent = PendingIntent.getActivity(SearchActivity.this,
                    100,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(SearchActivity.this, R.color.colorPrimary));
            builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(SearchActivity.this, Uri.parse(articleList.get(position).getWebUrl()));
            }
        });
        fetchArticles(0);
    }

    private void fetchArticles(final int pNum) {
        Call<ResponseWrapper> call;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nyTimesAPI apiCall = retrofit.create(nyTimesAPI.class);
        call = apiCall.loadArticles(API_KEY, mQuery, String.valueOf(pNum), mSortOrder, mBeginDate, mNewsDesk);
        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response.isSuccessful()) {
                    List<Article> rlist = response.body().getResponse().getArticles();
                    if(pNum == 0){
                        articleList.clear();
                    }
                    articleList.addAll(rlist);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(binding.cLayout, "Response Unsuccessful", Snackbar.LENGTH_LONG).show();
                    try {
                        Log.d("NYTimesSearch", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                Snackbar.make(binding.cLayout, "Error fetching article list", Snackbar.LENGTH_LONG).show();
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
                articleList.clear();
                scrollListener.resetState();
                mQuery = query;
                fetchArticles(0);
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

                if (mSortOrder != null) {
                    Bundle bundle = new Bundle();
                    Filter filter = new Filter();
                    filter.setSort_order(mSortOrder);
                    filter.setBegin_date(mBeginDate);
                    filter.setNews_desk(mNewsDesk);

                    bundle.putParcelable("filter", filter);
                    fDialog.setArguments(bundle);
                }

                fDialog.setFinishDialogListener(new FilterDialog.FilterTaskListener() {
                    @Override
                    public void onFinishDialog(Filter filter) {
                        if (filter != null) {
                            mSortOrder = filter.getSort_order();
                            mNewsDesk = filter.getNews_desk();
                            mBeginDate = filter.getBegin_date();
                            //Refresh the article list
                            fetchArticles(0);
                            //Save the settings to Shared Pref
                            mSettings = getSharedPreferences("FilterSettings", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putString("beginDate", mBeginDate);
                            editor.putString("sortOrder", mSortOrder);
                            editor.putString("newsDesk", mNewsDesk);
                            editor.commit();
                        }
                    }
                });

                fDialog.show(getSupportFragmentManager(),"");

            case R.id.action_search:
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
