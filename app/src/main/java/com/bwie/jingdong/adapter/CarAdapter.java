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
import com.bwie.jingdong.bean.CartBean;
import com.bwie.jingdong.bean.CountPriceBean;
import com.bwie.jingdong.presenter.CarPresenter;
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
 * Created by lenovo on 2018/1/17.
 */

public class CarAdapter extends BaseExpandableListAdapter {
    Context context;
    Handler handler;
    RelativeLayout relative_progress;
    CarPresenter carPresenter;
    List<CartBean.DataBean> data;
    private int childIndex;
    private int allIndex;

    public CarAdapter(Context context, Handler handler, RelativeLayout relative_progress, CarPresenter carPresenter, List<CartBean.DataBean> data) {
        this.carPresenter=carPresenter;
        this.context=context;
        this.data=data;
        this.relative_progress=relative_progress;
        this.handler=handler;
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
            holder.group_check=view.findViewById(R.id.cb_group);
            holder.group_text=view.findViewById(R.id.text_group);
            view.setTag(holder);
        }else {
            holder= (GroupHolder) view.getTag();
        }
        final CartBean.DataBean dataBean = data.get(groupPosition);
        holder.group_text.setText(dataBean.getSellerName());
        holder.group_check.setChecked(dataBean.isGroupChecked());
        holder.group_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_progress.setVisibility(View.VISIBLE);
                childIndex = 0;
                upDateAllChildInGroup(dataBean,holder.group_check.isChecked());
            }
        });
        return view;
    }

    private void upDateAllChildInGroup(final CartBean.DataBean dataBean, final boolean checked) {
        CartBean.DataBean.ListBean listBean = dataBean.getList().get(childIndex);
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
                        upDateAllChildInGroup(dataBean,checked);
                    }else {
                        carPresenter.getData(ApiUtil.cartUrl);
                    }
                }
            }
        });
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder;
        if (view==null){
            view=View.inflate(context,R.layout.child_items,null);
            holder=new ChildHolder();
            holder.child_check=view.findViewById(R.id.cb_child);
            holder.text_add=view.findViewById(R.id.text_add);
            holder.text_delete=view.findViewById(R.id.text_delete);
            holder.text_jian=view.findViewById(R.id.text_jian);
            holder.text_num=view.findViewById(R.id.text_num);
            holder.child_price=view.findViewById(R.id.text_price);
            holder.child_text=view.findViewById(R.id.text_title);
            holder.child_image=view.findViewById(R.id.image_goods);
            view.setTag(holder);
        }else {
            holder= (ChildHolder) view.getTag();
        }
        final CartBean.DataBean.ListBean listBean = data.get(groupPosition).getList().get(childPosition);
        holder.child_text.setText(listBean.getTitle());
        holder.child_price.setText("+"+listBean.getBargainPrice());
        String[] split = listBean.getImages().split("\\|");
        Glide.with(context)
                .load(split[0])
                .into(holder.child_image);
        holder.child_check.setChecked(listBean.getSelected()==0 ? false : true);
        holder.text_num.setText(listBean.getNum()+"");
        holder.child_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_progress.setVisibility(View.VISIBLE);
                Map<String, String> params=new HashMap<>();
                params.put("uid","2797");
                params.put("sellerid", String.valueOf(listBean.getSellerid()));
                params.put("pid", String.valueOf(listBean.getPid()));
                params.put("selected", String.valueOf(listBean.getSelected()== 0 ? 1 :0));
                params.put("num", String.valueOf(listBean.getNum()));
                OkHttp3Util.doPost(ApiUtil.updateCartUrl, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            carPresenter.getData(ApiUtil.cartUrl);
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
                            carPresenter.getData(ApiUtil.cartUrl);
                        }
                    }
                });
            }
        });
        holder.text_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = listBean.getNum();
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
                            carPresenter.getData(ApiUtil.cartUrl);
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
                            carPresenter.getData(ApiUtil.cartUrl);
                        }
                    }
                });
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void sendCountAndPrice() {
        double price=0;
        int count=0;
        for(int i=0;i<data.size();i++){
            List<CartBean.DataBean.ListBean> listBeans = data.get(i).getList();
            for (int j=0;j<listBeans.size();j++){
                price +=listBeans.get(j).getBargainPrice() * listBeans.get(j).getNum();
                count +=listBeans.get(j).getNum();
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String priceString = decimalFormat.format(price);
        CountPriceBean countPriceBean = new CountPriceBean(priceString, count);
        Message msg= Message.obtain();
        msg.what=0;
        msg.obj=countPriceBean;
        handler.sendMessage(msg);
    }

    public void setAllChildsChecked(boolean checked) {
        List<CartBean.DataBean.ListBean> allList=new ArrayList<>();
        for (int i=0;i<data.size();i++){
            List<CartBean.DataBean.ListBean> listBeans = data.get(i).getList();
            for (int j=0;j<listBeans.size();j++){
                allList.add(listBeans.get(j));
            }
        }
        relative_progress.setVisibility(View.VISIBLE);
        allIndex = 0;
        upDateAllChecked(allList,checked);
    }

    private void upDateAllChecked(final List<CartBean.DataBean.ListBean> allList, final boolean checked) {
        CartBean.DataBean.ListBean listBean = allList.get(allIndex);
        relative_progress.setVisibility(View.VISIBLE);
        Map<String, String> params=new HashMap<>();
        params.put("uid","2797");
        params.put("sellerid", String.valueOf(listBean.getSellerid()));
        params.put("pid", String.valueOf(listBean.getPid()));
        params.put("selected", String.valueOf(checked ? 1 :0));
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
                        upDateAllChecked(allList,checked);
                    } else {
                        carPresenter.getData(ApiUtil.cartUrl);
                    }
                }
            }
        });
    }

    class GroupHolder{
        CheckBox group_check;
        TextView group_text;
    }

    class ChildHolder{
        CheckBox child_check;
        ImageView child_image;
        TextView child_text;
        TextView child_price;
        TextView text_jian;
        TextView text_num;
        TextView text_add;
        TextView text_delete;
    }
}
