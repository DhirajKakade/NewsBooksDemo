package com.dhiraj.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.dhiraj.news.items.NewsItem;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;

public class NewsViewActivity extends AppCompatActivity {

    private final Gson gson = new Gson();
    private Toolbar toolbar;
    private ImageView ivUrlToImageNewsView;
    private WebView webViewNewsView;
    private CollapsingToolbarLayout ctNewsView;
    private ProgressBar pbNewsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);
        initView();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String category = bundle.getString("category");
        String sNewsItem = bundle.getString("newsItem");
        NewsItem newsItem = gson.fromJson(sNewsItem, NewsItem.class);
        toolbar.setTitle(category);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ctNewsView.setTitle(category);
        ctNewsView.setCollapsedTitleTextColor(getResources().getColor(R.color.color_text_unselected));


        if (!TextUtils.isEmpty(newsItem.getUrlToImage())) {
            Glide.with(this)
                    .load(newsItem.getUrlToImage())
                    .fitCenter()
                    .placeholder(R.drawable.placeholder)
                    .into(ivUrlToImageNewsView);

        } else {
            ivUrlToImageNewsView.setImageResource(R.drawable.placeholder);
        }

        webViewNewsView.getSettings().setJavaScriptEnabled(true);
        webViewNewsView.loadUrl(newsItem.getUrl());
        webViewNewsView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbNewsView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbNewsView.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                pbNewsView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        if (title != null && !title.toString().isEmpty()) {
            String mTitle = title.toString();
            ctNewsView.setTitle(mTitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        ivUrlToImageNewsView = findViewById(R.id.ivUrlToImageNewsView);
        webViewNewsView = findViewById(R.id.webViewNewsView);
        ctNewsView = findViewById(R.id.ctNewsView);
        pbNewsView = findViewById(R.id.pbNewsView);
    }
}