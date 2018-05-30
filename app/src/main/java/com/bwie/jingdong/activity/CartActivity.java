package com.bwie.jingdong.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.bwie.jingdong.adapter.CarAdapter;
import com.bwie.jingdong.adapter.JianAdapter;
import com.bwie.jingdong.bean.CartBean;
import com.bwie.jingdong.bean.CountPriceBean;
import com.bwie.jingdong.bean.ShouYeBean;
import com.bwie.jingdong.custom.CartExpanableListview;
import com.bwie.jingdong.inter.ItemClickListener;
import com.bwie.jingdong.presenter.CarPresenter;
import com.bwie.jingdong.util.ApiUtil;
import com.bwie.jingdong.util.CommonUtils;
import com.bwie.jingdong.util.OkHttp3Util;
import com.bwie.jingdong.view.ICartView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CartActivity extends AppCompatActivity implements ICartView, View.OnClickListener {

    private ImageView image_fanhui;
    private CartExpanableListview elv;
    private RelativeLayout relative_progress;
    private CheckBox check_all;
    private TextView text_total;
    private TextView text_buy;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                countPriceBean = (CountPriceBean) msg.obj;
                text_total.setText("合计：￥" + countPriceBean.getPriceString());
                text_buy.setText("结算（" + countPriceBean.getCount() + "）");
            }
        }
    };
    private CarPresenter carPresenter;
    private CartBean cartBean;
    private CarAdapter carAdapter;
    private CountPriceBean countPriceBean;
    private Button linear2;
    private SharedPreferences sp;
    private RecyclerView tuijian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        image_fanhui = findViewById(R.id.image_fanhui);
        elv = findViewById(R.id.elv);
        relative_progress = findViewById(R.id.relative_progress);
        check_all = findViewById(R.id.check_all);
        text_total = findViewById(R.id.text_total);
        text_buy = findViewById(R.id.text_buy);
        linear2 = findViewById(R.id.linear2);
        tuijian = findViewById(R.id.tuijian);
        carPresenter = new CarPresenter(this);
        elv.setGroupIndicator(null);
        image_fanhui.setOnClickListener(this);
        check_all.setOnClickListener(this);
        text_buy.setOnClickListener(this);
        getTuijian();
        tuijian.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
    }

    @Override
    protected void onResume() {
        super.onResume();
        carPresenter.getData(ApiUtil.cartUrl);
        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        boolean islogin = sp.getBoolean("islogin", false);
        Log.d("TAG", "是否登录" + islogin);
        if (islogin) {
            //登录过后才可以显示购物车状态
            //调用获取数据的方法
            relative_progress.setVisibility(View.VISIBLE);
            carPresenter.getData(ApiUtil.cartUrl);
            linear2.setVisibility(View.GONE);
            elv.setVisibility(View.VISIBLE);
        } else {
            //没有登录时候再给一个布局，显示未登录，点击登录按钮跳转到登录界面
            linear2.setVisibility(View.VISIBLE);
            elv.setVisibility(View.GONE);
            linear2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void getSuccess(final CartBean cartBean) {
        this.cartBean = cartBean;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                relative_progress.setVisibility(View.GONE);
                List<CartBean.DataBean> data = cartBean.getData();
                if (data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        List<CartBean.DataBean.ListBean> listBeans = data.get(i).getList();
                        data.get(i).setGroupChecked(isAllChildInGroupChecked(listBeans));
                    }
                    check_all.setChecked(isAllGroupChecked(data));
                    carAdapter = new CarAdapter(CartActivity.this, handler, relative_progress, carPresenter, data);
                    elv.setAdapter(carAdapter);
                    for (int i = 0; i < data.size(); i++) {
                        elv.expandGroup(i);
                    }
                    carAdapter.sendCountAndPrice();
                } else {
                    Toast.makeText(CartActivity.this, "购物车空,请添加购物车", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isAllGroupChecked(List<CartBean.DataBean> data) {
        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).isGroupChecked()) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllChildInGroupChecked(List<CartBean.DataBean.ListBean> listBeans) {
        for (int i = 0; i < listBeans.size(); i++) {
            if (listBeans.get(i).getSelected() == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_fanhui:
                finish();
                break;
            case R.id.check_all:
                carAdapter.setAllChildsChecked(check_all.isChecked());
                break;
            case R.id.text_buy:
                final String priceString = countPriceBean.getPriceString();
                Map<String, String> params = new HashMap<>();
                params.put("uid", "2797");
                params.put("price", priceString);
                OkHttp3Util.doPost(ApiUtil.createCartUrl, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            CountPriceBean countPriceBean = new CountPriceBean(priceString, 1);
                            Intent intent = new Intent(CartActivity.this, DingDanActivity.class);
                            intent.putExtra("price", countPriceBean);
                            startActivity(intent);
                        }
                    }
                });
                break;
        }
    }

    private void getTuijian() {
        String path = "https://www.zhaoapi.cn/ad/getAd";
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
                            JianAdapter tuiJianAdapter = new JianAdapter(CartActivity.this, tuijian);
                            CartActivity.this.tuijian.setAdapter(tuiJianAdapter);
                            tuiJianAdapter.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void setItemClickListener(int position) {
                                    int pid = tuijian.getList().get(position).getPid();
                                    Intent intent = new Intent(CartActivity.this, GoodsActivity.class);
                                    intent.putExtra("pid", pid);
                                    startActivity(intent);
                                }

                                @Override
                                public void setItemLongClickListener(int position) {
                                    int pid = tuijian.getList().get(position).getPid();
                                    Intent intent = new Intent(CartActivity.this, GoodsActivity.class);
                                    intent.putExtra("pid", pid);
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
