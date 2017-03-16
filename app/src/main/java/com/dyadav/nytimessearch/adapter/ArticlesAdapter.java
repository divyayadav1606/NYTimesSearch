package com.dyadav.nytimessearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.dyadav.nytimessearch.R;
import com.dyadav.nytimessearch.modal.Article;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Article> articleList;
    private Context context;
    private final int ARTICLE_WITH_IMAGE = 0, ARTICLE_NO_IMAGE = 1;

    public ArticlesAdapter(Context context, List<Article> articleList) {
        this.articleList = articleList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (articleList.get(position).getMultimedia().isEmpty()) {
            return ARTICLE_NO_IMAGE;
        } else {
            return ARTICLE_WITH_IMAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case ARTICLE_WITH_IMAGE:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_with_image, parent, false);
                viewHolder = new ArticleWithImageViewHolder(v1);
                break;

            case ARTICLE_NO_IMAGE:
            default:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_no_image, parent, false);
                viewHolder = new ArticleNoImageViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Article article = articleList.get(position);

        if (article != null) {
            switch (holder.getItemViewType()) {
                case ARTICLE_WITH_IMAGE:
                    ArticleWithImageViewHolder vh1 = (ArticleWithImageViewHolder) holder;
                    vh1.getHeadline().setText(article.getHeadline().getMain());
                    //find largest image
                    for (int i =0; i < article.getMultimedia().size(); i++)
                        if(article.getMultimedia().get(i).getWidth().equals("600"))
                            Glide.with(context)
                                .load(article.getMultimedia().get(i).getUrl())
                                .into(vh1.getImage());
                    break;
                case ARTICLE_NO_IMAGE:
                default: {
                    ArticleNoImageViewHolder vh2 = (ArticleNoImageViewHolder) holder;
                    vh2.getHeadline().setText(article.getHeadline().getMain());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
