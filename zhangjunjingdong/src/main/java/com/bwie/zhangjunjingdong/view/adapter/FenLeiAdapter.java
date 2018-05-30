package com.bwie.zhangjunjingdong.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.FenLeiBean;

public class FenLeiAdapter extends BaseAdapter {
    private FenLeiBean fenLeiBean;
    private Context context;
    private int position;

    public FenLeiAdapter(Context context, FenLeiBean fenLeiBean) {
        this.context = context;
        this.fenLeiBean = fenLeiBean;
    }

    @Override
    public int getCount() {
        return fenLeiBean.getData().size();
    }

    @Override
    public Object getItem(int i) {
        return fenLeiBean.getData().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.fen_lei_item_layout,null);
            holder = new ViewHolder();

            holder.textView = view.findViewById(R.id.fen_lei_item_text);

            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        FenLeiBean.DataBean dataBean = fenLeiBean.getData().get(i);
        holder.textView.setText(dataBean.getName());

        //判断
        if (position == i) {
            //设置灰色的背景 和红色文字
            view.setBackgroundColor(Color.TRANSPARENT);
            holder.textView.setTextColor(Color.RED);
        }else {
            //白色的背景和黑色的文字
            view.setBackgroundColor(Color.WHITE);
            holder.textView.setTextColor(Color.BLACK);
        }

        return view;
    }

    public void setCurPositon(int position) {
        this.position = position;
    }

    private class ViewHolder{
        TextView textView;
    }
}
