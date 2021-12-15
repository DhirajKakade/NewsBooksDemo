package com.dhiraj.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.dhiraj.news.items.BooksItem;
import com.google.gson.Gson;

public class BookViewActivity extends AppCompatActivity {

    private final Gson gson = new Gson();
    private Toolbar toolbar;
    private CardView cardItemBooks;
    private ImageView ivThumbnailBooksView;
    private TextView tvTitleBookView;
    private TextView tvSubTitleBookView;
    private TextView tvAuthorsBookView;
    private TextView tvPublisherBookView;
    private TextView tvAverageRatingBookView;
    private TextView tvDescriptionBookView;
    private TextView tvRatingsCountBookView;
    private TextView tvPagesBookView;
    private LinearLayout llIsEbookBookView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        initView();

        toolbar.setTitle("BOOK DETAIL");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String bookItem = bundle.getString("bookItem");
        BooksItem booksItem = gson.fromJson(bookItem, BooksItem.class);

        if (booksItem.getVolumeInfo().getImageLinks() != null) {
            if (!TextUtils.isEmpty(booksItem.getVolumeInfo().getImageLinks().getSmallThumbnail())) {
                Glide.with(this)
                        .load(booksItem.getVolumeInfo().getImageLinks().getSmallThumbnail())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(ivThumbnailBooksView);

            } else {
                ivThumbnailBooksView.setImageResource(R.drawable.placeholder);
            }
        } else {
            ivThumbnailBooksView.setImageResource(R.drawable.placeholder);
        }

        tvTitleBookView.setText(booksItem.getVolumeInfo().getTitle());
        if (TextUtils.isEmpty(booksItem.getVolumeInfo().getSubtitle())) {
            tvSubTitleBookView.setVisibility(View.GONE);
        } else {
            tvSubTitleBookView.setText(booksItem.getVolumeInfo().getSubtitle());
        }

        if (booksItem.getVolumeInfo().getAuthors().size() != 0) {
            String authorsList = booksItem.getVolumeInfo().getAuthors().toString();
            String authors = authorsList.substring(1, authorsList.length() - 1).replace(", ", ",");
            tvAuthorsBookView.setText(authors);
        } else {
            tvAuthorsBookView.setVisibility(View.GONE);
        }
        tvPublisherBookView.setText(booksItem.getVolumeInfo().getPublisher());
        tvAverageRatingBookView.setText("" + booksItem.getVolumeInfo().getAverageRating());

        if (booksItem.getVolumeInfo().getRatingsCount() > 0) {
            tvRatingsCountBookView.setText("" + booksItem.getVolumeInfo().getRatingsCount() + " reviews");
        }

        tvDescriptionBookView.setText(booksItem.getVolumeInfo().getDescription());
        tvPagesBookView.setText("" + booksItem.getVolumeInfo().getPageCount());

        if (booksItem.getSaleInfo().isEbook) {
            llIsEbookBookView.setVisibility(View.VISIBLE);
        } else {
            llIsEbookBookView.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        cardItemBooks = findViewById(R.id.cardItemBooks);
        ivThumbnailBooksView = findViewById(R.id.ivThumbnailBooksView);
        tvTitleBookView = findViewById(R.id.tvTitleBookView);
        tvSubTitleBookView = findViewById(R.id.tvSubTitleBookView);
        tvAuthorsBookView = findViewById(R.id.tvAuthorsBookView);
        tvPublisherBookView = findViewById(R.id.tvPublisherBookView);
        tvAverageRatingBookView = findViewById(R.id.tvAverageRatingBookView);
        tvDescriptionBookView = findViewById(R.id.tvDescriptionBookView);
        tvRatingsCountBookView = findViewById(R.id.tvRatingsCountBookView);
        tvPagesBookView = findViewById(R.id.tvPagesBookView);
        llIsEbookBookView = findViewById(R.id.llIsEbookBookView);
    }
}