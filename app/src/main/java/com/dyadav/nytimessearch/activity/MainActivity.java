package com.dyadav.nytimessearch.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dyadav.nytimessearch.R;
import com.dyadav.nytimessearch.fragment.NewsFragment;

public class MainActivity extends AppCompatActivity {

    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setBackgroundDrawable(null);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.holder_layout, NewsFragment.newInstance());
        transaction.commit();
    }

    public void onSaveInstanceState(Bundle outState){
        //getFragmentManager().putFragment(outState, "fragment", selectedFragment);
    }
    public void onRestoreInstanceState(Bundle inState){
        //myFragment = getFragmentManager().getFragment(inState,"myfragment");
    }
}
