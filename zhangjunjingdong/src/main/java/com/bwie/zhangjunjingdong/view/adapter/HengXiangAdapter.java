package com.bwie.zhangjunjingdong.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.FenLeiBean;
import com.bwie.zhangjunjingdong.view.Iview.OnItemListner;
import com.bwie.zhangjunjingdong.view.hodler.HengXiangHolder;

public class HengXiangAdapter extends RecyclerView.Adapter<HengXiangHolder> {
    private FenLeiBean fenLeiBean;
    private Context context;
    private OnItemListner onItemListner;

    public HengXiangAdapter(Context context, FenLeiBean fenLeiBean) {
        this.context = context;
        this.fenLeiBean = fenLeiBean;
    }

    @Override
    public HengXiangHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.heng_xiang_item_layout,null);
        HengXiangHolder hengXiangHolder = new HengXiangHolder(view);

        return hengXiangHolder;
    }

    @Override
    public void onBindViewHolder(HengXiangHolder holder, final int position) {
        FenLeiBean.DataBean dataBean = fenLeiBean.getData().get(position);

        holder.heng_item_text.setText(dataBean.getName());
        Glide.with(context).load(dataBean.getIcon()).into(holder.heng_item_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemListner.onItemClick(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemListner.onItemLongClick(position);

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {

        return fenLeiBean.getData().size();
    }

    public void setOnItemListner(OnItemListner onItemListner) {
        this.onItemListner = onItemListner;
    }
}
