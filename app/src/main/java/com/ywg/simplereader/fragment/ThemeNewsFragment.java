package com.ywg.simplereader.fragment;


import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ywg.simplereader.R;
import com.ywg.simplereader.activity.MainActivity;
import com.ywg.simplereader.activity.ThemeNewsDetailActivity;
import com.ywg.simplereader.adapter.CardViewAdapter;
import com.ywg.simplereader.adapter.ThemeNewsItemAdapter;
import com.ywg.simplereader.bean.ADInfo;
import com.ywg.simplereader.bean.Item;
import com.ywg.simplereader.bean.News;
import com.ywg.simplereader.bean.StoriesEntity;
import com.ywg.simplereader.bean.Theme;
import com.ywg.simplereader.constant.Constant;
import com.ywg.simplereader.util.NetworkUtils;
import com.ywg.simplereader.util.PreUtils;
import com.ywg.simplereader.view.RecyclerViewHeader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ThemeNewsFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private CardViewAdapter mAdapter;
    //private ProgressBar mProgressBar;

    private RecyclerViewHeader mRecyclerViewHeader;
    private List<Item> mItemList;

    private boolean isNight;

    private List<ImageView> mImgList = new ArrayList<ImageView>();
    private List<ADInfo> mADInfoList = new ArrayList<ADInfo>();

    private ImageView mHeaderImage;

    private TextView mThemeDes;

    private List<String> mImageList;

    private List<String> imageUrls;
    /*= {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
            "http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
            "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};*/

    private News news;

    private ThemeNewsItemAdapter mThemeNewsItemAdapter;

    private Handler handler = new Handler();

    private Theme theme;

    private String themeId;

    private String themeName;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public ThemeNewsFragment() {
        // Required empty public constructor
    }

    public ThemeNewsFragment(Theme theme) {
        // Required empty public constructor
        this.theme = theme;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme_news, container, false);

        isNight = ((MainActivity) mActivity).isNight();

        themeId = theme.getId();

        themeName = theme.getName();

        Log.d("Main", themeId + "");

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        // mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mHeaderImage = (ImageView) view.findViewById(R.id.iv_header_image);

        mThemeDes = (TextView) view.findViewById(R.id.tv_theme_des);
        mThemeDes.setText(theme.getName());

        initSwipeRefreshLayout(view);

        initRecyclerView(view);

        setListeners();

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
                refreshFragment();
            }
        });

    }

    private void refreshFragment() {
        mSwipeRefreshLayout.setRefreshing(false);
        /*new Thread(){
            @Override
            public void run() {
                loadData();
                mActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mThemeNewsItemAdapter.updateData();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
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

    private void setListeners() {

        mThemeNewsItemAdapter.setOnItemClickLitener(new ThemeNewsItemAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                // Item item = mItemList.get(position);
                // intent.putExtra("Item", item);
                // ActivityOptionsCompat options =ActivityOptionsCompat.makeCustomAnimation(getActivity(),0, R.anim.slide_in_from_right);
                //ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

                // mActivity.overridePendingTransition(0, R.anim.slide_in_from_right);

                StoriesEntity entity = mThemeNewsItemAdapter.getItem(position);
                Intent intent = new Intent(mActivity, ThemeNewsDetailActivity.class);
                // intent.putExtra(Constant.START_LOCATION, startingLocation);
                intent.putExtra("entity", entity);
                intent.putExtra("isNight", ((MainActivity) mActivity).isNight());
                intent.putExtra("themeName", themeName);
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
                // mThemeNewsItemAdapter.updateData();
                startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);

                //更新点击的item数据
                mThemeNewsItemAdapter.updateItem(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Snackbar sb = Snackbar.make(mRecyclerView, "你长按了 item " + position, Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
                sb.show();
            }
        });
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // mRecyclerView.setBackgroundColor(mActivity.getResources().getColor(isNight ? R.color.recyclerview_bg_color_dark : R.color.recyclerview_bg_color_light));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewHeader = (RecyclerViewHeader) view.findViewById(R.id.recycler_view_header);
        mRecyclerViewHeader.attachTo(mRecyclerView, true);
        mThemeNewsItemAdapter = new ThemeNewsItemAdapter(mActivity);
        mRecyclerView.setAdapter(mThemeNewsItemAdapter);

    }

    /**
     * 加载RecyclerView中的数据
     */
    private void loadData() {
        //mImageList = new ArrayList<String>();
        //mItemList = new ArrayList<Item>();
        if (NetworkUtils.isNetworkConnected(mActivity)) {
            OkHttpUtils.get().url(Constant.THEME_NEWS + themeId).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + (Constant.BASE_COLUMN + Integer.parseInt(themeId)) + ",' " + response + "')");
                    db.close();
                    parseJson(response);
                }
            });
        }else {
            //若网络可用 先从缓存中查询 有的话直接从缓存中获取
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + (Constant.BASE_COLUMN + Integer.parseInt(themeId)), null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseJson(json);
                cursor.close();
                db.close();
            } else {
                Toast.makeText(mActivity, "网络不可用", Toast.LENGTH_SHORT).show();
            }

        }

        //mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 解析Json
     *
     * @param responseStr
     */
    private void parseJson(String responseStr) {
        Gson gson = new Gson();
        news = gson.fromJson(responseStr, News.class);

        Glide.with(getContext()).load(news.getImage()).fitCenter().into(mHeaderImage);
       // imageLoaderTools.displayImage(news.getImage(), mHeaderImage);
        Log.d("Main", "ThemeNewsFragment——》" + news.getImage());
        mThemeDes.setText(news.getDescription());

        // Log.d("Main",latest.toString());
        List<StoriesEntity> newsList = news.getStories();

        mThemeNewsItemAdapter.updateData(newsList);

    }

    public void updateTheme() {
        mThemeNewsItemAdapter.updateTheme();
    }

}
