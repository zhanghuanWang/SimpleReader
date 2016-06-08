package com.ywg.simplereader.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cnvp on 15/11/27.
 */
public class TabLayoutAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mTitleList;
    private List<String> mDataList;

    public TabLayoutAdapter(Context context, List<String> titleList, List<String> dataList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mTitleList = titleList;
        this.mDataList = dataList;
    }
    @Override
    public int getCount() {
        return mTitleList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView tv = new TextView(mContext);
        tv.setTextSize(30.0f);
        tv.setText(mDataList.get(position));
        tv.setGravity(Gravity.CENTER);
        (container).addView(tv);
        return tv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        (container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    public void updateList(List<String> itemList) {
        mTitleList.clear();
        mTitleList.addAll(itemList);
        notifyDataSetChanged();
    }
}
