package com.ywg.simplereader.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ywg.simplereader.R;
import com.ywg.simplereader.bean.UpdateInfo;
import com.ywg.simplereader.constant.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import okhttp3.Call;

/**
 * 软件更新的管理器
 *
 * @author Administrator
 */
public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的XML信息 */
    private HashMap<String, String> mHashMap = new HashMap<String, String>();
    /* 下载保存路径 */
    private String mSavePath;

    /* 新版本号 */
    private int mServerCode;
    /* 新版本的软件名 */
    private String mAppName;
    /* 新版本apk下载地址 */
    private String mApkUrl;
    /* 新版本的更新描述 */
    private String mUpdateMessage;

    /* 下载进度的对话框 */
    private ProgressDialog mProgressDialog;
    /* 记录进度条数量 */
    private int progress;
    /* 下载进度 */
    private TextView mProgressTxt;
    /* 是否取消更新 */
    private boolean isCancelUpdate = false;

    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    mProgressDialog.setProgress(progress);
                    // mProgressTxt.setText(progress + "");
                    // 设置进度条位置
                    // mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 取消下载对话框显示
                    mProgressDialog.dismiss();
                    // 安装文件
                    installApk();
                    break;
                case 3:
                    //检测是否有新版本
                    if (isUpdate()) {
                        // 显示提示更新对话框
                        showUpdateDialog();
                    } else {
                        /*new CustomToast(mContext).makeCustomToast(R.string.soft_update_no,
                                Toast.LENGTH_SHORT);*/
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
       /* new GetXMLDataAysntask().execute();*/
        OkHttpUtils.get().url(Constant.SERVER_BASE_URL + Constant.UPDATE_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                UpdateInfo updateInfo = new Gson().fromJson(response, UpdateInfo.class);
                mApkUrl = updateInfo.getUrl();
                mServerCode = Integer.parseInt(updateInfo.getVersionCode());
                mUpdateMessage = updateInfo.getUpdateMessage();
                mHandler.sendEmptyMessage(3);
            }
        });
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    public boolean isUpdate() {
        // 获取当前软件版本
        int versionCode = getVersionCode(mContext);
        // 版本比较
        if (mServerCode > versionCode) {
            return true;
        }
        return false;
    }
    /**
     * 获取服务器上的软件版本更新的xml配置文件 并进行解析
     *
     *//*
    public class GetXMLDataAysntask extends AsyncTask<Void, Void, InputStream> {

        @Override
        protected InputStream doInBackground(Void... params) {
            InputStream inputStream = HttpUtil
                    .getXMLStream(HttpUtil.version_update_url);
            // InputStream inputStream =ParseXmlService.class.getClassLoader()
            // .getResourceAsStream("version.xml");
            // String jsonStr =
            // HttpUtil.getJsonData(HttpUtil.version_update_url);
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);
            if (inputStream != null) {
                try {
                    //mHashMap = new ParseXmlService().parseXml(inputStream);
                    if (mHashMap != null) {
                        mServerCode = Integer.parseInt(mHashMap.get("version"));
                        mAppName = mHashMap.get("name");
                        mApkUrl = mHashMap.get("url");
                        mUpdateMessage = mHashMap.get("description");
                        mHandler.sendEmptyMessage(3);
                    } else {
                        Log.d("Main", "XML数据解析失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("Main", "获取xml输入流失败");
            }
        }
    }*/

    /**
     * 获取软件版本名称 versionName向用户展示的版本号
     */
    public String getVersionName(Context context) {
        String versionName = "";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取软件版本号 versionCode用于版本更新
     */
    public int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    public void showUpdateDialog() {
        String updateDes = mHashMap.get("description");
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(updateDes);
        builder.setCancelable(false);
        // 更新
        builder.setPositiveButton(R.string.soft_update_now,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 显示下载对话框
                        showDownloadDialog();
                    }
                });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 显示软件下载对话框
     */
    public void showDownloadDialog() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle("下载中");
        mProgressDialog.setMessage("正在下载中......请稍后");
        mProgressDialog.setIndeterminate(false);
        // mProgressDialog.setButton("取消", new OnClickListener() {
        //
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        // isCancelUpdate = true;
        // mProgressDialog.dismiss();
        // }
        // });
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        // // 构造软件下载对话框
        // AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // builder.setTitle(R.string.soft_updating);
        // // 给下载对话框增加进度条
        // final LayoutInflater inflater = LayoutInflater.from(mContext);
        // View view = inflater.inflate(R.layout.soft_update_progress, null);
        // mProgress = (ProgressBar) view.findViewById(R.id.update_progress);
        // mProgressTxt = (TextView) view.findViewById(R.id.tv_progress);
        // builder.setView(view);
        // // 取消更新
        // builder.setNegativeButton(R.string.soft_update_cancel,
        // new OnClickListener() {
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        // dialog.dismiss();
        // // 设置取消状态
        // isCancelUpdate = true;
        // }
        // });
        // mDownloadDialog = builder.create();
        // mDownloadDialog.show();
        // 下载apk安装包
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    public void downloadApk() {
        // 启动新线程下载apk
        new Thread(new DownloadApkThread()).start();
    }

    /**
     * 下载文件线程
     */
    public class DownloadApkThread implements Runnable {
        @Override
        public void run() {
            HttpURLConnection conn = null;
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    // String sdpath = Environment.getExternalStorageDirectory()
                    // + "/";
                    // mSavePath = sdpath + "update";
                    mSavePath = "/sdcard/update/";
                    URL url = new URL(mApkUrl);
                    // 创建连接
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(8 * 1000);
                    conn.setConnectTimeout(8 * 1000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    // 连接成功则执行下载文件的任务
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // 获取文件大小
                        int fileSize = conn.getContentLength();
                        // 创建输入流
                        is = conn.getInputStream();

                        File file = new File(mSavePath);
                        // 判断文件目录是否存在
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        File apkFile = new File(mSavePath, mAppName);
                        fos = new FileOutputStream(apkFile);
                        // 缓存数组
                        byte buf[] = new byte[1024];
                        // 要下载的文件大小
                        int downFileSize = 0;
                        //
                        int numread = 0;
                        // 写入到文件中
                        // do {
                        while ((numread = is.read(buf)) != -1) {
                            downFileSize += numread;
                            // 写入文件
                            fos.write(buf, 0, numread);
                            // 计算进度条位置
                            progress = (int) (((float) downFileSize / fileSize) * 100);
                            // 更新下载进度
                            mHandler.sendEmptyMessage(DOWNLOAD);
                            // if(numread <= 0) {
                            // // 下载完成通知安装
                            // mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            // break;
                            // }
                        }
                        // } while (!isCancelUpdate); // 点击取消就停止下载.
                        mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 安装APK文件
     */
    public void installApk() {
        File apkfile = new File(mSavePath, mAppName);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkfile),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }
}