package com.dyadav.nytimessearch.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dyadav.nytimessearch.R;
import com.dyadav.nytimessearch.adapter.ArticlesAdapter;
import com.dyadav.nytimessearch.databinding.FragmentNewsBinding;
import com.dyadav.nytimessearch.modal.Article;
import com.dyadav.nytimessearch.modal.Filter;
import com.dyadav.nytimessearch.modal.ResponseWrapper;
import com.dyadav.nytimessearch.rest.apiClient;
import com.dyadav.nytimessearch.rest.nyTimesAPI;
import com.dyadav.nytimessearch.utility.ChromeUtils;
import com.dyadav.nytimessearch.utility.EndlessRecyclerViewScrollListener;
import com.dyadav.nytimessearch.utility.ItemClickSupport;
import com.dyadav.nytimessearch.utility.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {

    ArrayList<Article> articleList;
    ArticlesAdapter mAdapter;
    StaggeredGridLayoutManager mLayoutManager;
    EndlessRecyclerViewScrollListener scrollListener;
    String mQuery = null;
    String mBeginDate;
    String mSortOrder;
    String mNewsDesk;
    FragmentNewsBinding binding;
    SharedPreferences mSettings;
    int mPage;

    private final static String API_KEY = com.dyadav.nytimessearch.BuildConfig.apikey;
    private final static String TAG = "NYTimesSearch";

    public NewsFragment() {}

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                                                R.layout.fragment_news,
                                                container, false);

        if (savedInstanceState != null) {
            articleList = savedInstanceState.getParcelableArrayList("articles");
        } else {
            articleList = new ArrayList<>();
        }

        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle(R.string.toolbar_title);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //Read Filter settings from SharedPreferences
        readFilterSettings();

        mAdapter = new ArticlesAdapter(getContext(), articleList);
        binding.rView.setAdapter(mAdapter);

        switch(getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                break;
        }

        binding.rView.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Handler handler = new Handler();
                mPage = page;
                Runnable runnableCode = () -> fetchArticles(page);
                handler.postDelayed(runnableCode, 500);
            }
        };

        binding.rView.addOnScrollListener(scrollListener);

        ItemClickSupport.addTo(binding.rView).setOnItemClickListener((recyclerView, position, v)->
                ChromeUtils.launchChromeTabs(articleList.get(position).getWebUrl(), getContext()));

        if (articleList.size() == 0)
            fetchArticles(0);

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void readFilterSettings() {
        mSettings = getActivity().getSharedPreferences("FilterSettings", Context.MODE_PRIVATE);
        mBeginDate = mSettings.getString("beginDate", null);
        mSortOrder = mSettings.getString("sortOrder", null);
        mNewsDesk =  mSettings.getString("newsDesk", null);
    }

    public void writeFilterSettings() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("beginDate", mBeginDate);
        editor.putString("sortOrder", mSortOrder);
        editor.putString("newsDesk", mNewsDesk);
        editor.apply();
    }

    private void fetchArticles(final int pNum) {
        //Network check
        if(!NetworkUtils.isOnline()) {
            Snackbar.make(binding.cLayout, R.string.connect_error, Snackbar.LENGTH_LONG).show();
            return;
        }

        Call<ResponseWrapper> call;

        nyTimesAPI apiCall = apiClient.getClient().create(nyTimesAPI.class);
        call = apiCall.loadArticles(API_KEY, mQuery, String.valueOf(pNum), mSortOrder, mBeginDate, mNewsDesk);
        binding.progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, call.request().url().toString());

        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<Article> rlist = response.body().getResponse().getArticles();
                    if(pNum == 0){
                        articleList.clear();
                    }
                    articleList.addAll(rlist);
                    mAdapter.notifyDataSetChanged();
                } else {
                    //Snackbar.make(binding.cLayout, R.string.response_unsuccessful, Snackbar.LENGTH_LONG).show();
                    try {
                        Log.d(TAG, response.errorBody().string());
                        if (response.code() == 429)
                            fetchArticles(mPage);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Snackbar.make(binding.cLayout, R.string.response_unsuccessful, Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Snackbar.make(binding.cLayout, R.string.error_fetching_articles, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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

                fDialog.setFinishDialogListener(filter -> {
                    if (filter != null) {
                        mSortOrder = filter.getSort_order();
                        mNewsDesk = filter.getNews_desk();
                        mBeginDate = filter.getBegin_date();
                        //Refresh the article list
                        mQuery = null;
                        fetchArticles(0);
                        //Save the settings to Shared Pref
                        writeFilterSettings();
                    }
                });

                fDialog.show(getActivity().getSupportFragmentManager(),"");
                break;

            case R.id.action_scroll_to_top:
                binding.rView.smoothScrollToPosition(0);
                break;

            case R.id.action_search:
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelableArrayList("articles", articleList);
    }
}
