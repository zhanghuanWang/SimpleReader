package com.ywg.simplereader.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ywg.simplereader.R;
import com.ywg.simplereader.adapter.ShowWebImageAdapter;
import com.ywg.simplereader.util.BitmapUtils;

import java.io.File;
import java.util.ArrayList;


public class ShowWebImageActivity extends AppCompatActivity {

    private Toolbar mToolBar;

    private TextView mIndexTv;
    private FloatingActionButton mDownloadImageBtn;

    private ViewPager mViewPager;
    private ShowWebImageAdapter mAdapter;
    private String imageUrl;
    private ArrayList<String> mImageUrlList;
    private int curIndex = 0;

    private File mSaveDir;

    //private SaveBitmapAsyncTask mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_webimage);

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");
        mImageUrlList = intent.getStringArrayListExtra("imageUrlList");

        initViews();

        // Glide.with(this).load(imagePath).placeholder(R.drawable.image_holder).fitCenter().into(mPhotoView);
        //mPhotoViewAttacher = new PhotoViewAttacher(mPhotoView);
        // mPhotoViewAttacher.setZoomable(false);//设置不能缩放

        setListeners();

    }

    private void setListeners() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curIndex = position;
                mIndexTv.setText((position + 1) + "/" + mImageUrlList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
         mDownloadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapUtils.getImage(mImageUrlList.get(curIndex));
                    BitmapUtils.saveImageToGallery(ShowWebImageActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        mAdapter.setOnClickListener(new ShowWebImageAdapter.OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }

            @Override
            public void onLongClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowWebImageActivity.this);
                builder.setTitle("操作");
                builder.setItems(new String[]{"保存图片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case 0:
                                try {
                                    Bitmap bitmap =  BitmapUtils.getImage(mImageUrlList.get(curIndex));
                                    BitmapUtils.saveImageToGallery(ShowWebImageActivity.this,bitmap);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });

    }

    private void initViews() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        //mToolBar.setBackgroundColor(getResources().getColor(isNight ? R.color.colorPrimaryDark : R.color.colorPrimary));
        //mToolBar.setTitle(storiesEntity.getTitle());
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mDownloadImageBtn = (FloatingActionButton) findViewById(R.id.btn_download_image);
        mIndexTv = (TextView) findViewById(R.id.tv_index);
        getCurIndex();
        // Log.d("Main", "被点击图片索引:"  + curIndex);
        mIndexTv.setText((curIndex + 1) + "/" + mImageUrlList.size());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mAdapter = new ShowWebImageAdapter(this, mImageUrlList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(curIndex);
        Log.d("Main", "被点击的图片地址：" + imageUrl);
        Log.d("Main", "所有图片地址：" + mImageUrlList.toString());
    }

    public void getCurIndex() {
        for (int i = 0; i < mImageUrlList.size(); i++) {
            if (mImageUrlList.get(i).equals(imageUrl)) {
                curIndex = i;
            }
        }
    }

   /* private class SaveBitmapAsyncTask extends AsyncTask<String, Void, Uri> {

        @Override
        protected void onPreExecute() {
            // onShowLoadingDialog();
        }

        @Override
        protected Uri doInBackground(String... params) {
            return BitmapUtils.saveBitmapToFile(mSaveDir, params[0]);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            //onDismissLoadingDialog();

            if (uri != null) {
                Toast.makeText(ShowWebImageActivity.this, "图片已保存到\n" + mSaveDir, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(ShowWebImageActivity.this, "图片保存失败" + mSaveDir, Toast.LENGTH_SHORT).show();

            }
        }
    }*/
} 