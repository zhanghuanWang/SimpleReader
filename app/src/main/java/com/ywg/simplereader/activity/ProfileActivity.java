package com.ywg.simplereader.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ywg.simplereader.R;
import com.ywg.simplereader.bean.User;
import com.ywg.simplereader.util.ImeUtil;
import com.ywg.simplereader.util.PreUtils;
import com.ywg.simplereader.view.CircleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 个人简介的界面
 */
public class ProfileActivity extends SlideBackActivity implements View.OnClickListener {

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;//本地
    private static final int CODE_CAMERA_REQUEST = 0xa1;//拍照
    private static final int CODE_RESULT_REQUEST = 0xa2;//最终裁剪后的结果

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 600;
    private static int output_Y = 600;

    private final String[] items = new String[]{"拍照","图库",};

    private Toolbar mToolbar;
    private boolean isNight;
    private SharedPreferences sp;

    private CircleImageView mHeadImage;
    private RelativeLayout mHeadImageLayout;
    private RelativeLayout mNickNameLayout;
    private RelativeLayout mPwdEditLayout;

    private TextView mName1Tv;
    private TextView mName2Tv;

    private Button mLogoutBtn;

    private User user;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(ProfileActivity.this,"修改成功！！",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(ProfileActivity.this,"修改失败！！",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sp = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        isNight = sp.getBoolean("isNight", false);


        initViews();

        setListeners();
       /* mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        //setStatusBarColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));

    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.profile);
        setSupportActionBar(mToolbar);
        //显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // String userJson =  PreUtils.getStringFromDefault(ProfileActivity.this, "userInfo", null);
        //Gson gson = new Gson();
        //user = gson.fromJson(userJson, User.class);

        mHeadImage = (CircleImageView) findViewById(R.id.iv_head_image);
        //Glide.with(ProfileActivity.this).load(user.getAvatar()).into(mHeadImage);
        mHeadImageLayout = (RelativeLayout) findViewById(R.id.layout_head_image);
        mNickNameLayout = (RelativeLayout) findViewById(R.id.layout_nick_name);
        mPwdEditLayout = (RelativeLayout) findViewById(R.id.layout_pwd);

        mName1Tv = (TextView) findViewById(R.id.tv_name1);
        mName2Tv = (TextView) findViewById(R.id.tv_name2);
      //  mName1Tv.setText(user.getName());
       //   mName2Tv.setText(user.getName());

        mLogoutBtn = (Button) findViewById(R.id.btn_logout);

    }

    private void setListeners() {
        //mHeadImage.setOnClickListener(this);
        mHeadImageLayout.setOnClickListener(this);
        mNickNameLayout.setOnClickListener(this);
        mPwdEditLayout.setOnClickListener(this);
        mLogoutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_head_image:
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setTitle(R.string.set_headimage);
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choseHeadImageFromCameraCapture();
                                break;
                            case 1:
                                choseHeadImageFromGallery();
                                break;
                            default:
                                break;

                        }
                    }
                });
                builder.create().show();
                break;
            case R.id.layout_nick_name:
                final EditText nickNameEt = new EditText(getApplicationContext());
                nickNameEt.setCursorVisible(true);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProfileActivity.this);
                builder1.setTitle(R.string.nick_name_edit);
                builder1.setView(nickNameEt);
                builder1.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImeUtil.hideSoftKeyboard(nickNameEt);
                    }
                });
                builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImeUtil.hideSoftKeyboard(nickNameEt);
                    }
                });
                builder1.setCancelable(false);
                builder1.create().show();
                ImeUtil.showSoftKeyboard(nickNameEt);
                break;
            case R.id.layout_pwd:
                final EditText pwdEt = new EditText(getApplicationContext());
                pwdEt.setCursorVisible(true);
                AlertDialog.Builder builder2 = new AlertDialog.Builder(ProfileActivity.this);
                builder2.setTitle(R.string.pwd_edit);
                builder2.setView(pwdEt);
                builder2.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImeUtil.hideSoftKeyboard(pwdEt);
                       /* String pwd = pwdEt.getText().toString().trim();
                       if(pwd != user.getPassword()) {
                           OkHttpUtils.post().addParams("phone",user.getPhone()).addParams("password",pwd).url(Constant.SERVER_BASE_URL + Constant.CHANGE_PWD_URL).build().execute(new StringCallback() {
                               @Override
                               public void onError(Call call, Exception e) {

                               }

                               @Override
                               public void onResponse(String s) {
                                    if(s.equals("success")) {
                                       mHandler.sendEmptyMessage(0);
                                     }else {
                                        mHandler.sendEmptyMessage(1);
                                    }
                               }
                           });

                       }*/
                    }
                });
                builder2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImeUtil.hideSoftKeyboard(pwdEt);
                    }
                });
                builder2.setCancelable(false);
                builder2.create().show();
                ImeUtil.showSoftKeyboard(pwdEt);
                break;
            case R.id.btn_logout:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(ProfileActivity.this);
                builder3.setTitle("确定要退出登录");
                builder3.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreUtils.putStringToDefault(getApplicationContext(),"userJson",null);
                        PreUtils.setPrefBoolean(getApplicationContext(), "isLogin", false);
                        Intent intent= new Intent(ProfileActivity.this, SplashActivity.class);
                        startActivity(intent);
                    }
                });
                builder3.setNegativeButton(R.string.cancel, null);
                builder3.create().show();

                break;
            default:
                break;
        }
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");//选择图片
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        //如果你想在Activity中得到新打开Activity关闭后返回的数据，
        //你需要使用系统提供的startActivityForResult(Intent intent,int requestCode)方法打开新的Activity
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {//取消
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST://如果是来自本地的
                cropRawPhoto(intent.getData());//直接裁剪图片
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);//设置图片框
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        //把裁剪的数据填入里面

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            mHeadImage.setImageBitmap(photo);

            //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
            File nf = new File(Environment.getExternalStorageDirectory() + "/Ask");
            nf.mkdir();

            //在根目录下面的ASk文件夹下 创建okkk.jpg文件
            File f = new File(Environment.getExternalStorageDirectory() + "/Ask", "okkk.jpg");

            FileOutputStream out = null;
            try {
                //打开输出流 将图片数据填入文件中
                out = new FileOutputStream(f);
                photo.compress(Bitmap.CompressFormat.PNG, 90, out);

                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

}
