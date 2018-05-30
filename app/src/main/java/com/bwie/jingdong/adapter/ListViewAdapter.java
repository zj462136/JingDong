package com.bwie.jingdong.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwie.jingdong.R;
import com.bwie.jingdong.activity.GoodsActivity;
import com.bwie.jingdong.bean.SouSuoBean;
import com.bwie.jingdong.inter.ItemClickListener;

import java.util.List;

/**
 * Created by lenovo on 2018/1/5.
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder>{
    Context context;
    List<SouSuoBean.DataBean> data;
    private ItemClickListener itemClickListener;
    public ListViewAdapter(Context context, List<SouSuoBean.DataBean> data) {
        this.context=context;
        this.data=data;
    }
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_view,null);
        ListViewHolder gridViewHolder = new ListViewHolder(view);
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        final SouSuoBean.DataBean dataBean = data.get(position);
        String images = dataBean.getImages();
        String[] split = images.split("\\|");
        Glide.with(context)
                .load(split[0])
//                .placeholder(R.mipmap.ic_launcher)
//                .crossFade(R.mipmap.ic_launcher)
                .into(holder.iv);
        holder.tv1.setText(dataBean.getTitle());
        holder.tv2.setText("￥"+dataBean.getBargainPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //触发的是监听事件的执行,,,而不是直接做处理
                int pid = dataBean.getPid();
                Intent intent = new Intent(context, GoodsActivity.class);
                intent.putExtra("pid",pid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public class ListViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        public TextView tv1;
        public TextView tv2;

        public ListViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
        }
    }
}
