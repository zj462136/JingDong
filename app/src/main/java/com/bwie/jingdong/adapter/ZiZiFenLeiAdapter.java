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
import com.bwie.jingdong.activity.LieBiaoActivity2;
import com.bwie.jingdong.bean.ZiFenLeiBean;
import com.bwie.jingdong.inter.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/12/27.
 */

public class ZiZiFenLeiAdapter extends RecyclerView.Adapter<ZiZiFenLeiAdapter.ViewHolder> {

    Context context;
    List<ZiFenLeiBean.DataBean.ListBean> list_zizifenlei=new ArrayList<>();
    private ItemClickListener itemClickListener;
    public ZiZiFenLeiAdapter(Context context, List<ZiFenLeiBean.DataBean.ListBean> list_zizifenlei) {
        this.context = context;
        this.list_zizifenlei = list_zizifenlei;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.zizifenlei,parent,false);

        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        Glide.with(context).load(list_zizifenlei.get(position).getIcon()).into(holder.iv_zizifenlei);
        System.out.println("===iv_zizifenlei=="+list_zizifenlei.get(position).getIcon());
        holder.tv_zizifenlei.setText(list_zizifenlei.get(position).getName());
        System.out.println("===tv_zizifenlei=="+list_zizifenlei.get(position).getName());

        // 如果设置了回调，则设置点击事件
        if (itemClickListener!= null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int pos = holder.getLayoutPosition();//条目的下标
                    //mOnItemClickLitener.onItemClick(v, pos);
                    int pscid = list_zizifenlei.get(position).getPscid();
                    System.out.println("===pscid=="+pscid);
                    Intent intent=new Intent(context, LieBiaoActivity2.class);
                    intent.putExtra("pscid",pscid);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list_zizifenlei.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener=itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView tv_zizifenlei;
        private final ImageView iv_zizifenlei;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_zizifenlei = itemView.findViewById(R.id.iv_zizifenlei);
            tv_zizifenlei = itemView.findViewById(R.id.tv_zizifenlei);
        }
    }
}
