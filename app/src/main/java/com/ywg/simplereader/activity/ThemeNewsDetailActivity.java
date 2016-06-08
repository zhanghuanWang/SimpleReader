package com.ywg.simplereader.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ywg.simplereader.R;
import com.ywg.simplereader.bean.Content;
import com.ywg.simplereader.bean.StoriesEntity;
import com.ywg.simplereader.constant.Constant;
import com.ywg.simplereader.db.WebCacheDbHelper;
import com.ywg.simplereader.util.NetworkUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


public class ThemeNewsDetailActivity extends SlideBackActivity {

    private Toolbar mToolBar;
    private boolean isNight;

    private WebView mWebView;

    private Intent intent;

    private StoriesEntity storiesEntity;

    private String themeName;

    private Content content;

    private WebCacheDbHelper dbHelper;

    private Button mReloadBtn;

    private FrameLayout mReloadLayout;

    private FloatingActionButton mCommentBtn;

  //  private EditText mCommentEt;

   // private TextView mCommentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_news_detail);

        dbHelper = new WebCacheDbHelper(this, 1);

        //imageLoaderTools = ImageLoaderUtils.getInstance(getApplicationContext());

        intent = getIntent();
        themeName = intent.getStringExtra("themeName");
        isNight = intent.getBooleanExtra("isNight", true);
        storiesEntity = (StoriesEntity) intent.getSerializableExtra("entity");

        //初始化View
        initViews();

        setListeners();

        //加载新闻详情数据
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                loadNewsData();
            }
        });

        //setupRevealBackground(savedInstanceState);
    }

    private void initViews() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        mToolBar.setTitle(themeName);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //setStatusBarColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));

       /* mCoolapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //mCoolapsingToolbar.setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        mCoolapsingToolbar.setTitle(storiesEntity.getTitle());
        mCoolapsingToolbar.setContentScrimColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        mCoolapsingToolbar.setStatusBarScrimColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));*/

        mCommentBtn = (FloatingActionButton) findViewById(R.id.btn_comment);
        //mCommentEt = (EditText) findViewById(R.id.et_comment);

      //  mCommentTv = (TextView) findViewById(R.id.tv_comment);

        mReloadBtn = (Button) findViewById(R.id.btn_reload);
        mReloadLayout = (FrameLayout) findViewById(R.id.layout_reload);
        mReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkConnected(ThemeNewsDetailActivity.this)) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            loadNewsData();
                        }
                    });
                } else {
                    Toast.makeText(ThemeNewsDetailActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);
        // int bgColor = isNight ? R.color.item_bg_color_dark : R.color.item_bg_color_light;
        // mWebView.loadUrl("http://www.baidu.com");
        // String customHtml = "<html><body bgcolor='" + bgColor + "'>Hello World</body></html>";
        // mWebView.loadData(customHtml, "text/html", "UTF-8");
    }

    private void setListeners() {
        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThemeNewsDetailActivity.this, NewsCommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("newsId", storiesEntity.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });

      /*  mCommentEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    KeyBoardUtils.openKeybord(mCommentEt, ThemeNewsDetailActivity.this);
                }
            }
        });
        mCommentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThemeNewsDetailActivity.this, NewsCommentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        //   menu.getItem(2).setTitle(isNight ? "日间模式" : "夜间模式");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_screen_capture:

                break;

            default:
                break;
        }
        return true;
    }


    /**
     * //加载新闻详情数据
     */
    private void loadNewsData() {

        //先试着从缓存加载新闻数据 若缓存中没有 则加载网络数据
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Cache where newsId = " + storiesEntity.getId(), null);
        if (cursor.moveToFirst()) {
            mWebView.setVisibility(View.VISIBLE);
            mReloadLayout.setVisibility(View.INVISIBLE);

            String json = cursor.getString(cursor.getColumnIndex("json"));
            parseJson(json);
            cursor.close();
            db.close();
        } else {
            //判断网络是否可用
            if (NetworkUtils.isNetworkConnected(this)) {
                mWebView.setVisibility(View.VISIBLE);
                mReloadLayout.setVisibility(View.INVISIBLE);

                OkHttpUtils.get().url(Constant.CONTENT + storiesEntity.getId()).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        response = response.replaceAll("'", "''");
                        db.execSQL("replace into Cache(newsId,json) values(" + storiesEntity.getId() + ",'" + response + "')");
                        db.close();
                        parseJson(response);
                    }
                });
            } else {
                mWebView.setVisibility(View.INVISIBLE);
                mReloadLayout.setVisibility(View.VISIBLE);
                Toast.makeText(ThemeNewsDetailActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseJson(String responseString) {
        Gson gson = new Gson();
        content = gson.fromJson(responseString, Content.class);
        //imageLoaderTools.displayImage(content.getImage(), mHeaderImage);
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news_light.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }

   /* private void parseCommentJson(String responseString) {
        Gson gson = new Gson();
        NewsExtraInfo comment = gson.fromJson(responseString, NewsExtraInfo.class);
        mCommentTv.setText(comment.getComments());
    }*/

    /*@Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= 21) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }*/

}
