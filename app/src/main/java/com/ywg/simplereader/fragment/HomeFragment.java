package com.ywg.simplereader.fragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ywg.simplereader.R;
import com.ywg.simplereader.activity.MainActivity;
import com.ywg.simplereader.adapter.CheeseDynamicAdapter;
import com.ywg.simplereader.adapter.ListViewAdapter;
import com.ywg.simplereader.adapter.NewsAdapter;
import com.ywg.simplereader.adapter.TabLayoutAdapter;
import com.ywg.simplereader.bean.Theme;
import com.ywg.simplereader.constant.Constant;
import com.ywg.simplereader.db.WebCacheDbHelper;
import com.ywg.simplereader.util.NetworkUtils;
import com.ywg.simplereader.util.PreUtils;
import com.ywg.simplereader.view.draggridview.DynamicGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class HomeFragment extends BaseFragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NewsAdapter mNewsAdapter;
    private String[] titles = new String[]{"头条", "娱乐", "科技", "热点", "温州", "社会", "移动互联", "影视", "体育", "段子",
            "头条", "娱乐", "科技", "热点", "温州", "社会", "移动互联", "影视", "体育", "段子"};
    private List<String> mTitleList;
    private List<Fragment> mFragmentList;

    //private SwipeRefreshLayout mSwipeRefreshLayout;

    private TabLayoutAdapter mTabLayoutAdapter;

    private boolean isNight;

    private String curId;

    private View view = null;

   private List<Theme> mThemeList;

    private Handler mHandler = new Handler();

    private ImageButton mDownOpenBtn;

    private PopupWindow mPopupWindow;

    private ListViewAdapter mListViewAdapter;

    private ListView mNotConcernedListView;

    private List<String> mNotConcernedList;

    private WebCacheDbHelper dbHelper;

    /*public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 0:
                    Toast.makeText(getContext(),  (String)msg.obj,  Toast.LENGTH_SHORT).show();
                    Log.d("Test", "CustomThread receive msg:" + (String) msg.obj);
            }

        }
    };*/
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        isNight = ((MainActivity) mActivity).isNight();

        dbHelper = new WebCacheDbHelper(getContext(), 1);

        // initCycleViewPager(view);
        // initRecyclerView(view);

        // mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        //  initSwipeRefreshLayout(view);

       // loadData();

        getData();

        //DownOpenBtn = (ImageButton) view.findViewById(R.id.btn_down_open);
        mNewsAdapter = new NewsAdapter(mActivity, getChildFragmentManager(), mFragmentList, mTitleList);
       // mNewsAdapter = new NewsAdapter(mActivity, getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(mNewsAdapter);

        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mTabLayout.setBackgroundColor(mActivity.getResources().getColor(isNight ? R.color.tab_bg_dark : R.color.tab_bg_light));
        //将TabLayout与ViewPager关联起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tab的标题来自PagerAdapter.getPageTitle()。
        mTabLayout.setTabsFromPagerAdapter(mNewsAdapter);
        TabLayout.TabLayoutOnPageChangeListener pageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(mTabLayout);
        mViewPager.addOnPageChangeListener(pageChangeListener);

        setListeners();

        return view;
    }


    //
    @Override
    protected void initData() {
        super.initData();
      // loadData();
    }

    private void loadData() {
        mTitleList = new ArrayList<String>();
        mTitleList.add("热门");
        mThemeList = new ArrayList<Theme>();
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(new HotNewsFragment());
        //判断网络是否可用
        if (NetworkUtils.isNetworkConnected(mActivity)) {
            //若有网则加载选项数据

            OkHttpUtils.get().url(Constant.SERVER_BASE_URL + Constant.THEME_URL).build().execute(new StringCallback() {

                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                   // handler.obtainMessage(0, mTitleList.toString());
                   // PreUtils.putStringToDefault(mActivity, Constant.THEMES, response);
                   // parseJson(response);
                }

            });
        } else {
            //若没网则直接加载先前保存的选项数据
            String json = PreUtils.getStringFromDefault(mActivity, Constant.THEMES, "");
            // JSONObject jsonObject = new JSONObject(json);
            //parseJson(json);
        }
    }

    private void setListeners() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /*mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override


            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mSwipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mSwipeRefreshLayout.setEnabled(true);
                        break;

                }
                return false;

            }
        });*/

       /* mDownOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ColumnChooseActivity.class);
                startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);*//*
                *//*showPopupWindow(mTabLayout);*//*
                *//*Intent intent = new Intent(getContext(), LabelManageActivity.class);
                startActivity(intent);
            }
        });*/
    }


    /**
     * 解析Json数据
     *
     * @param response
     */
    private void parseJson(String response) {
        Gson gson = new Gson();
        Theme[] themeList = gson.fromJson(response, Theme[].class);
        for (int i = 0; i < themeList.length; i++) {
            Theme theme = themeList[i];
            mTitleList.add(theme.getName());
            mFragmentList.add(new ThemeNewsFragment(theme));
           // mThemeList.add(theme);
        }
        mNewsAdapter.addData(mFragmentList, mTitleList);
        //Toast.makeText(getContext(),mThemeList.toString(), Toast.LENGTH_SHORT).show();
       /* for (int i = 0; i < mThemeList.size(); i++) {
            mFragmentList.add(new ThemeNewsFragment(mThemeList.get(i)));
            mTitleList.add(mThemeList.get(i).getName());
        }*/
            /*JSONObject jsonObject = new JSONObject(response);
            JSONArray themesArray = jsonObject.getJSONArray("others");

            for (int i = 0; i < themesArray.length(); i++) {
                Theme theme = new Theme();
                JSONObject itemObject = themesArray.getJSONObject(i);
                theme.setDescription(itemObject.getString("description"));
                theme.setId(itemObject.getString("id"));
                theme.setName(itemObject.getString("name"));
                theme.setThumbnail(itemObject.getString("thumbnail"));
                mThemeList.add(theme);
                // for(int j = 0; j < mThemeList.size(); j++) {
                //     mFragmentList.add(new HotNewsFragment());
                //    }

                mTitleList.add(itemObject.getString("name"));
            }
            // mNewsAdapter.addData(mThemeList);*/
    }

    public void getData() {
        mTitleList = new ArrayList<String>();
        mTitleList.add("热门");
        mThemeList = new ArrayList<Theme>();
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(new HotNewsFragment());

        /*if(NetworkUtils.isNetworkConnected(getContext())) {
            //若有网则加载选项数据
            OkHttpUtils.get().url(Constant.BASE_URL + Constant.THEMES).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    //Log.d("Main",json);
                    //将更新的选项数据保存
                    PreUtils.putStringToDefault(getContext(), Constant.THEMES, response);
                    parseJson1(response);
                }
            });
        }else {*/
            //若没网则直接加载先前保存的选项数据
            String json = PreUtils.getStringFromDefault(getContext(), Constant.THEMES, "");
            parseJson1(json);
        /*}*/

    }
    /**
     * 解析Json数据
     *
     * @param response
     */
    private void parseJson1(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray itemsArray = jsonObject.getJSONArray("others");
            for (int i = 0; i < itemsArray.length(); i++) {
                Theme theme = new Theme();
                JSONObject itemObject = itemsArray.getJSONObject(i);
                theme.setDescription(itemObject.getString("description"));
                theme.setId(itemObject.getString("id"));
                theme.setName(itemObject.getString("name"));
                theme.setThumbnail(itemObject.getString("thumbnail"));
                mThemeList.add(theme);
                mTitleList.add(itemObject.getString("name"));
            }

            for (int i = 0; i < mTitleList.size() - 1; i++) {
                Theme theme = mThemeList.get(i);
                mFragmentList.add(new ThemeNewsFragment(theme));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void showPopupWindow(View view) {
        // 如果正在显示则不处理
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        // 如果没有初始化则初始化
        LinearLayout popupView = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.layout_column_switch, null);
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //点击弹窗以外区域隐藏弹窗
        mPopupWindow.setOutsideTouchable(true);
        //获取焦点
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x44000000));
      //  mPopupWindow.setAnimationStyle(R.style.Popupwindow);
       // int[] location = new int[2];
        //view.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
       // mPopupWindow.showAsDropDown(view);

        //setListeners(popupView);
    }

    private void dismissPopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    private void setListeners(LinearLayout layout) {
        final DynamicGridView gridView = (DynamicGridView) layout.findViewById(R.id.dynamic_grid);
        final CheeseDynamicAdapter mCheeseDynamicAdapter = new CheeseDynamicAdapter(mActivity, mTitleList,
                getResources().getInteger(R.integer.column_count));
        gridView.setAdapter(mCheeseDynamicAdapter);

        mNotConcernedListView = (ListView) layout.findViewById(R.id.list_view);
        mNotConcernedList  = new ArrayList<String>();
        mListViewAdapter = new ListViewAdapter(mActivity, mNotConcernedList);
        mNotConcernedListView.setAdapter(mListViewAdapter);

        //拖动结束后
        gridView.setOnDropListener(new DynamicGridView.OnDropListener() {
            @Override
            public void onActionDrop() {
                gridView.stopEditMode();
            }
        });
        //
        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                // Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                // Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String column = (String) parent.getAdapter().getItem(position);
                Toast.makeText(mActivity, parent.getAdapter().getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();
                mCheeseDynamicAdapter.remove(column);
                mListViewAdapter.insert(column, mListViewAdapter.getList().size());
            }
        });

        ImageButton upCloseBtn = (ImageButton) layout.findViewById(R.id.btn_up_close);
        upCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
               // List<String> itemList = mCheeseDynamicAdapter.getItems().t;
               // mTabLayoutAdapter.updateList(itemList);
            }
        });

        mListViewAdapter.setOnAddClickListener(new ListViewAdapter.OnAddClickListener() {
            @Override
            public void onClick(int position, boolean add) {
                mListViewAdapter.remove(position);
                String column = mListViewAdapter.getList().get(position);
                mCheeseDynamicAdapter.add(1, column);
            }
        });
    }

    public void updateTheme() {
        mTabLayout.setBackgroundColor(mActivity.getResources().getColor(isNight ? R.color.tab_bg_dark : R.color.tab_bg_light));
    }

    /**
     * 确保在Activity退出前先关闭PopupWindow。
     */
    @Override
    public void onStop() {
        dismissPopupWindow();
        super.onStop();

    }

}
