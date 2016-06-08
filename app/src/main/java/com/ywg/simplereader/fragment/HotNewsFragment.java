package com.ywg.simplereader.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ywg.simplereader.R;
import com.ywg.simplereader.activity.HotNewsDetailActivity;
import com.ywg.simplereader.activity.MainActivity;
import com.ywg.simplereader.adapter.CardViewAdapter;
import com.ywg.simplereader.adapter.HotNewsItemAdapter;
import com.ywg.simplereader.bean.ADInfo;
import com.ywg.simplereader.bean.Before;
import com.ywg.simplereader.bean.Item;
import com.ywg.simplereader.bean.Latest;
import com.ywg.simplereader.bean.StoriesEntity;
import com.ywg.simplereader.constant.Constant;
import com.ywg.simplereader.db.WebCacheDbHelper;
import com.ywg.simplereader.util.NetworkUtils;
import com.ywg.simplereader.util.PreUtils;
import com.ywg.simplereader.util.TimeUtils;
import com.ywg.simplereader.view.DividerItemDecoration;
import com.ywg.simplereader.view.Kanner;
import com.ywg.simplereader.view.RecyclerViewHeader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotNewsFragment extends BaseFragment {

    private WebCacheDbHelper dbHelper;

    private RecyclerView mRecyclerView;
    private CardViewAdapter mAdapter;
    private ProgressBar mProgressBar;

    private RecyclerViewHeader mRecyclerViewHeader;
    private List<Item> mItemList;

    private boolean isNight;

    private List<ImageView> mImgList = new ArrayList<ImageView>();
    private List<ADInfo> mADInfoList = new ArrayList<ADInfo>();

    private List<String> mImageList;

    private List<String> imageUrls;
    /*= {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
            "http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
            "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};*/

    private Latest latest;
    private String date;
    private Before before;

    private HotNewsItemAdapter mHotNewsItemAdapter;

    private Handler handler = new Handler();

    private Kanner kanner;

    private boolean isLoading = false;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    public HotNewsFragment() {
        // Required empty public constructor
    }

   /* private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(getContext(), msg.toString(), Toast.LENGTH_LONG).show();
        }
    };*/

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_news, container, false);

       // initFlashView(view);

        dbHelper = new WebCacheDbHelper(getContext(), 1);

        isNight = ((MainActivity)mActivity).isNight();

        initViews(view);


        return view;
    }

    private void initViews(View view) {
        initSwipeRefreshLayout(view);
        initRecyclerView(view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    @Override
    protected void initData() {
        super.initData();
        loadData();

    }

    private void initSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

    }

    private void refreshFragment() {

        mSwipeRefreshLayout.setRefreshing(false);
       /* new Thread(){
            @Override
            public void run() {
                loadData();

               *//* mActivity.runOnUiThread(new Runnable() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    @Override
                    public void run() {
                        //mHotNewsItemAdapter.updateData();

                    }
                });*//*
            }
        }.start();*/

        //mViewPager.getCurrentItem().
        /*curId = titles[mTabLayout.getSelectedTabPosition()];
        Fragment fragment = getChildFragmentManager().findFragmentByTag(curId);
        fragment.ref*/
        /*switch (curId) {
            case "home":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(), "home").commit();
                break;
            case "concern":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConcernFragment(), "concern").commit();
                break;
            case "found":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FoundFragment(), "found").commit();
                break;
            default:
                break;
        }*/
    }

    /*private void initFlashView(View view) {
        mFlashView = (FlashView) view.findViewById(R.id.flash_view);
       // imageUrls = new ArrayList<String>();
      *//*  for (int i = 0; i < mImageList.size(); i++) {
            imageUrls.add(mImageList.get(i));
        }*//*
        *//*imageUrls.add("http://www.n63.com/photodir/n63img/?N=X2hiJTI2ZGRXJTVFVyU1RFdrVyU1RGtXJTVFV2tXciU1Q1dlZyU1QiUyNiUyQi5mJTI2b29vJTI3bSU2MHJnWWFwZlklNjAlMjdZJTVEamdjJTI3&v=.jpg");
        imageUrls.add("http://img2.niutuku.com/desk/342/341-20354.jpg");
        imageUrls.add("http://s9.knowsky.com/bizhi/l/25001-35000/200952919065150856.jpg");
        imageUrls.add("http://img4.duitang.com/uploads/item/201302/17/20130217155211_LLREe.jpeg");
        imageUrls.add("http://imgsrc.baidu.com/forum/pic/item/76aa76ec8c70e1ff2e2e210f.jpg");
        mFlashView.setImageUris(imageUrls);*//*
        //mFlashView.setEffect(EffectConstants.CUBE_EFFECT);//更改图片切换的动画效果
        mFlashView.setEffect(EffectConstants.DEFAULT_EFFECT);
        mFlashView.setOnPageClickListener(new FlashViewListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(mActivity, "你的点击的是第" + (position + 1) + "张图片！", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void setListeners() {

        mHotNewsItemAdapter.setOnItemClickLitener(new HotNewsItemAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                // Item item = mItemList.get(position);
                // intent.putExtra("Item", item);
                // ActivityOptionsCompat options =ActivityOptionsCompat.makeCustomAnimation(getActivity(),0, R.anim.slide_in_from_right);
                //ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

                // mActivity.overridePendingTransition(0, R.anim.slide_in_from_right);

                StoriesEntity entity = mHotNewsItemAdapter.getItem(position);
                Intent intent = new Intent(mActivity, HotNewsDetailActivity.class);
                // intent.putExtra(Constant.START_LOCATION, startingLocation);
                intent.putExtra("entity", entity);
                intent.putExtra("isNight", ((MainActivity) mActivity).isNight());
                intent.putExtra("themeName", "热门");
                //保存看过的新闻
                String readSequence = PreUtils.getStringFromDefault(mActivity, "read", "");
                String[] splits = readSequence.split(",");
                StringBuffer sb = new StringBuffer();
                if (splits.length >= 100) {
                    for (int i = 50; i < splits.length; i++) {
                        sb.append(splits[i] + ",");
                    }
                    readSequence = sb.toString();
                }

                if (!readSequence.contains(entity.getId() + "")) {
                    readSequence = readSequence + entity.getId() + ",";
                }
                PreUtils.putStringToDefault(mActivity, "read", readSequence);
                //TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                //tv_title.setTextColor(getResources().getColor(R.color.clicked_tv_textcolor));
                startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                //更新点击的item数据
                mHotNewsItemAdapter.updateData(position);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                Snackbar sb = Snackbar.make(mRecyclerView, "你长按了 item " + position, Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
                sb.show();
            }
        });

        //给RecyclerView添加滚动事件 监听是否滚动到底部
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            //用来标记是否正在向最后一个滑动，既是否向右滑动或向下滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        loadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if (dx > 0 || dy > 0) {
                    //大于0表示，正在向右滚动
                    isSlidingToLast = true;
                } else {
                    //小于等于0 表示停止或向左滚动
                    isSlidingToLast = false;
                }

            }
        });

        /*mFlashView.setOnPageClickListener(new FlashViewListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(mActivity.getApplicationContext(), "你的点击的是第" + (position + 1) + "张图片！", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void initRecyclerView(View view) {
        kanner = (Kanner) view.findViewById(R.id.kanner);
        kanner.setOnItemClickListener(new Kanner.OnItemClickListener() {
            @Override
            public void click(View v, Latest.TopStoriesEntity entity) {
                StoriesEntity storiesEntity = new StoriesEntity();
                storiesEntity.setId(entity.getId());
                storiesEntity.setTitle(entity.getTitle());
                Intent intent = new Intent(mActivity, HotNewsDetailActivity.class);
                intent.putExtra("entity", storiesEntity);
                intent.putExtra("isNight", ((MainActivity) mActivity).isNight());
                startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerViewHeader = (RecyclerViewHeader) view.findViewById(R.id.recycler_view_header);
        mRecyclerViewHeader.attachTo(mRecyclerView, true);
        mHotNewsItemAdapter = new HotNewsItemAdapter(mActivity);
        mRecyclerView.setAdapter(mHotNewsItemAdapter);
        setListeners();
    }

    /**
     * 加载新闻数据
     */
    private void loadData() {
        //mImageList = new ArrayList<String>();
        //mItemList = new ArrayList<Item>();
        isLoading = true;
        //判断网络是否可用 可用则从网络获取
        if (NetworkUtils.isNetworkConnected(mActivity)) {

            OkHttpUtils.get().url(Constant.LATEST_NEWS).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                  /*  SQLiteDatabase db = dbHelper.getWritableDatabase();*/
                    db.execSQL("replace into CacheList(date,json) values(" + Constant.LATEST_COLUMN + ",' " + response + "')");
                    db.close();
                    parseLatestJson(response);
                }
            });
        }else {
            Toast.makeText(mActivity, "网络不可用", Toast.LENGTH_SHORT).show();
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + Constant.LATEST_COLUMN, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLatestJson(json);
                cursor.close();
                db.close();
            } else {
                isLoading = false;
                return;
            }

        }
    }

    /**
     * 解析最新新闻的json数据
     * @param responseStr
     */
    private void parseLatestJson(String responseStr) {
        Gson gson = new Gson();
        latest = gson.fromJson(responseStr, Latest.class);
        date = latest.getDate();

     /*   handler.post(new Runnable() {
            @Override
            public void run() {*/
                List<StoriesEntity> latestStories = latest.getStories();
                List<Latest.TopStoriesEntity> lastestTopStoriesList = latest.getTop_stories();
               /* imageUrls = new ArrayList<String>();
                for(int i = 0; i < lastestTopStories.size(); i++) {
                    imageUrls.add(lastestTopStories.get(i).getImage());
                }
                mFlashView.setImageUris(imageUrls);
                mFlashView.setEffect(EffectConstants.DEFAULT_EFFECT);*/
                /*StoriesEntity topic = new StoriesEntity();
                topic.setType(Constant.TOPIC);
                topic.setTitle("今日热闻");
                latestStories.add(0, topic);*/
                // Log.d("Main", latestStories.toString());
                kanner.setTopEntities(lastestTopStoriesList);
                Log.d("Main", "HotNewsFragment——》" + latestStories.toString());
                StoriesEntity topic = new StoriesEntity();
                topic.setType(Constant.TOPIC);
                topic.setTitle("今日热闻");
                latestStories.add(0, topic);
             //   mHotNewsItemAdapter = new HotNewsItemAdapter(mActivity, latestStories);
              //  mRecyclerView.setAdapter(mHotNewsItemAdapter);
             //   setListeners();

                mHotNewsItemAdapter.refreshList(latestStories);
                isLoading = false;
        /*    }
        });*/
        mProgressBar.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);

    }

    /**
     * 加载更多新闻数据
     */
    private void loadMore() {
        isLoading = true;
        if (NetworkUtils.isNetworkConnected(mActivity)) {
            OkHttpUtils.get().url(Constant.BEFORE + date).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
//                    PreUtils.putStringTo(Constant.CACHE, mActivity, url, responseString);
                    SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + date + ",' " + response + "')");
                    db.close();
                    parseBeforeJson(response);
                }
            });


        } else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + date, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseBeforeJson(json);
            } else {
                db.delete("CacheList", "date < " + date, null);
                isLoading = false;
                Snackbar sb = Snackbar.make(mRecyclerView, "没有更多的离线内容了~", Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(((MainActivity) mActivity).isNight() ? R.color.colorPrimaryDark : R.color.colorPrimary));
                sb.show();
            }
            cursor.close();
            db.close();
        }
    }

    private void parseBeforeJson(String responseString) {
        Gson gson = new Gson();
        before = gson.fromJson(responseString, Before.class);
        if (before == null) {
            isLoading = false;
            return;
        }
        date = before.getDate();
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<StoriesEntity> storiesEntities = before.getStories();
                StoriesEntity topic = new StoriesEntity();
                topic.setType(Constant.TOPIC);
                topic.setTitle(TimeUtils.convertDate(date));
                Log.d("Main",date);
                storiesEntities.add(0, topic);
                mHotNewsItemAdapter.addList(storiesEntities);
                isLoading = false;
            }
        });
    }


    public void updateTheme() {
        mHotNewsItemAdapter.updateTheme();
    }

}
