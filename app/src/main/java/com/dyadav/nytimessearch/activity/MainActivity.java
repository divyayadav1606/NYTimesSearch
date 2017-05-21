package com.dyadav.nytimessearch.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.dyadav.nytimessearch.R;
import com.dyadav.nytimessearch.fragment.BookmarksFragment;
import com.dyadav.nytimessearch.fragment.NewsFragment;
import com.dyadav.nytimessearch.fragment.TrendingFragment;

public class MainActivity extends AppCompatActivity {

    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setBackgroundDrawable(null);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_news:
                                selectedFragment = NewsFragment.newInstance();
                                break;
                            case R.id.action_bookmark:
                                selectedFragment = BookmarksFragment.newInstance();
                                break;
                            case R.id.action_trending:
                                selectedFragment = TrendingFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.holder_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.holder_layout, NewsFragment.newInstance());
        transaction.commit();
    }

    public void onSaveInstanceState(Bundle outState){
        //getFragmentManager().putFragment(outState, "fragment", selectedFragment);
    }
    public void onRestoreInstanceState(Bundle inState){
        //myFragment = getFragmentManager().getFragment(inState,"myfragment");
    }
}
