package com.ywg.simplereader.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ywg.simplereader.R;
import com.ywg.simplereader.activity.MainActivity;
import com.ywg.simplereader.bean.StoriesEntity;
import com.ywg.simplereader.constant.Constant;
import com.ywg.simplereader.util.PreUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cnvp on 15/12/2.
 */
public class HotNewsItemAdapter extends RecyclerView.Adapter<HotNewsItemAdapter.CustomViewHolder> {

    private LayoutInflater mInflater;

    private Context mContext;

    private boolean isNight;

    private List<StoriesEntity> mStoriesList;

    public HotNewsItemAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        isNight = ((MainActivity)mContext).isNight();
        this.mStoriesList = new ArrayList<StoriesEntity>();
    }


    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    //
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    //创建view
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = mInflater.inflate(R.layout.item_hot_news, parent, false);
        //cardView.setBackgroundColor(mContext.getResources().getColor(isNight ? R.color.item_bg_color_dark : R.color.item_bg_color_light));
        CustomViewHolder holder = new CustomViewHolder(cardView);
        return holder;
    }
    //将数据与界面进行绑定
    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        StoriesEntity storiesEntity = mStoriesList.get(position);

        String readSeq = PreUtils.getStringFromDefault(mContext, "read", "");
        //判断item id是否保存过 即item是否点击过 来设置不同的文本颜色
        if (readSeq.contains(mStoriesList.get(position).getId() + "")) {
            holder.tvTitle.setTextColor(mContext.getResources().getColor(isNight ? R.color.item_title_color_dark_clicked : R.color.item_title_color_light_clicked));
        } else {
            holder.tvTitle.setTextColor(mContext.getResources().getColor(isNight ? R.color.item_title_color_dark : R.color.item_title_color_light));
        }

        //holder.tvDes.setTextColor(mContext.getResources().getColor(isNight ? R.color.item_des_color_dark : R.color.item_des_color_light));

        if (storiesEntity.getType() == Constant.TOPIC) {
            holder.itemView.setClickable(false);
            ((CardView) holder.tvTopic.getParent().getParent()).setCardBackgroundColor(mContext.getResources().getColor(isNight ? R.color.recyclerview_bg_color_dark : R.color.recyclerview_bg_color_light));
            ((CardView) holder.tvTopic.getParent().getParent()).setContentPadding(0, 0, 0, 0);
            ((CardView) holder.tvTopic.getParent().getParent()).setCardElevation(0.0f);
            holder.tvTitle.setVisibility(View.GONE);
            holder.ivImage.setVisibility(View.GONE);
            holder.tvTopic.setVisibility(View.VISIBLE);
            holder.tvTopic.setText(storiesEntity.getTitle());
            holder.tvTopic.setTextColor(mContext.getResources().getColor(isNight ? R.color.item_title_color_dark_clicked : R.color.item_title_color_light_clicked));
        } else {
            ((CardView) holder.tvTitle.getParent().getParent()).setCardElevation(5.0f);
            ((CardView) holder.tvTitle.getParent().getParent()).setContentPadding(30, 30, 30, 30);
            //((RelativeLayout) holder.tvTopic.getParent()).setBackgroundResource(osn ? R.drawable.item_background_selector_light : R.drawable.item_background_selector_dark);
            holder.tvTopic.setVisibility(View.GONE);
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(storiesEntity.getTitle());
            holder.ivImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(storiesEntity.getImages().get(0)).fitCenter().into(holder.ivImage);
            ((CardView)holder.tvTitle.getParent().getParent()).setCardBackgroundColor(mContext.getResources().getColor(isNight ? R.color.item_bg_color_dark : R.color.item_bg_color_light));
        }
        ((RelativeLayout)holder.tvTitle.getParent().getParent().getParent()).setBackgroundColor(mContext.getResources().getColor(isNight ? R.color.recyclerview_bg_color_dark : R.color.recyclerview_bg_color_light));



        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            CardView cardView = (CardView) holder.itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    //removeData(pos);
                    return false;
                }
            });
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有元素
    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImage;
        private TextView tvTitle;
        //private TextView tvDes;
        private TextView tvTopic;
        public CustomViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTopic = (TextView) itemView.findViewById(R.id.tv_topic);
            // tvDes = (TextView) itemView.findViewById(R.id.tv_des);
        }
    }

    public void addData(List<StoriesEntity> storiesList) {
        // Item item = new Item(R.mipmap.ic_launcher, "New data","HelloWorld\nHelloWorld\nHelloWorld\n");
        //mItemList.add(position, item);
        // notifyItemInserted(position);
        this.mStoriesList.addAll(storiesList);
        notifyDataSetChanged();
    }

   /* //删除数据
    public void removeData(int position) {
        mItemList.remove(position);
        notifyItemRemoved(position);
    }*/

    public void updateTheme() {
        isNight = ((MainActivity) mContext).isNight();
        notifyDataSetChanged();
    }

    public StoriesEntity getItem(int position) {
        return mStoriesList.get(position);
    }

    @Override
    public int getItemCount() {
        return mStoriesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateData(int position) {
        //  notifyDataSetChanged();
        notifyItemChanged(position);
        Log.d("Main", "refesh item");
    }
    public void updateData() {
        notifyDataSetChanged();
        Log.d("Main", "refresh fragment");
    }

    public void refreshList(List<StoriesEntity> storiesEntityList) {
        mStoriesList.clear();
        this.mStoriesList.addAll(storiesEntityList);
        notifyDataSetChanged();
    }
    public void addList(List<StoriesEntity> storiesEntityList) {
        this.mStoriesList.addAll(storiesEntityList);
        notifyDataSetChanged();
    }
}
