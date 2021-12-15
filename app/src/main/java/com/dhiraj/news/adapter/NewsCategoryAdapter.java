package com.dhiraj.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhiraj.news.model.ItemClickListener;
import com.dhiraj.news.R;
import com.dhiraj.news.items.NewsCategoryItem;

import java.util.ArrayList;
import java.util.List;


public class NewsCategoryAdapter extends RecyclerView.Adapter<NewsCategoryAdapter.MyViewHolder> {

    private final Context context;
    private final ItemClickListener itemClickListener;
    private List<NewsCategoryItem> newsCategoryItems = new ArrayList<>();

    public NewsCategoryAdapter(Context context, List<NewsCategoryItem> newsCategoryItems, ItemClickListener itemClickListener) {
        this.context = context;
        this.newsCategoryItems = newsCategoryItems;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news_category, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NewsCategoryItem newsCategoryItem = newsCategoryItems.get(position);


        if (newsCategoryItem.isSelected()) {
            holder.tvNewsCategoryItem.setTextColor(context.getResources().getColor(R.color.color_text_unselected));
        } else {
            holder.tvNewsCategoryItem.setTextColor(context.getResources().getColor(R.color.default_tag_color));
        }

        holder.tvNewsCategoryItem.setText(newsCategoryItem.getTitle());

        holder.tvNewsCategoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });

//        holder.tvTitleNewsItem.setText(newsItem.getTitle());
//        holder.tvContentNewsItem.setText(newsItem.getDescription());

//        if (!TextUtils.isEmpty(newsItem.getUrlToImage())) {
//            Glide.with(context)
//                    .load(newsItem.getUrlToImage())
//                    .centerCrop()
//                    .placeholder(R.drawable.placeholder)
//                    .into(holder.ivUrlToImageNewsItem);
//
//        } else {
//            holder.ivUrlToImageNewsItem.setImageResource(R.drawable.placeholder);
//        }


    }

    @Override
    public int getItemCount() {
        return newsCategoryItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNewsCategoryItem;

        public MyViewHolder(View view) {
            super(view);
            tvNewsCategoryItem = view.findViewById(R.id.tvNewsCategoryItem);
        }
    }
}


