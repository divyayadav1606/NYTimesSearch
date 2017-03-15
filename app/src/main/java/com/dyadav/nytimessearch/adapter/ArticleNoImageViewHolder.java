package com.dyadav.nytimessearch.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dyadav.nytimessearch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class ArticleNoImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.headline_only)
    TextView headline;

    public ArticleNoImageViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    public TextView getHeadline() {
        return headline;
    }

    public void setHeadline(TextView headline) {
        this.headline = headline;
    }
}
