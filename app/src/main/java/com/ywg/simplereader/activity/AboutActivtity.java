package com.ywg.simplereader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ywg.simplereader.R;
import com.ywg.simplereader.util.AppUtils;

public class AboutActivtity extends SlideBackActivity {

    private Toolbar mToolbar;

    private TextView mVersion;

    private Button mGithubBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.nav_about);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mVersion = (TextView) findViewById(R.id.tv_version);
        mVersion.setText("V" + AppUtils.getVerName(this));

        mGithubBtn = (Button) findViewById(R.id.btn_github);
        mGithubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivtity.this, WebViewActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });

    }

}
