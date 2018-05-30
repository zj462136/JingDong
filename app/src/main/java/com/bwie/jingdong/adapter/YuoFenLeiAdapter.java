package com.bwie.jingdong.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.bwie.jingdong.bean.ZiFenLeiBean;
import com.bwie.jingdong.inter.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/12/27.
 */

public class YuoFenLeiAdapter extends RecyclerView.Adapter<YuoFenLeiAdapter.ViewHolder>{
    Context context;
    List<ZiFenLeiBean.DataBean> list1=new ArrayList<>();
    public YuoFenLeiAdapter(Context context, List<ZiFenLeiBean.DataBean> list1) {
        this.context=context;
        this.list1=list1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.you_fenlei_layout,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_zifenlei.setText(list1.get(position).getName());
        List<ZiFenLeiBean.DataBean.ListBean> list = this.list1.get(position).getList();
        if (list!=null){
            holder.rv_zifenlei_item.setLayoutManager(new GridLayoutManager(context,4));
            ZiZiFenLeiAdapter zizifenleiadapter=new ZiZiFenLeiAdapter(context,list);
            holder.rv_zifenlei_item.setAdapter(zizifenleiadapter);
            //条目的点击事件
            zizifenleiadapter.setItemClickListener(new ItemClickListener() {
                @Override
                public void setItemClickListener(int position) {
                    Toast.makeText(context, position + " click", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void setItemLongClickListener(int position) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerView rv_zifenlei_item;
        private final TextView tv_zifenlei;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_zifenlei = itemView.findViewById(R.id.tv_zifenlei);
            rv_zifenlei_item = itemView.findViewById(R.id.rv_zifenlei_item);
        }
    }
}