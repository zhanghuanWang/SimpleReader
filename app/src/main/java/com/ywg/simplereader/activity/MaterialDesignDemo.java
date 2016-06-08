package com.ywg.simplereader.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ywg.simplereader.R;
import com.ywg.simplereader.adapter.CardViewAdapter;
import com.ywg.simplereader.adapter.TabLayoutAdapter;
import com.ywg.simplereader.bean.Item;

import java.util.ArrayList;
import java.util.List;

public class MaterialDesignDemo extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolBar;
    private RecyclerView mRecyclerView;
    private CardViewAdapter mCardViewAdapter;
    private List<Item> mItemList;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabLayoutAdapter mTabLayoutAdapter;
    private List<String> mTitleList;
    private List<String> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_design_demo);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        initData();

        initNavigationView();

       // initRecyclerView();

        initTabLayout();

        initEvent();
    }

    private void initTabLayout() {
        mTabLayoutAdapter = new TabLayoutAdapter(this, mTitleList, mDataList);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mTabLayoutAdapter);
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
        mTabLayout.setTabsFromPagerAdapter(mTabLayoutAdapter);
        TabLayout.TabLayoutOnPageChangeListener pageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(mTabLayout);
        mViewPager.addOnPageChangeListener(pageChangeListener);
    }

    private void initNavigationView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        //ActionBarDrawerToggle可以配合Toolbar，实现Toolbar上菜单按钮开关效果。
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolBar,R.string.menu_open,R.string.menu_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置NavigationView的点击事件
        setUpDrawerContent(mNavigationView);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCardViewAdapter = new CardViewAdapter(this, mItemList);
        mRecyclerView.setAdapter(mCardViewAdapter);
    }

    public void setUpDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_item_home:
                        // switchToHome();
                        break;
                    case R.id.nav_item_concern:
                        //switchToConcern();
                        break;
                    case R.id.nav_item_found:
                        // switchToAbout();
                        break;

                }
                //设置item被选中
                menuItem.setChecked(true);
                //关闭DrawerLayout
                mDrawerLayout.closeDrawers();
                return  true;
            }
        });
    }

    private void initEvent() {

      /*mCardViewAdapter.setOnItemClickLitener(new CardViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MaterialDesignDemo.this, position + " clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MaterialDesignDemo.this,position + " long clicked", Toast.LENGTH_SHORT).show();
            }
        });*/

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

    }

    private void initData() {
        mItemList = new ArrayList<Item>();
        for (int i = 0; i < 20; i++) {
            Item item = new Item(R.mipmap.ic_launcher,"APP开发者" + i, "本文主要介绍CardView的使用，\nCardView是继承自FrameLayout，\n" +
                    "使用比较简单，只需要用CardView包含其他View就可以实现卡片效果了。\n" +
                    "实现效果如下：");
            mItemList.add(item);
        }

        mTitleList = new ArrayList<String>();
        mDataList = new ArrayList<String>();
        for (int i = 1; i <= 4; i++) {
            mTitleList.add("Title" + i);
            mDataList.add("Tab" + i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
           /* case R.id.action_add_data:
                mCardViewAdapter.addData(1);
                break;
            case R.id.action_delete_data:
                mCardViewAdapter.removeData(1);
                break;*/
            default:
                break;
        }
        return true;
    }
}
