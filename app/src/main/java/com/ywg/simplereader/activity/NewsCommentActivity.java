package com.ywg.simplereader.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ywg.simplereader.R;
import com.ywg.simplereader.adapter.NewsCommentAdapter;
import com.ywg.simplereader.bean.LongComments;
import com.ywg.simplereader.bean.NewsComment;
import com.ywg.simplereader.bean.NewsExtraInfo;
import com.ywg.simplereader.bean.ShortComments;
import com.ywg.simplereader.constant.Constant;
import com.ywg.simplereader.util.NetworkUtils;
import com.ywg.simplereader.util.KeyBoardUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class NewsCommentActivity extends SlideBackActivity {

    //private RecyclerView mShortCommentsRecyclerView;

    // private RecyclerView mLongCommentsRecyclerView;

    private ListView mLongCommentsListView;

    private NewsCommentAdapter mLongCommentAdapter;

    private List<NewsComment> mLongCommentList;

    private ListView mShortCommentsListView;

    private NewsCommentAdapter mShortCommentAdapter;

    private List<NewsComment> mShortCommentList;

    private Toolbar mToolbar;

    private String newsId;

    private TextView mShortCommentsNum;

    private TextView mLongCommentsNum;

    private EditText mInputEt;

    private Button mSendBtn;

    private ProgressBar mProgressBar;

    private LinearLayout mContentLayout;
    private FrameLayout mRefreshLayout;
    private FrameLayout mReloadLayout;

    private Button mReloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_comment);
        //获得新闻
        newsId = getIntent().getStringExtra("newsId");

        Log.d("Main", newsId + "");
        initViews();

        setListeners();
        loadCommentData();

    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // mToolbar.setTitle("新闻评论");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*mShortCommentsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_short_comments);
        mShortCommentsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mShortCommentsRecyclerView.setLayoutManager(layoutManager);
        mShortCommentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mShortCommentsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        //mCommentList = new ArrayList<String>(Arrays.asList(Cheeses.sCheeses));
        mShortCommentList = new ArrayList<NewsComment>();
        mShortCommentAdapter = new NewsCommentAdapter(this, mShortCommentList);
        mShortCommentsRecyclerView.setAdapter(mShortCommentAdapter);*/

   /*    mLongCommentsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_long_comments);
        mLongCommentsRecyclerView.setHasFixedSize(true);
        mLongCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLongCommentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLongCommentsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        //mCommentList = new ArrayList<String>(Arrays.asList(Cheeses.sCheeses));
        mLongCommentList = new ArrayList<NewsComment>();
        mLongCommentAdapter = new NewsCommentAdapter(this, mLongCommentList);
        mLongCommentsRecyclerView.setAdapter(mLongCommentAdapter);*/

        mShortCommentsListView = (ListView) findViewById(R.id.list_view_short_comments);
        View shortHeaderView = LayoutInflater.from(this).inflate(R.layout.news_comment_header, null);
        mShortCommentsListView.addHeaderView(shortHeaderView);
        mShortCommentList = new ArrayList<NewsComment>();
        mShortCommentAdapter = new NewsCommentAdapter(this, mShortCommentList);
        mShortCommentsListView.setAdapter(mShortCommentAdapter);

        mShortCommentsNum = (TextView) shortHeaderView.findViewById(R.id.tv_num);

       /* mLongCommentsListView = (ListView) findViewById(R.id.list_view_long_comments);
        View longHeaderView = LayoutInflater.from(this).inflate(R.layout.news_comment_header, null);
        mLongCommentsListView.addHeaderView(longHeaderView);
        mLongCommentList = new ArrayList<NewsComment>();
        mLongCommentAdapter = new NewsCommentAdapter(this, mLongCommentList);
        mLongCommentsListView.setAdapter(mLongCommentAdapter);

        mLongCommentsNum = (TextView) longHeaderView.findViewById(R.id.tv_num);*/

        mInputEt = (EditText) findViewById(R.id.et_input);
        mSendBtn = (Button) findViewById(R.id.btn_send);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mContentLayout = (LinearLayout) findViewById(R.id.layout_content);
        mRefreshLayout = (FrameLayout) findViewById(R.id.layout_refresh);
        mReloadLayout = (FrameLayout) findViewById(R.id.layout_reload);
        mReloadBtn = (Button) findViewById(R.id.btn_reload);
    }



    private void setListeners() {
        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    mSendBtn.setEnabled(true);
                } else {
                    mSendBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInputEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mSendBtn.setVisibility(View.VISIBLE);
                } else {
                    mSendBtn.setVisibility(View.GONE);
                }
            }
        });
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = mInputEt.getText().toString().trim();
                //点击发送之后 清空输入框 隐藏软键盘
                mInputEt.setText("");
                KeyBoardUtils.hideKeybord(v, NewsCommentActivity.this);
            }
        });
        mReloadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefreshLayout.setVisibility(View.VISIBLE);
                loadCommentData();
            }
        });
        mReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCommentData();
            }
        });

    }

    /**
     * 加载新闻评论数据
     */
    private void loadCommentData() {
        if (NetworkUtils.isNetworkConnected(this)) {
            mReloadLayout.setVisibility(View.INVISIBLE);
            mRefreshLayout.setVisibility(View.VISIBLE);
            OkHttpUtils.get().url(Constant.BASE_URL + Constant.NEWS_EXTRA + newsId).build().execute(new StringCallback() {

                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    parseCommentJson(response);
                }
            });

            OkHttpUtils.get().url(Constant.BASE_URL +Constant.COMMENT + newsId + "/short-comments").build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    parseShortCommentsJson(response);
                    mRefreshLayout.setVisibility(View.INVISIBLE);
                    mContentLayout.setVisibility(View.VISIBLE);
                }
            });

        }else {
            mContentLayout.setVisibility(View.INVISIBLE);
            mRefreshLayout.setVisibility(View.INVISIBLE);
            mReloadLayout.setVisibility(View.VISIBLE);
            Toast.makeText(NewsCommentActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
        }
    }

    private void 此(String responseString) {
        Gson gson = new Gson();
        final LongComments longComments = gson.fromJson(responseString, LongComments.class);
        List<NewsComment> longCommentList = longComments.getComments();
        mLongCommentAdapter.addList(longCommentList);
    }

    /**
     * 解析短评论数据
     * @param responseString
     */
    private void parseShortCommentsJson(String responseString) {
        Gson gson = new Gson();
        final ShortComments shortComments = gson.fromJson(responseString, ShortComments.class);
        List<NewsComment> shortCommentList = shortComments.getComments();
        mShortCommentAdapter.addList(shortCommentList);
    }

    /**
     * 解析新闻有几条评论
     * @param responseString
     */
    private void parseCommentJson(String responseString) {
        Gson gson = new Gson();
        NewsExtraInfo comment = gson.fromJson(responseString, NewsExtraInfo.class);
        //mCommentTv.setText(comment.getComments());
        mToolbar.setTitle(comment.getComments() + "条评论");
        mShortCommentsNum.setText(comment.getShort_comments() + "条短评");
        //mLongCommentsNum.setText(comment.getLong_comments() + "条长评");
    }

    /**
     * 点击EditText以外的区域后软键盘隐藏
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //获取当前获得当前焦点所在View
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                //如果不是edittext，则隐藏软键盘
                KeyBoardUtils.hideKeybord(v, this);
                mInputEt.clearFocus();
            }
            return super.dispatchTouchEvent(ev);
        }
        /**
         *  看源码可知superDispatchTouchEvent  是个抽象方法，用于自定义的Window此处目的是为了继续将事件由dispatchTouchEvent ( MotionEvent event ) 传递到onTouchEvent ( MotionEvent event )
         必不可少，否则所有组件都不能触发
         */
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            //此处根据输入框左上位置和宽高获得右下位置
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
