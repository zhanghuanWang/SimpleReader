package com.ywg.simplereader.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.ywg.simplereader.R;
import com.ywg.simplereader.adapter.CheeseDynamicAdapter;
import com.ywg.simplereader.view.draggridview.DynamicGridView;

import java.util.ArrayList;
import java.util.List;

public class LabelManageActivity extends SlideBackActivity {

    private Toolbar mToolbar;

    private DynamicGridView mConcernedGridView;
    private DynamicGridView mNotConcernedGridView;
    private CheeseDynamicAdapter  mConcernedLabelAdapter;
    private CheeseDynamicAdapter  mNotConcernedLabelAdapter;

    private List<String> mConcernedLabelList = new ArrayList<String>();
    private List<String> mNotConcernedLabelList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_manage);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("标签管理");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mConcernedGridView= (DynamicGridView) findViewById(R.id.dynamic_grid);
        mConcernedLabelAdapter = new CheeseDynamicAdapter(this, mConcernedLabelList,
                getResources().getInteger(R.integer.column_count));
        mConcernedGridView.setAdapter(mConcernedLabelAdapter);

        mNotConcernedGridView = (DynamicGridView) findViewById(R.id.dynamic_grid1);
        mNotConcernedLabelAdapter = new CheeseDynamicAdapter(this, mNotConcernedLabelList, getResources().getInteger(R.integer.column_count));

        mNotConcernedGridView.setAdapter(mNotConcernedLabelAdapter);

        //拖动结束后
        mConcernedGridView.setOnDropListener(new DynamicGridView.OnDropListener() {
            @Override
            public void onActionDrop() {
                mConcernedGridView.stopEditMode();
            }
        });
        //
        mConcernedGridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                // Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                // Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
            }
        });
        mConcernedGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mConcernedGridView.startEditMode(position);
                return true;
            }
        });

        mConcernedGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String column = (String) parent.getAdapter().getItem(position);
                Toast.makeText(LabelManageActivity.this, parent.getAdapter().getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();
                mConcernedLabelAdapter.remove(column);
               // mListViewAdapter.insert(column, mListViewAdapter.getList().size());
            }
        });

       /* mNotConcernedLabelAdapter.setOnAddClickListener(new ListViewAdapter.OnAddClickListener() {
            @Override
            public void onClick(int position, boolean add) {
                mListViewAdapter.remove(position);
                String column = mListViewAdapter.getList().get(position);
                mCheeseDynamicAdapter.add(1, column);
            }
        });*/
    }
}
