package com.ywg.simplereader.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class BackHandledFragment extends Fragment {

    public abstract String getTagText();

    public abstract boolean onBackPressed();

    //定义回调函数及变量
    protected BackHandlerInterface backHandlerInterface;
    public interface BackHandlerInterface {
        public void setSelectedFragment(BackHandledFragment backHandledFragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof BackHandlerInterface)) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        } else {
            backHandlerInterface = (BackHandlerInterface) getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Mark this fragment as the selected Fragment.
        //将自己的实例传出去
        backHandlerInterface.setSelectedFragment(this);
    }
}