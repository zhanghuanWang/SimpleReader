package com.ywg.simplereader.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ywg.simplereader.R;
import com.ywg.simplereader.adapter.TabLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabLayoutAdapter mAdapter;
    private List<String> mTitleList;
    private List<String> mDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //使用CollapsingToolbarLayout后，title需要设置到CollapsingToolbarLayout上
       // CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbar.setTitle("APP开发者");

        initData();
        mAdapter = new TabLayoutAdapter(this, mTitleList, mDataList);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter);
        //setUpViewPager(mViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
      //  TabLayout.Tab tab1 = mTabLayout.newTab().setText("Tab1");
       // TabLayout.Tab tab2 = mTabLayout.newTab().setText("Tab2");
       // TabLayout.Tab tab3 = mTabLayout.newTab().setText("Tab3");
      //  mTabLayout.addTab(tab1);
      //  mTabLayout.addTab(tab2);
     //   mTabLayout.addTab(tab3);

        //将TabLayout与ViewPager关联起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tab的标题来自PagerAdapter.getPageTitle()。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        TabLayout.TabLayoutOnPageChangeListener pageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(mTabLayout);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        initEvent();
    }

    private void initEvent() {
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
    }

    private void initData() {
        mTitleList = new ArrayList<String>();
        mDataList = new ArrayList<String>();
        for (int i = 1; i <= 10; i++) {
            mTitleList.add("Title" + i);
            mDataList.add("Tab" + i);
        }
    }

}
