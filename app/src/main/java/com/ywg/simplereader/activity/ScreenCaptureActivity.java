package com.ywg.simplereader.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xycoding.labeller.view.CaptureView;
import com.ywg.simplereader.R;
import com.ywg.simplereader.util.BitmapUtils;

import java.io.File;

public class ScreenCaptureActivity extends SlideBackActivity {

    private Toolbar mToolbar;

    //private ImageView mImageView;

    private CaptureView mCaptureView;

    private ProgressBar mProgressBar;

    private File mSaveDir;

    private SaveBitmapAsyncTask mAsyncTask;

    private Intent mIntent;

    private Uri mUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_capture);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("新闻截图");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSaveDir = BitmapUtils.getDiskCacheDir(this, "novcapture");

        Intent mIntent = getIntent();

        mCaptureView = (CaptureView) findViewById(R.id.capture_view);
        // 标签最多字数
        mCaptureView.setLabelMaxTextLength(20);
        // 标签默认文本
        mCaptureView.setLabelDefaultText("明明能靠脸偏偏靠才华");
        // 设置内容
        mCaptureView.setImage(mIntent.getData());

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_capture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
           /* case R.id.action_add_data:
                //mCardViewAdapter.addData(1);
                break;
            case R.id.action_delete_data:
               // mCardViewAdapter.removeData(1);
                break;*/

            case R.id.action_save_share:
                mAsyncTask = new SaveBitmapAsyncTask();
                mAsyncTask.execute(mCaptureView.getContentBitmap());
                break;
            default:
                break;
        }
        return true;
    }


    public void shareMsg(String activityTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/*");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }

    public void shareMsg(String activityTitle, String msgText,
                         Uri imageUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imageUri == null) {
            intent.setType("text/plain"); // 纯文本
        } else {
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
      //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }
    /**
     * 分享
     */
    private void share() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        //intent.setType("text/plain");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, intent.getStringExtra("title") + "：" + "\n");
     //   intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, mCaptureView.getContentBitmap());
        startActivity(Intent.createChooser(intent, "新闻截图分享"));
    }

    private class SaveBitmapAsyncTask extends AsyncTask<Bitmap, Void, Uri> {

        @Override
        protected void onPreExecute() {
            // onShowLoadingDialog();
        }

        @Override
        protected Uri doInBackground(Bitmap... params) {
           return BitmapUtils.saveBitmapToFile(mSaveDir, params[0]);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            //onDismissLoadingDialog();
            //shareMsg("新闻分享",mIntent.getStringExtra("title"),uri);
          //  mUri = uri;
            if (uri != null) {
                Toast.makeText(ScreenCaptureActivity.this, "图片已保存到\n" + mSaveDir, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(ScreenCaptureActivity.this, "图片保存失败" + mSaveDir, Toast.LENGTH_SHORT).show();

            }
        }
    }
}
