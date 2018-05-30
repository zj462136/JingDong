package com.bwie.jingdong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwie.jingdong.R;
import com.bwie.jingdong.bean.FenLeiBean;
import com.bwie.jingdong.inter.ItemClickListener;

import java.util.List;

/**
 * Created by lenovo on 2017/12/26.
 */

public class JiuAdapter extends RecyclerView.Adapter<JiuAdapter.ViewHolder>{
    Context context;
    FenLeiBean fenLeiBean;
    private ItemClickListener itemClickListener;

    public JiuAdapter(Context context, FenLeiBean fenLeiBean) {
        this.context=context;
        this.fenLeiBean=fenLeiBean;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.jiugongge,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        List<FenLeiBean.DataBean> list = fenLeiBean.getData();
        Glide.with(context)
                .load(list.get(position).getIcon())
//                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageView);
        holder.textView.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //触发的是监听事件的执行,,,而不是直接做处理
                itemClickListener.setItemClickListener(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                itemClickListener.setItemClickListener(position);
                //解决点击和长按的冲突
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return fenLeiBean.getData().size();
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.iv);
            textView = view.findViewById(R.id.tv);
        }
    }
}
