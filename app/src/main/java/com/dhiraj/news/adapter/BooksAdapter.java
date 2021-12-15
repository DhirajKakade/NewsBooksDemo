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
import com.dhiraj.news.items.BooksItem;
import com.dhiraj.news.model.ItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private final Context context;
    private final ItemClickListener itemClickListener;
    private List<BooksItem> booksItemList = new ArrayList<>();

    public BooksAdapter(Context context, List<BooksItem> booksItemList, ItemClickListener itemClickListener) {
        this.context = context;
        this.booksItemList = booksItemList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_books, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final BooksItem booksItem = booksItemList.get(position);
        holder.tvTitleBooksItem.setText(booksItem.getVolumeInfo().getTitle());

        if (booksItem.getVolumeInfo().getImageLinks() != null) {
            if (!TextUtils.isEmpty(booksItem.getVolumeInfo().getImageLinks().getSmallThumbnail())) {
                Glide.with(context)
                        .load(booksItem.getVolumeInfo().getImageLinks().getSmallThumbnail())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(holder.ivThumbnailBooksItem);

            } else {
                holder.ivThumbnailBooksItem.setImageResource(R.drawable.placeholder);
            }
        } else {
            holder.ivThumbnailBooksItem.setImageResource(R.drawable.placeholder);
        }

        holder.llBooksItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return booksItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llBooksItem;
        public CardView cardItemBooks;
        public ImageView ivThumbnailBooksItem;
        public TextView tvTitleBooksItem;

        public MyViewHolder(View view) {
            super(view);
            llBooksItem = view.findViewById(R.id.llBooksItem);
            cardItemBooks = view.findViewById(R.id.cardItemBooks);
            ivThumbnailBooksItem = view.findViewById(R.id.ivThumbnailBooksItem);
            tvTitleBooksItem = view.findViewById(R.id.tvTitleBooksItem);

        }
    }
}


