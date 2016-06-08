package com.ywg.simplereader.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ywg.simplereader.bean.Theme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cnvp on 15/11/30.
 */
public class NewsAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<Fragment> mFragmentList;
    private List<String> mLabelList;
    private List<Theme> mThemeList;

    public NewsAdapter(Context context, FragmentManager fm,List<Fragment> fragmentList, List<String> labelList) {
        super(fm);
        this.mContext = context;
        this.mFragmentList = fragmentList;
        this.mLabelList = labelList;
        mThemeList = new ArrayList<Theme>();
    }



    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragmentList.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      //  return mThemeList.get(position).getName();
        return mLabelList.get(position);
    }

    public void addData(List<Fragment> fragmentList, List<String> labelList) {
        this.mFragmentList = fragmentList;
        this.mLabelList = labelList;
        notifyDataSetChanged();

    }
    /*public void addData(List<Theme> themeList) {
        this.mThemeList.addAll(themeList);
        notifyDataSetChanged();
    }*/

}
