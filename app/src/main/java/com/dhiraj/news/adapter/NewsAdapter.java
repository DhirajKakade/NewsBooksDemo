package com.dhiraj.news.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dhiraj.news.R;
import com.dhiraj.news.items.NewsItem;
import com.dhiraj.news.model.ItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private final Context context;
    private final ItemClickListener itemClickListener;
    private List<NewsItem> newsItems = new ArrayList<>();

    public NewsAdapter(Context context, List<NewsItem> newsItems, ItemClickListener itemClickListener) {
        this.context = context;
        this.newsItems = newsItems;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final NewsItem newsItem = newsItems.get(position);
        holder.tvTitleNewsItem.setText(newsItem.getTitle());
//        holder.tvContentNewsItem.setText(newsItem.getDescription());

        if (!TextUtils.isEmpty(newsItem.getUrlToImage())) {
            Glide.with(context)
                    .load(newsItem.getUrlToImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(holder.ivUrlToImageNewsItem);

        } else {
            holder.ivUrlToImageNewsItem.setImageResource(R.drawable.placeholder);
        }

        holder.llNewsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardItemNews;
        public LinearLayout llNewsItem;
        public ImageView ivUrlToImageNewsItem;
        public TextView tvTitleNewsItem;
        public TextView tvContentNewsItem;

        public MyViewHolder(View view) {
            super(view);
            cardItemNews = view.findViewById(R.id.cardItemNews);
            llNewsItem = view.findViewById(R.id.llNewsItem);
            ivUrlToImageNewsItem = view.findViewById(R.id.ivUrlToImageNewsItem);
            tvTitleNewsItem = view.findViewById(R.id.tvTitleNewsItem);
//            tvContentNewsItem = view.findViewById(R.id.tvContentNewsItem);

        }
    }
}


