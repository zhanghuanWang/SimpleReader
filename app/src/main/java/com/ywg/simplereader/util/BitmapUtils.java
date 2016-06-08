package com.ywg.simplereader.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cnvp on 15/12/23.
 */
public class BitmapUtils {

    /**
     * 截取webView可视区域的截图
     *
     * @param webView 前提：WebView要设置webView.setDrawingCacheEnabled(true);
     * @return
     */

    public static Bitmap captureWebViewVisibleSize(WebView webView) {

        Bitmap bmp = webView.getDrawingCache();

        return bmp;

    }

    /**
     * 截取webView快照(webView加载的整个内容的大小)
     *
     * @param webView
     * @return
     */

    public static Bitmap captureWebView(WebView webView) {

        Picture snapShot = webView.capturePicture();

        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),

                snapShot.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmp);

        snapShot.draw(canvas);

        return bmp;

    }

    /**
     * 截取整个屏幕
     * @param context
     * @return
     */
    public static Bitmap captureScreen(Activity context){
        View cv = context.getWindow().getDecorView();
        Bitmap bmp = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        cv.draw(canvas);
        return bmp;
    }

    /**
     * 保存图片
     *
     * @param dir
     * @param bitmap
     * @return
     */
    public static Uri saveBitmapToFile(File dir, Bitmap bitmap) {
        FileOutputStream outputStream = null;
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, convertTimestamp() + ".png");
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String convertTimestamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        return format.format(new Date());
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * 保存图片到本地相册
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        if (bmp == null){
            ToastUtils.showLong(context, "保存出错了");
            return;
        }
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "SimpleReader");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            ToastUtils.showLong(context, "文件未发现");
            e.printStackTrace();
        } catch (IOException e) {
            ToastUtils.showLong(context, "保存出错了");
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        ToastUtils.showLong(context, "保存成功");
    }

    public static Bitmap getImage(String address) throws Exception{
        //通过代码 模拟器浏览器访问图片的流程
        URL url = new URL(address);
        HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        //获取服务器返回回来的流
        InputStream is = conn.getInputStream();
        byte[] imagebytes = getBytes(is);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
        return bitmap;
    }

    public static byte[] getBytes(InputStream is) throws Exception{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = is.read(buffer))!=-1){
            bos.write(buffer, 0, len);
        }
        is.close();
        bos.flush();
        byte[] result = bos.toByteArray();
        System.out.println(new String(result));
        return  result;
    }
}
