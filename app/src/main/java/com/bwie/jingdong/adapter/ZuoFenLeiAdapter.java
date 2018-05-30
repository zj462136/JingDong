package com.bwie.jingdong.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bwie.jingdong.R;
import com.bwie.jingdong.bean.FenLeiBean;
import com.bwie.jingdong.fragment.FragmenFenLei;

import java.util.List;

/**
 * Created by lenovo on 2017/12/27.
 */

public class ZuoFenLeiAdapter extends BaseAdapter{
    Context context;
    List<FenLeiBean.DataBean> list;
    public ZuoFenLeiAdapter(Context context, List<FenLeiBean.DataBean> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view==null){
            view=View.inflate(context, R.layout.zuo_fenlei_layout,null);
            holder=new ViewHolder();
            holder.textView=view.findViewById(R.id.text_name);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        holder.textView.setText(list.get(i).getName());
        if (i== FragmenFenLei.mPostion){
            holder.textView.setTextColor(Color.RED);
            holder.textView.setBackgroundColor(Color.parseColor("#F3F5F7"));
        }else {
            holder.textView.setTextColor(Color.BLACK);
            holder.textView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        return view;
    }
    class ViewHolder{
        TextView textView;
    }
}
