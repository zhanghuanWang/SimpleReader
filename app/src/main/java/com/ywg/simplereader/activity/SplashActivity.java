package com.ywg.simplereader.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ywg.simplereader.R;
import com.ywg.simplereader.constant.Constant;
import com.ywg.simplereader.util.AppManager;
import com.ywg.simplereader.util.CommonUtil;
import com.ywg.simplereader.util.NetworkUtils;
import com.ywg.simplereader.util.PreUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;

public class SplashActivity extends Activity {

    private ImageView mStartImage;
    private TextView mAppNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_splash);

        new CommonUtil(this).setStatusBarColor(android.R.color.transparent);
        mStartImage = (ImageView) findViewById(R.id.iv_start);
        mAppNameTv = (TextView) findViewById(R.id.tv_app_name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initLaunchImage();
            }
        }).start();

    }

    private void initLaunchImage() {

        File dir = getFilesDir();
        final File imgFile = new File(dir, "ic_launch.jpg");
        if (imgFile.exists()) {
            mStartImage.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        } else {
            mStartImage.setImageResource(R.mipmap.ic_launch);
        }

        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(3000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(PreUtils.getPrefBoolean(SplashActivity.this, "isLogin", false)) {
                    enterMain();
                }else {
                    enterLoginRegister();
                }
                if(NetworkUtils.isNetworkConnected(SplashActivity.this)) {
                    //若有网则加载选项数据
                    OkHttpUtils.get().url(Constant.BASE_URL + Constant.THEMES).build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            //将更新的选项数据保存
                            PreUtils.putStringToDefault(SplashActivity.this, Constant.THEMES, response);
                        }
                    });
                   /* NetworkUtils.get(Constant.THEMES, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            String json = response.toString();
                           //Log.d("Main",json);
                            //将更新的选项数据保存
                            PreUtils.putStringToDefault(SplashActivity.this, Constant.THEMES, json);
                        }
                    });*/
                } else {
                    Toast.makeText(SplashActivity.this, "没有网络连接!", Toast.LENGTH_LONG).show();
                    enterMain();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mStartImage.startAnimation(scaleAnim);
    }

    private void enterMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }

    private void enterLoginRegister() {
        Intent intent = new Intent(SplashActivity.this, LoginRegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }

    public void saveImage(File file, byte[] bytes) {
        try {
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
