package com.ywg.simplereader.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ywg.simplereader.R;
import com.ywg.simplereader.view.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class StaggeredGridLayoutActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mDataList;

    private StaggerGridLayoutAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        initData();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //如果你需要显示的是横向滚动的列表或者竖直滚动的列表，则使用这个LayoutManager。
        //显然，我们要实现的是ListView的效果，所以需要使用它。生成这个LinearLayoutManager之后可以设置他滚动的方向，默认竖直滚动，所以这里没有显式地设置。
        //RecyclerView.LayoutManager是一个抽象类 有三个实现类:1.LinearLayoutManager 现行管理器，支持横向、纵向。2.GridLayoutManager 网格布局管理器 3.StaggeredGridLayoutManager 瀑布就式布局管理器
       // RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new StaggerGridLayoutAdapter(this,mDataList);
        mRecyclerView.setAdapter(mAdapter);

        initEvent();
    }

    private void initEvent() {
        mAdapter.setOnItemClickLitener(new StaggerGridLayoutAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(StaggeredGridLayoutActivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(StaggeredGridLayoutActivity.this, position + " long click",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        mDataList = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            mDataList.add(i + "");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
           /* case R.id.action_add_data:
                mAdapter.addData(1);
                break;
            case R.id.action_delete_data:
                mAdapter.removeData(1);
                break;*/
            default:
                break;
        }
        return true;
    }
}
