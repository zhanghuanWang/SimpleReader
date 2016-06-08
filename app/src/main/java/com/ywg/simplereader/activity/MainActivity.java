package com.ywg.simplereader.activity;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ywg.simplereader.R;
import com.ywg.simplereader.constant.Constant;
import com.ywg.simplereader.db.CacheDbHelper;
import com.ywg.simplereader.fragment.BackHandledFragment;
import com.ywg.simplereader.fragment.ConcernFragment;
import com.ywg.simplereader.fragment.FoundFragment;
import com.ywg.simplereader.fragment.HomeFragment;
import com.ywg.simplereader.util.AppManager;
import com.ywg.simplereader.util.NetworkUtils;
import com.ywg.simplereader.util.PreUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements BackHandledFragment.BackHandlerInterface {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolBar;
    private TextView mNickNameTv;
    private ImageView mProfileImage;
    private ImageView mQrcodeCardImage;
    private ImageView mQrcodeScanImage;

    private BackHandledFragment selectedFragment;

    private FrameLayout mFragmentContainer;

    private HomeFragment mHomeFragment;
    private FoundFragment mFoundFragment;
    private ConcernFragment mConcernFragment;
    //是否是夜间模式
    private boolean isNight = false;
    private String curId;
    private MenuItem mNightModeItem;

    private CacheDbHelper dbHelper;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    private String[] titles = new String[]{"头条", "娱乐", "科技", "热点", "温州", "社会", "移动互联", "影视", "体育", "段子",
            "头条", "娱乐", "科技", "热点", "温州", "社会", "移动互联", "影视", "体育", "段子"};
    private int themeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 允许使用transitions
        //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_main);

        dbHelper = new CacheDbHelper(this, 1);

        isNight = PreUtils.getPrefBoolean(getApplicationContext(), "isNight", false);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        /*if(PreUtils.getPrefBoolean(getApplicationContext(), "isWifiOffline", true)) {*/
            //若当前是wifi状态下 则进行离线下载
            if(NetworkUtils.isWifiConnected(getApplicationContext())) {
                wifiOfflineDownload();
            }
        /*}*/

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        mToolBar.setTitle(R.string.app_name);
        setSupportActionBar(mToolBar);
        //  setStatusBarColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));

        mFragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

        //initViews();
        initNavigationView();

       // initEvent();

        loadHome();

        Toast.makeText(MainActivity.this,PreUtils.getStringFromDefault(MainActivity.this, "userInfo", null),Toast.LENGTH_LONG);

    }

    private void initViews() {
        mQrcodeCardImage = (ImageView) findViewById(R.id.iv_qrcode_card);
        mQrcodeScanImage = (ImageView) findViewById(R.id.iv_qrcode_scan);
    }

    /**
     * wifi下自动离线下载
     */
    private void wifiOfflineDownload() {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("正在离线下载中...")
                .setContentText("最新新闻")
                .setTicker("开始离线下载")
                .setVibrate(new long[] {0,300,500,700})
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setAutoCancel(true);

        mBuilder.setContentTitle(titles[0]);
        OkHttpUtils.get().url(Constant.LATEST_NEWS).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                SQLiteDatabase db = getCacheDbHelper().getWritableDatabase();
                  /*  SQLiteDatabase db = dbHelper.getWritableDatabase();*/
                db.execSQL("replace into CacheList(date,json) values(" + Constant.LATEST_COLUMN + ",' " + response + "')");
                db.close();
            }
        });

        for(themeId = 2; themeId <= 13; themeId ++) {
            mBuilder.setContentTitle(titles[themeId-1]);
            OkHttpUtils.get().url(Constant.THEME_NEWS + themeId).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    SQLiteDatabase db = getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + (Constant.BASE_COLUMN + Integer.parseInt(String.valueOf(themeId))) + ",' " + response + "')");
                    db.close();
                }
            });
        }

        mBuilder.setContentTitle("成功下载离线新闻").setContentText("无网络时，您也可以阅读新闻了").setProgress(0, 0, false);
        mNotificationManager.notify(3, mBuilder.build());
        //new wifiOfflineDownloadTask().execute();


       /* new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    mBuilder.setProgress(100,i,false);
                    mNotificationManager.notify(3, mBuilder.build());
                    SystemClock.sleep(100);
                }
                mBuilder.setContentText("下载完成").setProgress(0, 0, false);
                mNotificationManager.notify(3, mBuilder.build());

            }
        }).start();*/
    }


    public class wifiOfflineDownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mBuilder.setContentText("下载完成").setProgress(0, 0, false);
            mNotificationManager.notify(3, mBuilder.build());
        }
    }

    private void loadHome() {
        getSupportFragmentManager().beginTransaction()
                //.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                .replace(R.id.fragment_container, new HomeFragment(), "home").
                commit();
        curId = "home";
    }

    private void initNavigationView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        //mNavigationView.setBackgroundColor(MainActivity.this.getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.item_bg_color_light));
        // mNavigationView.setItemBackgroundResource(isNight ? R.color.item_bg_color_dark : R.color.item_bg_color_light);
        View headerView = mNavigationView.inflateHeaderView(R.layout.navigation_head);
        setUpProfileImage(headerView);
        mNavigationView.getMenu().getItem(3).setChecked(isNight);
        mNavigationView.getHeaderView(0).setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        //headerView.setBackgroundColor(MainActivity.this.getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        //ActionBarDrawerToggle可以配合Toolbar，实现Toolbar上菜单按钮开关效果。
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.menu_open, R.string.menu_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置NavigationView的点击事件
        setUpDrawerContent(mNavigationView);
    }

    private void setUpProfileImage(View headerView) {
        mProfileImage = (ImageView) headerView.findViewById(R.id.profile_image);
        mNickNameTv = (TextView) headerView.findViewById(R.id.tv_nick_name);
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭DrawerLayout
                mDrawerLayout.closeDrawers();
                // Toast.makeText(MainActivity.this, "我是 APP开发者", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);

            }
        });
    }


    public void setUpDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_item_home:
                        //设置item被选中
                        menuItem.setChecked(true);
                        switchToHome();
                        break;
                    case R.id.nav_item_concern:
                        //设置item被选中
                        menuItem.setChecked(true);
                        switchToConcern();
                        break;
                    case R.id.nav_item_found:
                        //设置item被选中
                        menuItem.setChecked(true);
                        switchToFound();
                        break;
                    case R.id.nav_item_setting:
                        openSettingActivity();
                        break;

                    case R.id.nav_item_about:
                        openAboutActivity();
                        break;

                    case R.id.nav_item_night_mode:
                        if (menuItem.isChecked()) {
                            Toast.makeText(MainActivity.this, "夜间模式关闭", Toast.LENGTH_SHORT).show();
                            menuItem.setChecked(false);
                        } else {
                            Toast.makeText(MainActivity.this, "夜间模式开启", Toast.LENGTH_SHORT).show();
                            menuItem.setChecked(true);
                        }
                        isNight = !isNight;
                        switchTheme();
                        break;
                    case R.id.nav_item_exit_app:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("提示")
                                .setMessage("确认退出应用?")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AppManager.getAppManager().AppExit(getApplicationContext());
                                    }
                                });
                        builder.create().show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void openAboutActivity() {
        //关闭DrawerLayout
       // mDrawerLayout.closeDrawers();
        Intent intent = new Intent(MainActivity.this, AboutActivtity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    private void openSettingActivity() {
        //关闭DrawerLayout
       // mDrawerLayout.closeDrawers();
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }


    private void switchToHome() {
        //关闭DrawerLayout
        mDrawerLayout.closeDrawers();
        mToolBar.setTitle("首页");
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mHomeFragment, "home").commit();
        curId = "home";
    }

    private void switchToConcern() {
        //关闭DrawerLayout
        mDrawerLayout.closeDrawers();
        mToolBar.setTitle("关注");
        if (mConcernFragment == null) {
            mConcernFragment = new ConcernFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mConcernFragment, "concern").commit();
        curId = "concern";
    }

    private void switchToFound() {
        //关闭DrawerLayout
        mDrawerLayout.closeDrawers();
        mToolBar.setTitle("发现");
        if (mFoundFragment == null) {
            mFoundFragment = new FoundFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFoundFragment, "found").commit();
        curId = "found";
    }


    private void initEvent() {
        mQrcodeCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QrcodeCardActivity.class);
                startActivity(intent);
            }
        });
        mQrcodeScanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        menu.getItem(0).setTitle(isNight ? "日间模式" : "夜间模式");
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
           *//* case R.id.action_add_data:
                //mCardViewAdapter.addData(1);
                break;
            case R.id.action_delete_data:
               // mCardViewAdapter.removeData(1);
                break;*//*
            case R.id.action_night_mode:
                isNight = !isNight;
                item.setTitle(isNight ? "日间模式" : "夜间模式");
               // AppCompatDelegate.setDefaultNightMode
                //切换主题
             //   switchTheme();
                break;
            case R.id.action_scan:
               *//* Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 0);*//*
                break;
            default:
                break;
        }
        return true;
    }*/

    private void switchTheme() {
        mToolBar.setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        //setStatusBarColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        //mNavigationView.setBackgroundColor(MainActivity.this.getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.item_bg_color_light));
        if (curId.equals("home")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(), "home").commit();
        } else if (curId.equals("concern")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConcernFragment(), "concern").commit();
        } else if (curId.equals("found")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FoundFragment(), "found").commit();
        }
        mNavigationView.getHeaderView(0).setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        mNavigationView.getMenu().getItem(3).setChecked(isNight);
        PreUtils.setPrefBoolean(getApplicationContext(), "isNight", isNight);
    }

    /**
     * 监听Back返回键
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (selectedFragment == null || !selectedFragment.onBackPressed()) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                exitAPP();
            }
        }
    }

    private long firstTime = 0;

    /**
     * 退出程序
     */
    public void exitAPP() {
        long secondTime = System.currentTimeMillis();
        //按两次退出程序 若第二次距第一次的时间小于2秒 则直接退出
        if ((secondTime - firstTime) > 2000) {
            //Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            Snackbar sb = Snackbar.make(mFragmentContainer, "再按一次退出应用", Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
            sb.show();
            firstTime = secondTime;
        } else {
            AppManager.getAppManager().AppExit(getApplicationContext());
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment backHandledFragment) {
        this.selectedFragment = backHandledFragment;
    }

    public boolean isNight() {
        return isNight;
    }

    public CacheDbHelper getCacheDbHelper() {
        return dbHelper;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
