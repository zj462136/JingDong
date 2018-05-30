package com.bwie.jingdong.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwie.jingdong.R;
import com.bwie.jingdong.bean.BuyBean;
import com.bwie.jingdong.bean.CountPriceBean;
import com.bwie.jingdong.presenter.BuyPresenter;
import com.bwie.jingdong.util.ApiUtil;
import com.bwie.jingdong.util.OkHttp3Util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/1/10.
 */

public class BuyAdapter extends BaseExpandableListAdapter{
    Context context;
    List<BuyBean.DataBean> data;
    Handler handler;
    BuyPresenter buyPresenter;
    RelativeLayout relative_progress;
    private int childIndex;
    private int allIndex;

    public BuyAdapter(Context context, List<BuyBean.DataBean> data, Handler handler, BuyPresenter buyPresenter, RelativeLayout relative_progress) {
        this.context=context;
        this.data=data;
        this.handler=handler;
        this.buyPresenter=buyPresenter;
        this.relative_progress=relative_progress;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        final GroupHolder holder;
        if (view==null){
            view=View.inflate(context, R.layout.group_items,null);
            holder=new GroupHolder();
            holder.cb_group=view.findViewById(R.id.cb_group);
            holder.text_group=view.findViewById(R.id.text_group);
            view.setTag(holder);
        }else {
            holder= (GroupHolder) view.getTag();
        }
        final BuyBean.DataBean dataBean = data.get(groupPosition);
        holder.cb_group.setChecked(dataBean.isGroup_check());
        holder.text_group.setText(dataBean.getSellerName());
        holder.cb_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_progress.setVisibility(View.VISIBLE);
                childIndex = 0;
                upDateChildCheckedInGroup(holder.cb_group.isChecked(),dataBean);
            }
        });
        return view;
    }

    private void upDateChildCheckedInGroup(final boolean checked, final BuyBean.DataBean dataBean) {
        BuyBean.DataBean.ListBean listBean = dataBean.getList().get(childIndex);
        Map<String, String> params=new HashMap<>();
        params.put("uid","2797");
        params.put("sellerid", String.valueOf(listBean.getSellerid()));
        params.put("pid", String.valueOf(listBean.getPid()));
        params.put("selected", String.valueOf(checked ? 1 : 0));
        params.put("num", String.valueOf(listBean.getNum()));
        OkHttp3Util.doPost(ApiUtil.updateCartUrl, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    childIndex++;
                    if (childIndex<dataBean.getList().size()){
                        upDateChildCheckedInGroup(checked,dataBean);
                    }else {
                        buyPresenter.getData(ApiUtil.cartUrl);
                    }
                }
            }
        });
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder;
        if (view==null){
            view=View.inflate(context, R.layout.child_items,null);
            holder=new ChildHolder();
            holder.cb_child=view.findViewById(R.id.cb_child);
            holder.image_goods=view.findViewById(R.id.image_goods);
            holder.text_title=view.findViewById(R.id.text_title);
            holder.text_price=view.findViewById(R.id.text_price);
            holder.text_jian = view.findViewById(R.id.text_jian);
            holder.text_num = view.findViewById(R.id.text_num);
            holder.text_add = view.findViewById(R.id.text_add);
            holder.text_delete=view.findViewById(R.id.text_delete);
            view.setTag(holder);
        }else {
            holder= (ChildHolder) view.getTag();
        }
        final BuyBean.DataBean.ListBean listBean = data.get(groupPosition).getList().get(childPosition);
        holder.cb_child.setChecked(listBean.getSelected()==0 ? false : true);
        Glide.with(context).load(listBean.getImages().split("\\|")[0]).into(holder.image_goods);
        holder.text_title.setText(listBean.getTitle());
        holder.text_price.setText(listBean.getBargainPrice()+"");
        holder.text_num.setText(listBean.getNum()+"");
        holder.cb_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_progress.setVisibility(View.VISIBLE);
                Map<String, String> params=new HashMap<>();
                params.put("uid","2797");
                params.put("sellerid", String.valueOf(listBean.getSellerid()));
                params.put("pid", String.valueOf(listBean.getPid()));
                params.put("selected", String.valueOf(listBean.getSelected()== 0 ? 1 : 0));
                params.put("num", String.valueOf(listBean.getNum()));
                OkHttp3Util.doPost(ApiUtil.updateCartUrl, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            buyPresenter.getData(ApiUtil.cartUrl);
                        }
                    }
                });
            }
        });
        holder.text_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_progress.setVisibility(View.VISIBLE);
                Map<String, String> params=new HashMap<>();
                params.put("uid","2797");
                params.put("sellerid", String.valueOf(listBean.getSellerid()));
                params.put("pid", String.valueOf(listBean.getPid()));
                params.put("selected", String.valueOf(listBean.getSelected()));
                params.put("num", String.valueOf(listBean.getNum()+1));
                OkHttp3Util.doPost(ApiUtil.updateCartUrl, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            buyPresenter.getData(ApiUtil.cartUrl);
                        }
                    }
                });
            }
        });
        holder.text_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num=listBean.getNum();
                if (num==1){
                    return;
                }
                relative_progress.setVisibility(View.VISIBLE);
                Map<String, String> params=new HashMap<>();
                params.put("uid","2797");
                params.put("sellerid", String.valueOf(listBean.getSellerid()));
                params.put("pid", String.valueOf(listBean.getPid()));
                params.put("selected", String.valueOf(listBean.getSelected()));
                params.put("num", String.valueOf(num-1));
                OkHttp3Util.doPost(ApiUtil.updateCartUrl, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            buyPresenter.getData(ApiUtil.cartUrl);
                        }
                    }
                });
            }
        });
        holder.text_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_progress.setVisibility(View.VISIBLE);
                Map<String, String> params=new HashMap<>();
                params.put("uid","2797");
                params.put("pid", String.valueOf(listBean.getPid()));
                OkHttp3Util.doPost(ApiUtil.deleteCartUrl, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            buyPresenter.getData(ApiUtil.cartUrl);
                        }
                    }
                });
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void sendPriceAndCount() {
        double price=0;
        int count=0;
        for (int i=0;i<data.size();i++){
            List<BuyBean.DataBean.ListBean> lists = data.get(i).getList();
            for (int j=0;j<lists.size();j++){
                BuyBean.DataBean.ListBean listBean = lists.get(j);
                if (listBean.getSelected()==1){
                    price +=listBean.getBargainPrice() * listBean.getNum();
                    count +=listBean.getNum();
                }
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String PriceString = decimalFormat.format(price);
        CountPriceBean countPriceBean = new CountPriceBean(PriceString, count);
        Message msg = Message.obtain();
        msg.what=0;
        msg.obj=countPriceBean;
        handler.sendMessage(msg);
    }

    public void setAllChildChecked(boolean checked) {
        relative_progress.setVisibility(View.VISIBLE);
        List<BuyBean.DataBean.ListBean> allList=new ArrayList<>();
        for (int i=0;i<data.size();i++){
            for (int j=0;j<data.get(i).getList().size();j++){
                allList.add(data.get(i).getList().get(j));
            }
        }
        allIndex = 0;
        upDateAllChild(checked,allList);
    }

    private void upDateAllChild(final boolean checked, final List<BuyBean.DataBean.ListBean> allList) {
        BuyBean.DataBean.ListBean listBean = allList.get(allIndex);
        Map<String, String> params=new HashMap<>();
        params.put("uid","2797");
        params.put("sellerid", String.valueOf(listBean.getSellerid()));
        params.put("pid", String.valueOf(listBean.getPid()));
        params.put("selected", String.valueOf(checked ? 1 : 0));
        params.put("num", String.valueOf(listBean.getNum()));
        OkHttp3Util.doPost(ApiUtil.updateCartUrl, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    allIndex++;
                    if (allIndex<allList.size()) {
                        upDateAllChild(checked,allList);
                    }else {
                        buyPresenter.getData(ApiUtil.cartUrl);
                    }
                }
            }
        });
    }

    private class GroupHolder{
        CheckBox cb_group;
        TextView text_group;
    }

    private class ChildHolder{
        CheckBox cb_child;
        ImageView image_goods;
        TextView text_title;
        TextView text_price;
        TextView text_num;
        TextView text_jian;
        TextView text_add;
        TextView text_delete;
    }
}
