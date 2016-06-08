package com.ywg.simplereader.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ywg.simplereader.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cnvp on 15/11/26.
 */
public class StaggerGridLayoutAdapter extends RecyclerView.Adapter <StaggerGridLayoutAdapter.MyViewHolder>{

    private LayoutInflater mLayoutInflater;
    private List<String> mDataList;
    private List<Integer> mHeights;
    //private int mHeight;
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    //
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public StaggerGridLayoutAdapter(Context context, List<String> dataList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mDataList = dataList;
        mHeights = new ArrayList<Integer>();
        for (int i = 0; i < dataList.size(); i++) {
            mHeights.add((int)(Math.random() * 300 + 100));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mLayoutInflater.inflate(R.layout.item,parent
        ,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.tvText.getLayoutParams();
        layoutParams.height = mHeights.get(position);
        //mHeight = (int)(Math.random() * 300 + 100);
       // layoutParams.height = mHeight;
        holder.tvText.setLayoutParams(layoutParams);
        holder.tvText.setText(mDataList.get(position));

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    removeData(pos);
                    return false;
                }
            });
        }
    }
    //添加数据
    public void addData(int position) {
        mDataList.add(position, "Insert One");
        notifyItemInserted(position);
    }
    //删除数据
    public void removeData(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvText;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }

}

