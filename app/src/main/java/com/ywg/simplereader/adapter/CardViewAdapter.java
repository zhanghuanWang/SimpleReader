package com.ywg.simplereader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ywg.simplereader.R;
import com.ywg.simplereader.activity.MainActivity;
import com.ywg.simplereader.bean.Item;
import com.ywg.simplereader.bean.StoriesEntity;

import java.util.List;

/**
 * Created by cnvp on 15/11/27.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CustomViewHolder> {

    private LayoutInflater mInflater;
    private List<Item> mItemList;
    private Context mContext;
    private boolean isNight;

    private List<StoriesEntity> mStoriesList;

    public CardViewAdapter(Context context, List<Item> itemList) {
        this.mContext = context;
        this.mItemList = itemList;
        mInflater = LayoutInflater.from(context);
        isNight = ((MainActivity)mContext).isNight();
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    //
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }
    //创建view
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = mInflater.inflate(R.layout.item_theme_news, parent, false);
        //cardView.setBackgroundColor(mContext.getResources().getColor(isNight ? R.color.item_bg_color_dark : R.color.item_bg_color_light));

        CustomViewHolder holder = new CustomViewHolder(cardView);
        return holder;
    }
    //将数据与界面进行绑定
    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        Item item = mItemList.get(position);
        holder.ivImage.setImageResource(item.getImgResId());
        holder.tvTitle.setText(item.getTvTitle());
        //holder.tvDes.setText(item.getTvContent());
        ((RelativeLayout)holder.tvTitle.getParent()).setBackgroundColor(mContext.getResources().getColor(isNight ? R.color.item_bg_color_dark : R.color.item_bg_color_light));
        holder.tvTitle.setTextColor(mContext.getResources().getColor(isNight ? R.color.item_title_color_dark : R.color.item_title_color_light));
       // holder.tvDes.setTextColor(mContext.getResources().getColor(isNight ? R.color.item_des_color_dark : R.color.item_des_color_light));
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, position + " Clicked",Toast.LENGTH_SHORT).show();;
            }
        });*/

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
                    //removeData(pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有元素
    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImage;
        private TextView tvTitle;
       // private TextView tvDes;
        public CustomViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
           // tvDes = (TextView) itemView.findViewById(R.id.tv_des);
        }
    }

    public void addData(int position) {
        Item item = new Item(R.mipmap.ic_launcher, "New data","HelloWorld\nHelloWorld\nHelloWorld\n");
        mItemList.add(position, item);
        notifyItemInserted(position);
    }

    //删除数据
    public void removeData(int position) {
        mItemList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateTheme() {
        isNight = ((MainActivity) mContext).isNight();
        notifyDataSetChanged();
    }

    public Item getItem(int position) {
        return mItemList.get(position);
    }
}
