package com.bwie.jingdong.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.bwie.jingdong.activity.DingDanActivity;
import com.bwie.jingdong.activity.GoodsActivity;
import com.bwie.jingdong.activity.LoginActivity;
import com.bwie.jingdong.adapter.BuyAdapter;
import com.bwie.jingdong.adapter.JianAdapter;
import com.bwie.jingdong.bean.BuyBean;
import com.bwie.jingdong.bean.CountPriceBean;
import com.bwie.jingdong.bean.ShouYeBean;
import com.bwie.jingdong.custom.CartExpanableListview;
import com.bwie.jingdong.inter.ItemClickListener;
import com.bwie.jingdong.presenter.BuyPresenter;
import com.bwie.jingdong.util.ApiUtil;
import com.bwie.jingdong.util.CommonUtils;
import com.bwie.jingdong.util.OkHttp3Util;
import com.bwie.jingdong.view.IBuyView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/12/27.
 */

public class FragmentBuy extends Fragment implements IBuyView, View.OnClickListener {
    private ImageView image_fanhui;
    private CartExpanableListview elv;
    private CheckBox check_quanxuan;
    private TextView text_quanxuan;
    private TextView text_heji;
    private TextView text_jiesuan;
    private BuyPresenter buyPresenter;
    private BuyBean buyBean;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                countPriceBean = (CountPriceBean) msg.obj;
                text_heji.setText("合计￥："+ countPriceBean.getPriceString());
                text_jiesuan.setText("去结算（"+ countPriceBean.getCount()+")");
            }
        }
    };
    private RelativeLayout relative_progress;
    private BuyAdapter buyAdapter;
    private RecyclerView tuijian;
    private CountPriceBean countPriceBean;
    private SharedPreferences sp;
    private Button linear2;
    private LinearLayout linear3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);
        elv = view.findViewById(R.id.elv);
        image_fanhui = view.findViewById(R.id.image_fanhui);
        check_quanxuan = view.findViewById(R.id.check_quanxuan);
        text_quanxuan = view.findViewById(R.id.text_quanxuan);
        text_heji = view.findViewById(R.id.text_heji);
        text_jiesuan = view.findViewById(R.id.text_jiesuan);
        relative_progress = view.findViewById(R.id.relative_progress);
        tuijian = view.findViewById(R.id.tuijian);
        linear2 = view.findViewById(R.id.linear2);
        linear3 = view.findViewById(R.id.linear3);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buyPresenter = new BuyPresenter(this);
        elv.setGroupIndicator(null);
        image_fanhui.setOnClickListener(this);
        check_quanxuan.setOnClickListener(this);
        text_quanxuan.setOnClickListener(this);
        text_jiesuan.setOnClickListener(this);
        tuijian.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        getTuijian();
    }

    @Override
    public void onResume() {
        super.onResume();
        sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        boolean islogin = sp.getBoolean("islogin", false);
        Log.d("TAG","是否登录"+islogin);
        if (islogin) {
            //登录过后才可以显示购物车状态
            //调用获取数据的方法
            relative_progress.setVisibility(View.VISIBLE);
            buyPresenter.getData(ApiUtil.cartUrl);
            linear2.setVisibility(View.GONE);
            elv.setVisibility(View.VISIBLE);
        } else {
            //没有登录时候再给一个布局，显示未登录，点击登录按钮跳转到登录界面
            linear2.setVisibility(View.VISIBLE);
            elv.setVisibility(View.GONE);
            linear2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void getSuccess(final BuyBean buyBean) {
        this.buyBean=buyBean;
        CommonUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                relative_progress.setVisibility(View.GONE);
                if (buyBean!=null) {
                    List<BuyBean.DataBean> data = buyBean.getData();
                    for (int i = 0; i < data.size(); i++) {
                        if (isChildInGroupChecked(i, data)) {
                            data.get(i).setGroup_check(true);
                        }
                    }

                    check_quanxuan.setChecked(isAllGroupChecked(data));

                    buyAdapter = new BuyAdapter(getActivity(), data, handler,buyPresenter,relative_progress);
                    elv.setAdapter(buyAdapter);
                    for (int i = 0; i < data.size(); i++) {
                        elv.expandGroup(i);
                    }
                    buyAdapter.sendPriceAndCount();
                }else {
                    Toast.makeText(getActivity(),"购物车空，请添加购物车",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isAllGroupChecked(List<BuyBean.DataBean> data) {
        for (int i=0;i<data.size();i++){
            if (!data.get(i).isGroup_check()){
                return false;
            }
        }
        return true;
    }

    private boolean isChildInGroupChecked(int i, List<BuyBean.DataBean> data) {
        List<BuyBean.DataBean.ListBean> lists = data.get(i).getList();
        for (int j=0;j<lists.size();j++){
            if (lists.get(j).getSelected()==0){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_fanhui:
                getActivity().finish();
                break;
            case R.id.check_quanxuan:
                if(buyAdapter!=null){
                    buyAdapter.setAllChildChecked(check_quanxuan.isChecked());
                }
                break;
            case R.id.text_quanxuan:
                if(buyAdapter!=null){
                    buyAdapter.setAllChildChecked(check_quanxuan.isChecked());
                }
                break;
            case R.id.text_jiesuan:
                Toast.makeText(getActivity(),"创建订单",Toast.LENGTH_SHORT).show();
                final String priceString = countPriceBean.getPriceString();
                Map<String, String> params=new HashMap<>();
                params.put("uid","2797");
                params.put("price",priceString);
                OkHttp3Util.doPost(ApiUtil.createCartUrl, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CountPriceBean countPriceBean = new CountPriceBean(priceString, 1);
                                    Intent intent = new Intent(getActivity(), DingDanActivity.class);
                                    intent.putExtra("price",countPriceBean);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
                break;
        }
    }
    private void getTuijian() {
        String path="https://www.zhaoapi.cn/ad/getAd";
        OkHttp3Util.doGet(path, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    final ShouYeBean shouYeBean = new Gson().fromJson(json, ShouYeBean.class);
                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            final ShouYeBean.TuijianBean tuijian = shouYeBean.getTuijian();
                            JianAdapter tuiJianAdapter= new JianAdapter(getActivity(), tuijian);
                            FragmentBuy.this.tuijian.setAdapter(tuiJianAdapter);
                            tuiJianAdapter.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void setItemClickListener(int position) {
                                    int pid = tuijian.getList().get(position).getPid();
                                    Intent intent = new Intent(getActivity(), GoodsActivity.class);
                                    intent.putExtra("pid",pid);
                                    startActivity(intent);
                                }

                                @Override
                                public void setItemLongClickListener(int position) {
                                    int pid = tuijian.getList().get(position).getPid();
                                    Intent intent = new Intent(getActivity(), GoodsActivity.class);
                                    intent.putExtra("pid",pid);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
