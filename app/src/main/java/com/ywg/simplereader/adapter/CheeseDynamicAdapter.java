package com.ywg.simplereader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywg.simplereader.R;

import java.util.List;

public class CheeseDynamicAdapter extends BaseDynamicGridAdapter {

    private List<String> mItemsList;

    public CheeseDynamicAdapter(Context context, List<String> itemList, int columnCount) {
        super(context, itemList, columnCount);
        this.mItemsList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheeseViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid_view, null);
            holder = new CheeseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CheeseViewHolder) convertView.getTag();
        }
        holder.columnTv.setText(getItem(position).toString());
        
        return convertView;
    }

    private class CheeseViewHolder {
        private TextView columnTv;
        private ImageView deleteImag;
        private CheeseViewHolder(View view) {
            columnTv = (TextView) view.findViewById(R.id.item_title);
            //deleteImag = (ImageView) view.findViewById(R.id.iv_delete);
        }

    }
}