package com.ywg.simplereader.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ywg.simplereader.R;
import com.ywg.simplereader.adapter.CheeseDynamicAdapter;
import com.ywg.simplereader.adapter.ListViewAdapter;
import com.ywg.simplereader.view.draggridview.DynamicGridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColumnChooseActivity extends AppCompatActivity {

    private String[] columns = new String[]{"日常心理学", "用户推荐日报", "电影日报", "不许无聊", "设计日报", "大公司日报", "财经日报", "互联网安全", "开始游戏", "音乐日报", "动漫日报", "体育日报"};
    private static final String TAG = ColumnChooseActivity.class.getName();

    private List<String> mConcernedList;

    private DynamicGridView gridView;

    private CheeseDynamicAdapter mCheeseDynamicAdapter;

    private ListViewAdapter mListViewAdapter;

    private ListView mNotConcernedListView;

    private List<String> mNotConcernedList;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_choose);

        initViews();

        setListeners();

    }

    private void initViews() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("栏目定制");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
        mConcernedList = new ArrayList<String>(Arrays.asList(columns));
        mCheeseDynamicAdapter = new CheeseDynamicAdapter(this, mConcernedList,
                getResources().getInteger(R.integer.column_count));
        gridView.setAdapter(mCheeseDynamicAdapter);

        mNotConcernedListView = (ListView) findViewById(R.id.list_view);
        mNotConcernedList  = new ArrayList<String>();
        mListViewAdapter = new ListViewAdapter(this, mNotConcernedList);
        mNotConcernedListView.setAdapter(mListViewAdapter);
    }

    private void setListeners() {
        //拖动结束后
        gridView.setOnDropListener(new DynamicGridView.OnDropListener() {
            @Override
            public void onActionDrop() {
                gridView.stopEditMode();
            }
        });
        //
        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String column = (String) parent.getAdapter().getItem(position);
                Toast.makeText(ColumnChooseActivity.this, parent.getAdapter().getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();
                mCheeseDynamicAdapter.remove(column);
                mListViewAdapter.insert(column, mListViewAdapter.getList().size());
            }
        });



        mListViewAdapter.setOnAddClickListener(new ListViewAdapter.OnAddClickListener() {
            @Override
            public void onClick(int position, boolean add) {
                mListViewAdapter.remove(position);
                String column = mListViewAdapter.getList().get(position);
                mCheeseDynamicAdapter.add(0, column);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (gridView.isEditMode()) {
            gridView.stopEditMode();
        } else {
            super.onBackPressed();
        }
    }

}
