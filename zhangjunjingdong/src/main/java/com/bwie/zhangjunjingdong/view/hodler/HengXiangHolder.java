package com.bwie.zhangjunjingdong.view.hodler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwie.zhangjunjingdong.R;

public class HengXiangHolder extends RecyclerView.ViewHolder {

    public ImageView heng_item_image;
    public TextView heng_item_text;

    public HengXiangHolder(View itemView) {
        super(itemView);

        heng_item_image = itemView.findViewById(R.id.heng_item_image);
        heng_item_text = itemView.findViewById(R.id.heng_item_text);
    }
}
