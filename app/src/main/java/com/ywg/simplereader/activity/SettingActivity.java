package com.ywg.simplereader.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ywg.simplereader.R;
import com.ywg.simplereader.util.DataCleanManager;
import com.ywg.simplereader.util.PreUtils;
import com.ywg.simplereader.util.UpdateManager;
import com.ywg.simplereader.view.IOSSwitchView;

public class SettingActivity extends SlideBackActivity implements View.OnClickListener {

    private Toolbar mToolBar;
    private boolean isNight;
    private SharedPreferences sp;

    private TextView mText;

    private IOSSwitchView mSwitchView;

    private Button mPersonalSettingBtn;
    private RelativeLayout mAutoOfflineLayout;
    private RelativeLayout mClearCacheLayout;
    private TextView mCacheSizeTv;
    private RelativeLayout mCheckUpdateLayout;
    private Button mAboutAppBtn;

    private String mCacheSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sp = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
        isNight = sp.getBoolean("isNight", false);

        initViews();

        setListeners();


        // setStatusBarColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));



    /*@Override
    public void onBackPressed() {
        if(Build.VERSION.SDK_INT >= 21) {
            finishAfterTransition();
        }else {
            super.onBackPressed();
        }
    }*/

    }

    private void initViews() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        mToolBar.setTitle("设置");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    /*    mText = (TextView) findViewById(R.id.tv_text);

        String text = "「非著名程序员」可能是百度一下东半球最好的技术分享公百度一下众号。每天，每百度一下周定时百度一下推送一些有关移动开发的原创文章和教程。 不信你可以\n";
        SpannableString s = KeywordUtil.highlightKeyword(Color.RED, text, "百度一下", "http://www.baidu.com");
        mText.setText(s);
        mText.setLinksClickable(true);
        mText.setMovementMethod(LinkMovementMethod.getInstance());*/

        mPersonalSettingBtn = (Button) findViewById(R.id.btn_personal_setting);
        mSwitchView = (IOSSwitchView) findViewById(R.id.switch_view);
        mSwitchView.setOn(PreUtils.getPrefBoolean(this, "isWifiOffline", true));
        // mAutoOfflineLayout = (RelativeLayout) findViewById(R.id.layout_auto_offline);
        mClearCacheLayout = (RelativeLayout) findViewById(R.id.layout_clear_cache);
        mCacheSizeTv = (TextView) findViewById(R.id.tv_cache_size);
        try {
            mCacheSize = DataCleanManager.getTotalCacheSize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCacheSizeTv.setText(mCacheSize);
        mCheckUpdateLayout = (RelativeLayout) findViewById(R.id.layout_check_update);
        mAboutAppBtn = (Button) findViewById(R.id.btn_about_app);
    }

    private void setListeners() {
        mSwitchView.setOnSwitchStateChangeListener(new IOSSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onStateSwitched(boolean isOn) {
                PreUtils.setPrefBoolean(getApplicationContext(), "isWifiOffline", isOn);
                if (isOn) {
                    Toast.makeText(SettingActivity.this, "开启wifi自动离线", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingActivity.this, "关闭wifi自动离线", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mPersonalSettingBtn.setOnClickListener(this);
        mClearCacheLayout.setOnClickListener(this);
        mCheckUpdateLayout.setOnClickListener(this);
        mAboutAppBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_personal_setting:
                Intent intent1 = new Intent(SettingActivity.this, ProfileActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                break;
            case R.id.layout_clear_cache:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("确定要清除缓存吗？");
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            DataCleanManager.cleanAllCache(getApplicationContext());
                            mCacheSize = DataCleanManager.getTotalCacheSize(getApplicationContext());
                            mCacheSizeTv.setText(mCacheSize);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                break;
            case R.id.layout_check_update:
                //检查版本更新
                new UpdateManager(SettingActivity.this).checkUpdate();
                break;
            case R.id.btn_about_app:
                Intent intent2 = new Intent(SettingActivity.this, AboutActivtity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                break;
            default:
                break;
        }
    }

}
