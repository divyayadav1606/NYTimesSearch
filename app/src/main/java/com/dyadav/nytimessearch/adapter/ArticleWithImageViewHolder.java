package com.dyadav.nytimessearch.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dyadav.nytimessearch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class ArticleWithImageViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.headline)
    TextView headline;

    public ArticleWithImageViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public TextView getHeadline() {
        return headline;
    }

    public void setHeadline(TextView headline) {
        this.headline = headline;
    }
}
