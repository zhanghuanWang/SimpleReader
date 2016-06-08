package com.ywg.simplereader.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ywg.simplereader.R;
import com.ywg.simplereader.adapter.GuideAdapter;
import com.ywg.simplereader.constant.Constant;
import com.ywg.simplereader.util.AppManager;
import com.ywg.simplereader.util.CodeUtils;
import com.ywg.simplereader.util.ImeUtil;
import com.ywg.simplereader.util.PreUtils;
import com.ywg.simplereader.util.RegUtils;
import com.ywg.simplereader.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;

public class LoginRegisterActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private int[] guide_images = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3};
    private CircleIndicator mCircleIndicator;

    private Button mLoginBtn;

    private Button mRegisterBtn;

    private EditText mLoginPhone;
    private EditText mLoginPwd;
    private EditText mRegisterPhone;
    private EditText mRegisterPwd;
    private EditText mRegisterName;
    private EditText mRegisterCode;
    private ImageView mRegisterImgCode;

    private EditText mRegisterPhone1;
    private EditText mRegisterMsgCode;
    private Button mGetVerificationCodeBtn;

    private String login_phone;
    private String login_pwd;
    private String register_phone;
    private String register_pwd;
    private String register_name;
    private String register_code;
    private String getCode;

    private boolean isLoginSuccessed = false;

    private boolean phoneIsExist = true;
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "e93304e66b9b";

    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "d551bab4c4decfb0e9a3a38d28325ab2";

    //注册短信回调事件
    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
           /* LogUtils.i("event:" + event + "    result:" + result + "    data:" + data.toString());*/
            switch (event) {
                //验证验证码是否正确
                case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //验证成功后打开登录界面
                        //userLogin();
                        Toast.makeText(LoginRegisterActivity.this, "注册成功！！", Toast.LENGTH_SHORT).show();
                        //将此账号信息存到数据库中
                        OkHttpUtils.post().addParams("phone", register_phone).addParams("password", register_pwd).addParams("name", register_name).url(Constant.SERVER_BASE_URL + Constant.SAVE_USER_INFO_URL).build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {

                            }

                            @Override
                            public void onResponse(String s) {

                            }
                        });

                    } else {
                        //验证失败
                    }
                    break;
                //获取验证码
                case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //获取验证码成功
                        //默认的智能验证是开启的,我已经在后台关闭
                    } else {
                        //获取验证码失败
                    }
                    break;
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 0:
                    ToastUtils.showLong(LoginRegisterActivity.this, "注册失败");
                    break;
                case 1:
                    ToastUtils.showLong(LoginRegisterActivity.this, "注册成功");
                    break;
                case 2:
                    ToastUtils.showLong(LoginRegisterActivity.this, "该账号已存在");
                case 3:
                   // ToastUtils.showLong(getApplicationContext(), (String) msg.obj);
                    PreUtils.setPrefBoolean(LoginRegisterActivity.this, "isLogin", true);
                    Toast.makeText(getApplicationContext(), "登录成功!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                case 4:
                    ToastUtils.showLong(LoginRegisterActivity.this, "该账号尚未注册简读!");
                    break;
                case 6:
                    ToastUtils.showLong(getApplicationContext(), (String) msg.obj);
                    break;
                default:
                    break;
            }

        }
    };
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);*/
       /* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }*/
        setContentView(R.layout.activity_login_register);

        AppManager.getAppManager().addActivity(this);
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        SMSSDK.registerEventHandler(eh); //注册短信回调

        initViews();

        initViewPager();

        setListener();

    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mCircleIndicator = (CircleIndicator) findViewById(R.id.indicator);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mRegisterBtn = (Button) findViewById(R.id.btn_register);
    }

    private void initViewPager() {
        mViewPager.setAdapter(new GuideAdapter(guide_images));
        mCircleIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }

    private void setListener() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister1();
            }
        });
    }

    /**
     * 用户注册的第一步
     */
    private void userRegister1() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_register_dialog1, null);
        mRegisterPhone = (EditText) view.findViewById(R.id.et_phone);
        mRegisterPwd = (EditText) view.findViewById(R.id.et_pwd);
        mRegisterName = (EditText) view.findViewById(R.id.et_name);
        mRegisterCode = (EditText) view.findViewById(R.id.et_code);
        mRegisterImgCode = (ImageView) view.findViewById(R.id.iv_code);
        mRegisterImgCode.setImageBitmap(CodeUtils.getInstance().createBitmap());
        getCode = CodeUtils.getInstance().getCode();
        mRegisterImgCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击图片验证码重新生成图片验证码
                mRegisterImgCode.setImageBitmap(CodeUtils.getInstance().createBitmap());
                getCode = CodeUtils.getInstance().getCode();
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.btn_register);
        builder.setView(view);
        builder.setPositiveButton(R.string.btn_register, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //检验注册信息
                checkRegisterInfo();
            }


        });
        builder.show();
        ImeUtil.showSoftKeyboard(mRegisterPhone); //强制账号输入框获得焦点，软键盘弹出
    }

    /**
     * 用户注册的第二步
     */
    private void userRegister2() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_register_dialog2, null);
        mRegisterPhone1 = (EditText) view.findViewById(R.id.et_phone);
        mRegisterPhone1.setText(register_phone);
        mRegisterMsgCode = (EditText) view.findViewById(R.id.et_msg_code);
        mGetVerificationCodeBtn = (Button) view.findViewById(R.id.btn_get_code);
        mGetVerificationCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetVerificationCodeBtn.setEnabled(false);
                timer.start();
            }
        });
        mGetVerificationCodeBtn.setEnabled(false);
        timer.start();
        getSMS(register_phone);
        // getVoice(register_phone);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.btn_register);
        builder.setView(view);
        builder.setPositiveButton(R.string.btn_register, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkVerificationCode();
            }
        });
        builder.show();
    }

    /**
     * 用户的登录界面 以对话框的形式出现
     */
    private void userLogin() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_login_dialog, null);
        mLoginPhone = (EditText) view.findViewById(R.id.et_phone);
        mLoginPwd = (EditText) view.findViewById(R.id.et_pwd);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.btn_login);
        builder.setView(view);
        builder.setPositiveButton(R.string.btn_login,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               /*if (checkLoginInfo()) {
                    Toast.makeText(LoginRegisterActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                } else {
                    Toast.makeText(LoginRegisterActivity.this, "该账号尚未注册简读!", Toast.LENGTH_SHORT).show();
                }*/
                ImeUtil.hideSoftKeyboard(mLoginPhone); //用户点击登录按钮后隐藏软键盘
                checkLoginInfo(); //检验用户的账号信息
            }

        });
        builder.show();
        ImeUtil.showSoftKeyboard(mLoginPhone); //强制账号输入框获得焦点，软键盘弹出
    }

    /**
     * 检验图片验证码是否输入正确
     */
    private boolean checkImageCode() {
        //将用户输入的图片验证码与生成的验证码在不考虑大小写的情况下进行比对
        if (register_code.equalsIgnoreCase(getCode))
            return true;
        else {
            return false;
        }
    }

    /**
     * 检验用户注册信息输入是否为空
     */
    private boolean checkRegisterInputIsEmpty() {
        if (TextUtils.isEmpty(register_phone) ||
                TextUtils.isEmpty(register_name) ||
                TextUtils.isEmpty(register_pwd) |
                        TextUtils.isEmpty(register_code))
            return true;

        return false;
    }

    private boolean checkLoginInputIsEmpty() {
        if (TextUtils.isEmpty(login_phone) ||
                TextUtils.isEmpty(login_pwd)) {
            return true;
        }
        return false;
    }

    /**
     * 检验密码输入是否合格，不能小于六位数
     */
    private boolean checkRegisterPassWordLength() {
        if (register_pwd.length() < 6)
            return false;
        else
            return true;
    }

    private boolean checkLoginPassWordLength() {
        if (login_pwd.length() < 6)
            return false;
        else
            return true;
    }

    /**
     * 检验用户信息
     */
    private void checkLoginInfo() {
        login_phone = mLoginPhone.getText().toString().trim();
        login_pwd = mLoginPwd.getText().toString().trim();

        if (checkLoginInputIsEmpty()) {
            Toast.makeText(LoginRegisterActivity.this, "信息不能为空!", Toast.LENGTH_SHORT).show();
        } else {
            if (RegUtils.isMobileNumber(login_phone)) {
                if (checkLoginPassWordLength()) {
                    if (login_phone.equals("15158570011") && login_pwd.equals("123456")) {
                    //queryUserInfo();
                    //if (queryUserInfo()) {
                        PreUtils.setPrefBoolean(LoginRegisterActivity.this, "isLogin", true);
                        Toast.makeText(getApplicationContext(), "登录成功!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                    } else {
                        Toast.makeText(getApplicationContext(), "该账号尚未注册简读!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginRegisterActivity.this, "密码必须大于6个字符!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginRegisterActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
            }
        }

        /*OkHttpUtils.post()
                .url(Constant.SERVER_BASE_URL + Constant.LOGIN_URL)
                .addParams("phone", login_phone)
                .addParams("password", login_pwd)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")) {
                            Toast.makeText(getApplicationContext(), "登录成功!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                        }else {
                            Toast.makeText(getApplicationContext(), "该账号尚未注册简读!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
    }

    private void queryUserInfo() {
        OkHttpUtils.post().addParams("phone", login_phone).addParams("password", login_pwd).url(Constant.SERVER_BASE_URL + Constant.USER_LOGIN_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String s) {
               /* Message msg = Message.obtain();
                msg.obj = s;
                msg.what = 3;
                mHandler.sendMessage(msg);
                Log.d("Main", s);*/
                switch (s){
                    case "success":
                        mHandler.sendEmptyMessage(3);
                        //isLoginSuccessed = true;
                        break;
                    case "fail":
                        // isLoginSuccessed = false;
                        mHandler.sendEmptyMessage(4);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void checkRegisterInfo() {
        register_phone = mRegisterPhone.getText().toString().trim();
        register_name = mRegisterName.getText().toString().trim();
        register_pwd = mRegisterPwd.getText().toString().trim();
        register_code = mRegisterCode.getText().toString();
        //判断用户注册信息是否为空，若为空
        if (checkRegisterInputIsEmpty()) {
            Toast.makeText(LoginRegisterActivity.this, "信息不能为空!", Toast.LENGTH_SHORT).show();
        } else { //不为空，则检验注册信息是否格式正确
            if (RegUtils.isMobileNumber(register_phone)) {
                if (checkRegisterPassWordLength()) {
                    if (checkImageCode()) {
                       userRegist();
                       /* if (phoneIsExist()) {
                            Toast.makeText(LoginRegisterActivity.this, "此手机号已注册！！", Toast.LENGTH_SHORT).show();
                        } else {
                           // userRegister2();
                            saveUserInfo();
                        }*/
                    } else {
                        Toast.makeText(LoginRegisterActivity.this, "验证码输入错误,请您重新输入!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginRegisterActivity.this, "密码必须大于6个字符!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginRegisterActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    private void saveUserInfo() {
        OkHttpUtils.post().addParams("phone", register_phone).addParams("password", register_pwd).addParams("name", register_name).url(Constant.SERVER_BASE_URL + Constant.SAVE_USER_INFO_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String s) {
                switch(s) {
                    case "fail":
                        mHandler.sendEmptyMessage(0);
                        //ToastUtils.showLong(getApplicationContext(), "注册失败");
                        break;
                    case "success":
                        mHandler.sendEmptyMessage(1);
                        //ToastUtils.showLong(getApplicationContext(), "注册成功");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void userRegist() {

        OkHttpUtils.post().addParams("phone", register_phone).addParams("password", register_pwd).addParams("name", register_name).url(Constant.SERVER_BASE_URL + Constant.USER_REGISTER_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String s) {
                Message msg = Message.obtain();
                msg.obj = s;
                msg.what = 6;
                mHandler.sendMessage(msg);
                Log.d("Main", s);
               /*switch(s) {
                    case "user exists":
                        mHandler.sendEmptyMessage(2);
                        break;
                    case "fail":
                        mHandler.sendEmptyMessage(0);
                        break;
                    case "success":
                        mHandler.sendEmptyMessage(1);
                        break;
                    default:
                        break;
                }*/
            }
        });
    }

    private boolean phoneIsExist() {
        OkHttpUtils.post().addParams("phone", register_phone).url(Constant.SERVER_BASE_URL + Constant.PHONE_IS_EXIST_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String s) {
                if (s.equals("not exist")) {
                    phoneIsExist = false;
                } else {
                    phoneIsExist = true;
                }
            }
        });
        return phoneIsExist;
    }

    /**
     * 短信验证码倒计时
     */
    CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mGetVerificationCodeBtn.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            mGetVerificationCodeBtn.setEnabled(true);
            mGetVerificationCodeBtn.setText(R.string.btn_get_verification_code);
        }
    };

    /**
     * 提交短信验证码
     */
    private void checkVerificationCode() {
        String SMS = mRegisterMsgCode.getText().toString().trim();
        String phone = mRegisterPhone.getText().toString().trim();
        SMSSDK.submitVerificationCode("86", phone, SMS);
    }

    /**
     * 获取短信验证码
     */
    private void getSMS(String phoneNumber) {
        // 请求获取短信验证码
        SMSSDK.getVerificationCode("86", phoneNumber);

    }

    /**
     * 获取语音验证码
     */
    private void getVoice(String phoneNumber) {
        // 请求获取语音验证码
        SMSSDK.getVoiceVerifyCode("86", phoneNumber);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
        timer.cancel();
        AppManager.getAppManager().finishActivity(this);
    }
}
