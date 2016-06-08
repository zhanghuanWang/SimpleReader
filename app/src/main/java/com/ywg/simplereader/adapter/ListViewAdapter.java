package com.ywg.simplereader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ywg.simplereader.R;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private List<String> mNotConcernedList;

    private Context mContext;

    private LayoutInflater mInFlater;

    public static interface OnAddClickListener {
        // true add; false cancel
        public void onClick(int position, boolean add); //传递boolean类型数据给activity
    }

    // add click callback
    OnAddClickListener onAddBtnClickListener;

    public void setOnAddClickListener(OnAddClickListener onAddBtnClickListener) {
        this.onAddBtnClickListener = onAddBtnClickListener;
    }

    public ListViewAdapter(Context context, List<String> notConcernedList) {
        this.mContext = context;
        this.mNotConcernedList = notConcernedList;
        mInFlater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mNotConcernedList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNotConcernedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = mInFlater.inflate(R.layout.item_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.tvColumn = (TextView) view.findViewById(R.id.tv_column);
            viewHolder.btnAdd = (ImageButton) view.findViewById(R.id.btn_add);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvColumn.setText(mNotConcernedList.get(position));
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAddBtnClickListener != null) {
                    onAddBtnClickListener.onClick(position, true);
                }
            }
        });
        return view;
    }

    public static class ViewHolder {
        TextView tvColumn;
        ImageButton btnAdd;
    }

    public void remove(int position) {//删除指定位置的item
        mNotConcernedList.remove(position);
        this.notifyDataSetChanged();//不要忘记更改适配器对象的数据源
    }

    public void insert(String column, int position) { //在指定位置插入item
        mNotConcernedList.add(position, column);
        this.notifyDataSetChanged();
    }

    public List<String> getList() {
        return mNotConcernedList;
    }


    /***
     * 动态修改ListVIiw的方位.
     *
     * @param start 点击移动的position
     * @param down  松开时候的position
     */
    public void update(int start, int down) {
        // 获取删除的东东.

        String column = mNotConcernedList.get(start);

        mNotConcernedList.remove(start);

        mNotConcernedList.add(down, column);

        notifyDataSetChanged();// 刷新ListView
    }

}