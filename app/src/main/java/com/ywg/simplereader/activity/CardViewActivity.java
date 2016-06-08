package com.ywg.simplereader.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ywg.simplereader.R;
import com.ywg.simplereader.adapter.CardViewAdapter;
import com.ywg.simplereader.bean.Item;

import java.util.ArrayList;
import java.util.List;

public class CardViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CardViewAdapter mAdapter;
    private List<Item> mItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initData();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CardViewAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);

        initEvent();
    }

    private void initEvent() {

        mAdapter.setOnItemClickLitener(new CardViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(CardViewActivity.this,position + " clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(CardViewActivity.this,position + " long clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        mItemList = new ArrayList<Item>();
        for (int i = 0; i < 100; i++) {
            Item item = new Item(R.mipmap.ic_launcher,"APP开发者" + i, "本文主要介绍CardView的使用，\nCardView是继承自FrameLayout，\n" +
                    "使用比较简单，只需要用CardView包含其他View就可以实现卡片效果了。\n" +
                    "实现效果如下：");
            mItemList.add(item);
        }
    }

}
