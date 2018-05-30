package com.bwie.jingdong.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.bwie.jingdong.bean.DingDanLieBiaoBean;
import com.bwie.jingdong.util.CommonUtils;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DaiZhiFuAdapter extends RecyclerView.Adapter<DaiZhiFuAdapter.ViewHolder>{
    Context context;
    List<DingDanLieBiaoBean.DataBean> data;
    Handler handler;
    public DaiZhiFuAdapter(Context context, List<DingDanLieBiaoBean.DataBean> data, Handler handler) {
        this.context=context;
        this.data=data;
        this.handler=handler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.daizhifu_items,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.text_title.setText(data.get(position).getTitle());
        holder.text_state.setText("待支付");
        holder.text_price.setText(data.get(position).getPrice()+"");
        holder.text_time.setText(data.get(position).getCreatetime());
        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params=new HashMap<>();
                params.put("uid", "2797");
                params.put("orderId", position + "");
                params.put("status", "2");
                OkHttp3Util.doPost("https://www.zhaoapi.cn/product/updateOrder", params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (data.get(position).getStatus()==0){
                            holder.bt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final AlertDialog.Builder ab=new AlertDialog.Builder(context);
                                    ab.setTitle("确认取消订单吗？");
                                    ab.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, final int j) {

                                            OkHttp3Util.doGet("https://www.zhaoapi.cn/product/updateOrder?uid=2766&status=2&orderId="+data.get(position).getOrderid(), new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {

                                                }

                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    if (response.isSuccessful()){
                                                        final String string = response.body().string();
                                                        CommonUtils.runOnUIThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
                                                                OkHttp3Util.doGet("https://www.zhaoapi.cn/product/getOrders?uid=2797&page=6", new Callback() {
                                                                    @Override
                                                                    public void onFailure(Call call, IOException e) {


                                                                    }

                                                                    @Override
                                                                    public void onResponse(Call call, Response response) throws IOException {

                                                                        final String string = response.body().string();
                                                                        if (response.isSuccessful()){

                                                                            CommonUtils.runOnUIThread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    DingDanLieBiaoBean dingDanLieBiaoBean = new Gson().fromJson(string, DingDanLieBiaoBean.class);
                                                                                    data.clear();
                                                                                    data.addAll(dingDanLieBiaoBean.getData());
                                                                                    //                                                                            holder.zt.setTextColor(Color.GRAY);

                                                                                    notifyDataSetChanged();
                                                                                    List<DingDanLieBiaoBean.DataBean> data = dingDanLieBiaoBean.getData();

                                                                                    Message message=Message.obtain();
                                                                                    message.obj=data;


                                                                                    message.what=2;
                                                                                    handler.sendMessage(message);



                                                                                }
                                                                            });
                                                                        }


                                                                    }
                                                                });

                                                            }
                                                        });
                                                    }


                                                }
                                            });



                                        }
                                    });
                                    ab.setNegativeButton("否",null);
                                    ab.show();
                                }
                            });
                        }
                    }
                });
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
