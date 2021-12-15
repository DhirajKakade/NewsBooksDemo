package com.dhiraj.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dhiraj.news.adapter.BooksAdapter;
import com.dhiraj.news.adapter.NewsAdapter;
import com.dhiraj.news.adapter.NewsCategoryAdapter;
import com.dhiraj.news.items.BooksItem;
import com.dhiraj.news.items.NewsCategoryItem;
import com.dhiraj.news.items.NewsItem;
import com.dhiraj.news.model.Functions;
import com.dhiraj.news.model.ItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    private final List<NewsItem> newsItemList = new ArrayList<>();
    private final List<BooksItem> booksItemList = new ArrayList<>();
    private final List<NewsCategoryItem> newsCategoryItemList = new ArrayList<>();
    private final Gson gson = new Gson();
    private RecyclerView rvMain;
    private SmoothBottomBar bottomBar;
    private ProgressBar pbMain;
    private RecyclerView rvCategoryMain;
    private Toolbar toolbar;
    private int lastCategoryPosition = 0;
    private EditText etSearchMain;
    private FloatingActionButton fabSearchMain;
    private LinearLayout llSearchMain;
    private TextView tvErrorMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        toolbar.setTitle("NEWS");
        setSupportActionBar(toolbar);
        Functions.centerToolbarTitle(toolbar);

        newsCategoryItemList.add(new NewsCategoryItem("Top-Headlines", true));
        newsCategoryItemList.add(new NewsCategoryItem("Latest", false));
        newsCategoryItemList.add(new NewsCategoryItem("India", false));
        newsCategoryItemList.add(new NewsCategoryItem("World", false));
        newsCategoryItemList.add(new NewsCategoryItem("Business", false));
        newsCategoryItemList.add(new NewsCategoryItem("Technology", false));
        newsCategoryItemList.add(new NewsCategoryItem("Entertainment", false));
        newsCategoryItemList.add(new NewsCategoryItem("Sports", false));
        newsCategoryItemList.add(new NewsCategoryItem("Science", false));
        newsCategoryItemList.add(new NewsCategoryItem("Health", false));

        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                newsCategoryItemList.get(lastCategoryPosition).setSelected(false);
                newsCategoryItemList.get(position).setSelected(true);
                lastCategoryPosition = position;
                rvCategoryMain.getAdapter().notifyDataSetChanged();
                getDataNews(newsCategoryItemList.get(position).getTitle());
            }
        };

        rvCategoryMain.setAdapter(new NewsCategoryAdapter(this, newsCategoryItemList, itemClickListener));
        pbMain.setVisibility(View.VISIBLE);

        ItemClickListener itemClickListener1 = new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (TextUtils.equals(toolbar.getTitle(), "NEWS")) {
                    Intent intent = new Intent(MainActivity.this, NewsViewActivity.class);
                    intent.putExtra("category", newsCategoryItemList.get(lastCategoryPosition).getTitle());
                    intent.putExtra("newsItem", gson.toJson(newsItemList.get(position)));
                    startActivity(intent);
                } else if (TextUtils.equals(toolbar.getTitle(), "BOOKS")) {
                    Intent intent = new Intent(MainActivity.this, BookViewActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(MainActivity.this, view, "bookImg");
                    intent.putExtra("bookItem", gson.toJson(booksItemList.get(position)));
                    startActivity(intent, options.toBundle());
                }
            }
        };

        llSearchMain.setVisibility(View.GONE);
        rvMain.setAdapter(new NewsAdapter(MainActivity.this, newsItemList, itemClickListener1));
        getDataNews(newsCategoryItemList.get(0).getTitle());

        etSearchMain.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getDataBooks(etSearchMain.getText().toString());
                    return true;
                }
                return false;
            }
        });


        fabSearchMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataBooks(etSearchMain.getText().toString());
            }
        });

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if (i == 0) {
                    rvCategoryMain.setVisibility(View.VISIBLE);
                    llSearchMain.setVisibility(View.GONE);
                    rvMain.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    rvMain.setAdapter(new NewsAdapter(MainActivity.this, newsItemList, itemClickListener1));
                    toolbar.setTitle("NEWS");
                    getDataNews(newsCategoryItemList.get(lastCategoryPosition).getTitle());
                } else if (i == 1) {
                    rvCategoryMain.setVisibility(View.GONE);
                    llSearchMain.setVisibility(View.VISIBLE);

                    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); //Получаем размер экрана
                    Display display = wm.getDefaultDisplay();

                    Point point = new Point();
                    display.getSize(point);
                    int screenWidth = point.x;
                    int photoWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 108, getResources().getDisplayMetrics());
                    int columnsCount = screenWidth / photoWidth;
                    rvMain.setLayoutManager(new GridLayoutManager(MainActivity.this, columnsCount));
                    rvMain.setAdapter(new BooksAdapter(MainActivity.this, booksItemList, itemClickListener1));
                    toolbar.setTitle("BOOKS");
                    getDataBooks(etSearchMain.getText().toString());
                }
                return false;
            }
        });
    }

    private void getDataNews(String category) {
        category = category.toLowerCase();
        category = category.replaceAll(" ", "_");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        String date = simpleDateFormat.format(calendar.getTime());

        newsItemList.clear();
        rvMain.getAdapter().notifyDataSetChanged();
        pbMain.setVisibility(View.VISIBLE);
        tvErrorMain.setVisibility(View.GONE);

        AndroidNetworking.get("https://newsapi.org/v2/everything?q=" + category + "&from=" + date + "&sortBy=publishedAt&apiKey="+getString(R.string.news_api_key))
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pbMain.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            if (TextUtils.equals(status, "ok")) {
                                int totalResults = response.getInt("totalResults");
                                JSONArray articles = response.getJSONArray("articles");

                                for (int i = 0; i < articles.length(); i++) {
                                    JSONObject jsonObject = articles.getJSONObject(i);
                                    String title = jsonObject.getString("title");
                                    String description = jsonObject.getString("description");
                                    String url = jsonObject.getString("url");
                                    String urlToImage = jsonObject.getString("urlToImage");
                                    newsItemList.add(new NewsItem(title, description, url, urlToImage));
                                }

                                rvMain.getAdapter().notifyDataSetChanged();
                                rvMain.scheduleLayoutAnimation();
                            }else if (TextUtils.equals(status, "error")){
                                String message = response.getString("message");
                                tvErrorMain.setVisibility(View.VISIBLE);
                                tvErrorMain.setText(message);
                            }
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                            tvErrorMain.setVisibility(View.VISIBLE);
                            tvErrorMain.setText(jsonException.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        try {
                            JSONObject jsonObject = new JSONObject(error.getErrorBody());
                            tvErrorMain.setText(jsonObject.getString("message"));
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                            tvErrorMain.setText(error.getErrorBody());
                        }
                        tvErrorMain.setVisibility(View.VISIBLE);
                        pbMain.setVisibility(View.GONE);

                    }
                });

    }

    private void getDataBooks(String search) {
        if (TextUtils.isEmpty(search)) {
            Toast.makeText(getApplicationContext(), "Please enter book name", Toast.LENGTH_SHORT).show();
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearchMain.getWindowToken(), 0);
        etSearchMain.clearFocus();

        try {
            search = URLEncoder.encode(search, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        booksItemList.clear();

        rvMain.getAdapter().notifyDataSetChanged();
        pbMain.setVisibility(View.VISIBLE);
        tvErrorMain.setVisibility(View.GONE);
        AndroidNetworking.get("https://www.googleapis.com/books/v1/volumes?q=" + search)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pbMain.setVisibility(View.GONE);
                        try {
                            JSONArray items = response.getJSONArray("items");

                            for (int i = 0; i < items.length(); i++) {
                                JSONObject jsonObject = items.getJSONObject(i);
                                BooksItem booksItem = gson.fromJson(jsonObject.toString(), BooksItem.class);
                                booksItemList.add(booksItem);
                            }

                            rvMain.getAdapter().notifyDataSetChanged();
                            rvMain.scheduleLayoutAnimation();

                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                            tvErrorMain.setVisibility(View.VISIBLE);
                            tvErrorMain.setText(jsonException.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        tvErrorMain.setVisibility(View.VISIBLE);
                        tvErrorMain.setText(error.getErrorBody());
                        pbMain.setVisibility(View.GONE);
                    }
                });

    }

    private void initView() {
        rvMain = findViewById(R.id.rvMain);
        bottomBar = findViewById(R.id.bottomBar);
        pbMain = findViewById(R.id.pbMain);
        rvCategoryMain = findViewById(R.id.rvCategoryMain);
        toolbar = findViewById(R.id.toolbar);
        etSearchMain = findViewById(R.id.etSearchMain);
        fabSearchMain = findViewById(R.id.fabSearchMain);
        llSearchMain = findViewById(R.id.llSearchMain);
        tvErrorMain = (TextView) findViewById(R.id.tvErrorMain);
    }
}