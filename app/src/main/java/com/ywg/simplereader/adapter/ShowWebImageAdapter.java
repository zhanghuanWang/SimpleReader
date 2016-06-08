package com.ywg.simplereader.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.ywg.simplereader.R;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Ywg on 2016/5/3.
 */
public class ShowWebImageAdapter extends PagerAdapter {

    private ArrayList<String> mImageUrlList;

    private Context mContext;

    private LayoutInflater mInflater;

    private PhotoViewAttacher photoViewAttacher;

    private PhotoView photoView;
    private ProgressBar progressBar;

    public ShowWebImageAdapter(Context context, ArrayList<String> imageUrlList) {
        this.mContext = context;
        this.mImageUrlList = imageUrlList;
        mInflater = LayoutInflater.from(context);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }
    //
    public interface OnClickListener {
        void onClick();
        void onLongClick(int position);
    }

    @Override
    public int getCount() {
        return mImageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mInflater.inflate(R.layout.item_show_webimage, null);
        photoView = (PhotoView) view.findViewById(R.id.photo_view);
        //progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        // 如果设置了回调，则设置点击事件
        if (mOnClickListener != null) {
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onClick();
                }
            });

            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnClickListener.onLongClick(position);
                    return false;
                }
            });
        }
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)mContext).finish();
            }
        });
        String imageUrl = mImageUrlList.get(position);
        Glide.with(mContext).load(imageUrl).placeholder(R.drawable.image_holder).fitCenter().into(photoView);
        photoViewAttacher = new PhotoViewAttacher(photoView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
