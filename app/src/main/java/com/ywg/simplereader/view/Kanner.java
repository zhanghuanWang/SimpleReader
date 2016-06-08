package com.ywg.simplereader.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ywg.simplereader.R;
import com.ywg.simplereader.bean.Latest;

import java.util.ArrayList;
import java.util.List;

public class Kanner extends FrameLayout implements OnClickListener {
    
    private List<Latest.TopStoriesEntity> mTopStoriesEntitiesList;
    
    private ImageLoader mImageLoader;
    
    private DisplayImageOptions options;

    //private ImageLoaderUtils imageLoaderTools;

    private List<View> views;
    
    private Context mContext;
    
    private ViewPager mViewPager;
    //是否自动播放
    private boolean isAutoPlay;
    //当前索引
    private int mCurrentItem;
    //间隔时间
    private int delayTime;
    
    private LinearLayout mDotsLayout;
    
    private List<ImageView> mDotsList;
    
    private Handler mHandler = new Handler();
    
    private OnItemClickListener mItemClickListener;

    public Kanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
       /* mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();*/
       // imageLoaderTools = ImageLoaderUtils.getInstance(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        views = new ArrayList<View>();
        mDotsList = new ArrayList<ImageView>();
        mTopStoriesEntitiesList = new ArrayList<>();
        delayTime = 2000;
    }

    public Kanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Kanner(Context context) {
        this(context, null);
    }

    public void setTopEntities(List<Latest.TopStoriesEntity> topEntities) {
        this.mTopStoriesEntitiesList = topEntities;
        reset();
    }

    private void reset() {
        views.clear();
        initUI();
    }

    private void initUI() {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.kanner_layout, this, true);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mDotsLayout = (LinearLayout) view.findViewById(R.id.layout_dots);
        mDotsLayout.removeAllViews();

        int len = mTopStoriesEntitiesList.size();
        for (int i = 0; i < len; i++) {
            ImageView iv_dot = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            mDotsLayout.addView(iv_dot, params);
            mDotsList.add(iv_dot);
        }

        for (int i = 0; i <= len + 1; i++) {
            View fm = LayoutInflater.from(mContext).inflate(
                    R.layout.kanner_content_layout, null);
            ImageView iv_image = (ImageView) fm.findViewById(R.id.iv_image);
            TextView tv_title = (TextView) fm.findViewById(R.id.tv_title);
            iv_image.setScaleType(ScaleType.CENTER_CROP);
            iv_image.setAlpha(255);
//            iv.setBackgroundResource(R.drawable.loading1);
            if (i == 0) {
                //mImageLoader.displayImage(topStoriesEntities.get(len - 1).getImage(), iv_image, options);
                Glide.with(mContext).load(mTopStoriesEntitiesList.get(len - 1).getImage()).placeholder(R.drawable.image_holder).centerCrop().into(iv_image);
                tv_title.setText(mTopStoriesEntitiesList.get(len - 1).getTitle());
            } else if (i == len + 1) {
               // mImageLoader.displayImage(topStoriesEntities.get(0).getImage(), iv_image, options);
                Glide.with(mContext).load(mTopStoriesEntitiesList.get(0).getImage()).placeholder(R.drawable.image_holder).centerCrop().into(iv_image);
                //imageLoaderTools.displayImage(mTopStoriesEntitiesList.get(0).getImage(), iv_image);
                tv_title.setText(mTopStoriesEntitiesList.get(0).getTitle());
            } else {
                //mImageLoader.displayImage(topStoriesEntities.get(i - 1).getImage(), iv_image, options);
                Glide.with(mContext).load(mTopStoriesEntitiesList.get(i - 1).getImage()).placeholder(R.drawable.image_holder).centerCrop().into(iv_image);
                tv_title.setText(mTopStoriesEntitiesList.get(i - 1).getTitle());
            }
            fm.setOnClickListener(this);
            views.add(fm);
        }
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setFocusable(true);
        mViewPager.setCurrentItem(1);
        mCurrentItem = 1;
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        startPlay();
    }


    private void startPlay() {
        isAutoPlay = true;
        mHandler.postDelayed(task, 3000);
    }


    private final Runnable task = new Runnable() {

        @Override
        public void run() {
            if (isAutoPlay) {
                mCurrentItem = mCurrentItem % (mTopStoriesEntitiesList.size() + 1) + 1;
                if (mCurrentItem == 1) {
                    mViewPager.setCurrentItem(mCurrentItem, false);
                    mHandler.post(task);
                } else {
                    mViewPager.setCurrentItem(mCurrentItem);
                    mHandler.postDelayed(task, 5000);
                }
            } else {
                mHandler.postDelayed(task, 5000);
            }
        }
    };

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    if (mViewPager.getCurrentItem() == 0) {
                        mViewPager.setCurrentItem(mTopStoriesEntitiesList.size(), false);
                    } else if (mViewPager.getCurrentItem() == mTopStoriesEntitiesList.size() + 1) {
                        mViewPager.setCurrentItem(1, false);
                    }
                    mCurrentItem = mViewPager.getCurrentItem();
                    isAutoPlay = true;
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < mDotsList.size(); i++) {
                if (i == arg0 - 1) {
                    mDotsList.get(i).setImageResource(R.mipmap.dot_white);
                } else {
                    mDotsList.get(i).setImageResource(R.mipmap.dot_light);
                }
            }

        }

    }


    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void click(View v, Latest.TopStoriesEntity entity);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            Latest.TopStoriesEntity entity = mTopStoriesEntitiesList.get(mViewPager.getCurrentItem() - 1);
            mItemClickListener.click(v, entity);
        }
    }
}
