package com.bwie.jingdong.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bwie.jingdong.R;
import com.bwie.jingdong.bean.DingDanLieBiaoBean;

import java.util.List;

public class YiQuXiaoAdapter extends RecyclerView.Adapter<YiQuXiaoAdapter.ViewHolder>{
    Context context;
    List<DingDanLieBiaoBean.DataBean> data;
    Handler handler;
    public YiQuXiaoAdapter(Context context, List<DingDanLieBiaoBean.DataBean> data, Handler handler) {
        this.context=context;
        this.data=data;
        this.handler=handler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.yiquxiao_items,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.text_title.setText(data.get(position).getTitle());
        holder.text_state.setText("已取消");
        holder.text_price.setText(data.get(position).getPrice() + "");
        holder.text_time.setText(data.get(position).getCreatetime());

        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = position + "";
                handler.sendMessage(msg);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_title;
        TextView text_state;
        TextView text_price;
        TextView text_time;
        Button bt;

        public ViewHolder(View itemView) {
            super(itemView);
            text_title=itemView.findViewById(R.id.text_title);
            text_state=itemView.findViewById(R.id.text_state);
            text_price=itemView.findViewById(R.id.text_price);
            text_time=itemView.findViewById(R.id.text_time);
            bt=itemView.findViewById(R.id.bt);
        }
    }
}
