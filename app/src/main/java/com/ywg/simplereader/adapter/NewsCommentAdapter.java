package com.ywg.simplereader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ywg.simplereader.R;
import com.ywg.simplereader.bean.NewsComment;
import com.ywg.simplereader.util.TimeUtils;

import java.util.List;

/**
 * Created by cnvp on 15/12/9.
 */
/*
public class NewsCommentAdapter extends RecyclerView.Adapter<NewsCommentAdapter.MyViewHolder> {

    private Context mContext;

    private List<NewsComment> mNewsCommentList;

    private LayoutInflater mInflater;

    private ImageLoaderUtils imageLoaderUtils;

    public NewsCommentAdapter(Context context, List<NewsComment> newsCommemtList) {
        this.mContext = context;
        this.mNewsCommentList = newsCommemtList;
        mInflater = LayoutInflater.from(context);
        imageLoaderUtils = ImageLoaderUtils.getInstance(context);
    }

    @Override
    public NewsCommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.news_comment_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsCommentAdapter.MyViewHolder holder, int position) {
        NewsComment newsComment = mNewsCommentList.get(position);
        imageLoaderUtils.displayImage(newsComment.getAvatar(), holder.ivAvatar);
        holder.tvAuthor.setText(newsComment.getAuthor());
        holder.tvLikes.setText(newsComment.getLikes());
        holder.tvContent.setText(newsComment.getContent());
        holder.tvTime.setText(TimeUtils.convertToDate(Long.parseLong(newsComment.getTime())));
    }

    @Override
    public int getItemCount() {
        return mNewsCommentList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAvatar;

        private TextView tvAuthor;

        private TextView tvLikes;

        private TextView tvContent;

        private TextView tvTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvLikes= (TextView) itemView.findViewById(R.id.tv_likes);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    public void addList(List<NewsComment> newsCommentList) {
        mNewsCommentList.addAll(newsCommentList);
        notifyDataSetChanged();
    }
}
*/

public class NewsCommentAdapter extends BaseAdapter {

    private Context mContext;

    private List<NewsComment> mNewsCommentList;

    private LayoutInflater mInflater;

    public NewsCommentAdapter(Context context, List<NewsComment> newsCommemtList) {
        this.mContext = context;
        this.mNewsCommentList = newsCommemtList;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return mNewsCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.news_comment_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.tvAuthor = (TextView) convertView.findViewById(R.id.tv_author);
            viewHolder.tvLikes= (TextView) convertView.findViewById(R.id.tv_likes);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewsComment newsComment = mNewsCommentList.get(position);
        Glide.with(mContext).load(newsComment.getAvatar()).fitCenter().into(viewHolder.ivAvatar);
        viewHolder.tvAuthor.setText(newsComment.getAuthor());
        viewHolder.tvLikes.setText(newsComment.getLikes());
        viewHolder.tvContent.setText(newsComment.getContent());
        viewHolder.tvTime.setText(TimeUtils.timestampFormat(Long.parseLong(newsComment.getTime())));
        return convertView;
    }

    public class ViewHolder {
        private ImageView ivAvatar;

        private TextView tvAuthor;

        private TextView tvLikes;

        private TextView tvContent;

        private TextView tvTime;
    }

    public void addList(List<NewsComment> newsCommentList) {
        mNewsCommentList.addAll(newsCommentList);
        notifyDataSetChanged();
    }
}
