package com.ywg.simplereader.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.ywg.simplereader.R;
import com.ywg.simplereader.bean.Content;
import com.ywg.simplereader.bean.StoriesEntity;
import com.ywg.simplereader.constant.Constant;
import com.ywg.simplereader.db.WebCacheDbHelper;
import com.ywg.simplereader.util.BitmapUtils;
import com.ywg.simplereader.util.CommonUtil;
import com.ywg.simplereader.util.NetworkUtils;
import com.ywg.simplereader.util.ScreenUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;


public class HotNewsDetailActivity extends SlideBackActivity {

    private Toolbar mToolBar;
    private boolean isNight;

    private WebView mWebView;

    private WebSettings webSettings;

    private int mCurItem = 2;
    private int mSelectedItem;

    private Intent intent;

    private StoriesEntity storiesEntity;

    private String themeName;

    private Content content;

    private WebCacheDbHelper dbHelper;

    private Button mReloadBtn;

    private Button mSetNetworkBtn;
    private FrameLayout mReloadLayout;

    private ImageView mHeaderImage;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private FloatingActionButton mCommentBtn;

    private ProgressBar mProgessBar;

    // private EditText mCommentEt;

    //private TextView mCommentTv;

    private String newsId;

    private ProgressDialog mProgressDialog;

    private PopupWindow mPopupWindow = null;

    private File mSaveDir;
    //存放所有图片的路径的集合
    private ArrayList<String> mImageUrlList = new ArrayList<String>();

    private SharedPreferences sp;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_news_detail);

        sp = PreferenceManager.getDefaultSharedPreferences(HotNewsDetailActivity.this);
        isNight = sp.getBoolean("isNight", false);

        dbHelper = new WebCacheDbHelper(this, 1);

        mSaveDir = BitmapUtils.getDiskCacheDir(this, "novcapture");

        //imageLoaderTools = ImageLoaderUtils.getInstance(getApplicationContext());

        intent = getIntent();
        themeName = intent.getStringExtra("themeName");
      //  isNight = intent.getBooleanExtra("isNight", true);
        storiesEntity = (StoriesEntity) intent.getSerializableExtra("entity");

        //初始化View
        initViews();

        setListeners();

        //加载新闻详情数据
        loadNewsData();

        //setupRevealBackground(savedInstanceState);
    }

    private void setListeners() {
        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotNewsDetailActivity.this, NewsCommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("newsId", storiesEntity.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });


        /*mCommentEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //KeyBoardUtils.openKeybord(mCommentEt, HotNewsDetailActivity.this);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mCommentEt, InputMethodManager.SHOW_FORCED);

                }                                     // 接受软键盘输入的编辑文本或其它视图
            }


        });*/

       /* mCommentTv.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotNewsDetailActivity.this, NewsCommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("newsId", storiesEntity.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });*/

    }

    private void initViews() {
        mHeaderImage = (ImageView) findViewById(R.id.iv_header_image);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        //mToolBar.setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        //mToolBar.setTitle(storiesEntity.getTitle());
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        new CommonUtil(this).setStatusBarColor(android.R.color.transparent);

        // setStatusBarColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));


        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        mCollapsingToolbarLayout.setTitle(storiesEntity.getTitle());
        mCollapsingToolbarLayout.setContentScrimColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        mCollapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        mCommentBtn = (FloatingActionButton) findViewById(R.id.btn_comment);
        mProgessBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgessBar.setVisibility(View.VISIBLE);
        mReloadBtn = (Button) findViewById(R.id.btn_reload);
        // mSetNetworkBtn = (Button) findViewById(R.id.btn_set_network);
        mReloadLayout = (FrameLayout) findViewById(R.id.layout_reload);
        mReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNewsData();
            }
        });


        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setDrawingCacheEnabled(true);
        webSettings = mWebView.getSettings();
        // 开启javascript设置
        webSettings.setJavaScriptEnabled(true);
        //设置缓存模式
        //LOAD_CACHE_ELSE_NETWORK模式下，无论是否有网络，只要本地有缓存，都使用缓存。本地没有缓存时才从网络上获取。
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启database storage API功能
        webSettings.setDatabaseEnabled(true);
        // 开启Application Cache功能
        webSettings.setAppCacheEnabled(true);
        webSettings.setSupportZoom(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("截屏中 请稍后...");

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
                //new ScreenCaptureTask(mWebView).execute();

                new CaptureAsyncTask().execute();
                //showPopWindow(bitmap);
                // mCommentBtn.setVisibility(View.VISIBLE);
                break;
            case R.id.action_font_size:
                //修改字体大小
                fontSizeModify();
                break;
            case R.id.action_night_mode:
                isNight = !isNight;
                item.setTitle(isNight ? "日间模式" : "夜间模式");
                mToolBar.setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
                changeWebViewTheme();
                sp.edit().putBoolean("isNight", isNight).apply();
                break;
            default:
                break;
        }
        return true;
    }

    private void changeWebViewTheme() {
        mWebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByClassName(\"origin-source-wrap\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs.setAttribute(\"class\",\"night\");  " +
                "}" +
                "})");
    }

    private void fontSizeModify() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HotNewsDetailActivity.this);
        builder.setTitle(R.string.font_size);
        String[] items = new String[]{"特大字号", "大字号", "中字号", "小字号", "特小字号"};
        builder.setSingleChoiceItems(items, mCurItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectedItem = which;
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (mSelectedItem) {
                    case 0:
                        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        webSettings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        webSettings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                    default:
                        break;
                }
                mCurItem = mSelectedItem;
            }

        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();

    }

    private class CaptureAsyncTask extends AsyncTask<Void, Void, Uri> {


        @Override
        protected void onPreExecute() {
            // onShowLoadingDialog();
            mProgressDialog.show();
            mCommentBtn.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Uri doInBackground(Void... params) {
            Bitmap bitmap = ScreenUtil.snapShotWithoutStatusBar(HotNewsDetailActivity.this);
            return BitmapUtils.saveBitmapToFile(mSaveDir, bitmap);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            //onDismissLoadingDialog();
            mProgressDialog.dismiss();
            Intent intent = new Intent(HotNewsDetailActivity.this, ScreenCaptureActivity.class);
            intent.putExtra("title", storiesEntity.getTitle());
            intent.setData(uri);
            startActivity(intent);
            mCommentBtn.setVisibility(View.VISIBLE);
        }
    }

    private void showPopWindow(Bitmap bitmap) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.layout_screen_capture, null);
        Button cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        Button shareBtn = (Button) view.findViewById(R.id.btn_share_save);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_screen_capture);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(view, (int) (ScreenUtil.getScreenWidth(this) * 0.75), (int) (ScreenUtil.getScreenHeight(this) * 0.75), true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setFocusable(true);
            // 设置点击是否消失
            mPopupWindow.setOutsideTouchable(true);
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable background = new ColorDrawable(0x4f000000);
            mPopupWindow.setBackgroundDrawable(background);
            // mPopupWindow.setAnimationStyle(R.style.);
            imageView.setImageBitmap(bitmap);
        }
        mPopupWindow.showAtLocation(findViewById(R.id.coordinator_layout), Gravity.CENTER, 0, 0);
    }

    /**
     * 加载新闻详情数据
     */
    private void loadNewsData() {
        //先判断数据库中是否有缓存数据 有的话直接读取
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Cache where newsId = " + storiesEntity.getId(), null);
        if (cursor.moveToFirst()) {
            mWebView.setVisibility(View.VISIBLE);
            String json = cursor.getString(cursor.getColumnIndex("json"));
            parseJson(json);
            cursor.close();
            db.close();
        } else {
            //若无缓存 则从网络获取
            //判断网络是否可用
            if (NetworkUtils.isNetworkConnected(this)) {
                mReloadLayout.setVisibility(View.INVISIBLE);
                mProgessBar.setVisibility(View.VISIBLE);
                // 若网络可用 则试着从缓存加载新闻数据 若缓存中没有 则加载网络数据
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
                mProgessBar.setVisibility(View.INVISIBLE);
                mReloadLayout.setVisibility(View.VISIBLE);
                Toast.makeText(HotNewsDetailActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void parseJson(String responseString) {
        Gson gson = new Gson();
        content = gson.fromJson(responseString, Content.class);
        mImageUrlList = getAllImgUrl(content.getBody());
        // imageLoaderTools.displayImage(content.getImage(), mHeaderImage);
        if (Util.isOnMainThread()) {
            Glide.with(getApplicationContext()).load(content.getImage()).placeholder(R.drawable.image_holder).centerCrop().into(mHeaderImage);
        }
        //imageLoaderTools.displayImage(content.getImage(), mHeaderImage);
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news_light.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        // Log.d("Main", html);
        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
        // 添加js交互接口类，并起别名 imagelistner
        mWebView.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
        mWebView.setWebViewClient(new MyWebViewClient());
        mProgessBar.setVisibility(View.INVISIBLE);
        mWebView.setVisibility(View.VISIBLE);
    }

    public String[] getAllImageUrls(String body) {
        String[] imageUrls = null;

        return imageUrls;
    }

    /*@Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= 21) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }*/

    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String imageUrl) {
            //System.out.println(img);
            //
            Intent intent = new Intent(context, ShowWebImageActivity.class);
            intent.putExtra("imageUrl", imageUrl);
            intent.putStringArrayListExtra("imageUrlList", mImageUrlList);
            context.startActivity(intent);
            //System.out.println(img);
        }
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            view.getSettings().setJavaScriptEnabled(true);

            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);

            super.onPageStarted(view, url, favicon);
        }

    }

    // 注入js函数监听 给每一张image添加点击事件
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        mWebView.loadUrl("javascript:(function(){" +
                //"var objs = document.getElementsByTagName(\"img\"); " +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    public class ScreenCaptureTask extends AsyncTask<Void, Void, Bitmap> {

        private WebView webView;

        public ScreenCaptureTask(WebView webView) {
            this.webView = webView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            //   Bitmap bitmap =  BitmapUtils.captureWebViewVisibleSize(webView);
            Bitmap bitmap = BitmapUtils.captureWebView(webView);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mProgressDialog.dismiss();
            if (bitmap != null) {
                Intent intent = new Intent(HotNewsDetailActivity.this, ScreenCaptureActivity.class);
                intent.putExtra("bitmap", bitmap);
                startActivity(intent);
            } else {
                Toast.makeText(HotNewsDetailActivity.this, "截屏失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public static ArrayList<String> getAllImgUrl(String htmlStr) {
        String img = "";
        Pattern p_image;
        Matcher m_image;
        ArrayList<String> imageUrlList = new ArrayList<String>();
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                imageUrlList.add(m.group(1));
            }
        }
        return imageUrlList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Util.isOnMainThread()) {
            Glide.get(getApplicationContext()).clearMemory();
        }
    }
}

