package com.dyadav.nytimessearch.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dyadav.nytimessearch.R;
import com.dyadav.nytimessearch.fragment.NewsFragment;

public class MainActivity extends AppCompatActivity {

    private NewsFragment fragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setBackgroundDrawable(null);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = NewsFragment.newInstance();
            transaction.replace(R.id.holder_layout, fragment);
            transaction.commit();
        } else {
            fragment = (NewsFragment) getSupportFragmentManager()
                            .getFragment(savedInstanceState, "newsFragment");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "newsFragment", fragment);
    }
}
