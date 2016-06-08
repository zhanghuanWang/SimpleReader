package com.ywg.simplereader.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Ywg on 2016/4/26.
 */
public class GuideAdapter extends PagerAdapter{

  /*  private final Random random = new Random();
    private int mSize;*/

    /*public GuideAdapter() {
        mSize = 5;
    }*/

    private int [] guide_images;
    public GuideAdapter(int [] guide_images) {
        this.guide_images = guide_images;
    }
    @Override public int getCount() {
        return guide_images.length;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override public Object instantiateItem(ViewGroup view, int position) {
        /*TextView textView = new TextView(view.getContext());
        textView.setText(String.valueOf(position + 1));
        textView.setBackgroundColor(0xff000000 | random.nextInt(0x00ffffff));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(48);
        view.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return textView;*/
        ImageView imageView = new ImageView(view.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(guide_images[position]);
        view.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return imageView;
    }
}
