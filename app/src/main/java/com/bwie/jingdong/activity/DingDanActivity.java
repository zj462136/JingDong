package com.bwie.jingdong.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.bwie.jingdong.bean.CountPriceBean;
import com.bwie.jingdong.util.ApiUtil;
import com.bwie.jingdong.util.OkHttp3Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DingDanActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView text_dindan;
    private TextView text_fukuan;
    private CountPriceBean countPriceBean;
    private ImageView image_fanhui;
    private Button bt_chuangjian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dingdan);
        image_fanhui = findViewById(R.id.image_fanhui);
        text_dindan = findViewById(R.id.text_dindan);
        text_fukuan = findViewById(R.id.text_fukuan);
        bt_chuangjian = findViewById(R.id.bt_chuangjian);
        countPriceBean = (CountPriceBean) getIntent().getSerializableExtra("price");
        text_fukuan.setText("实付款:￥"+countPriceBean.getPriceString());
        image_fanhui.setOnClickListener(this);
        text_dindan.setOnClickListener(this);
        bt_chuangjian.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_fanhui:
                AlertDialog.Builder builder = new AlertDialog.Builder(DingDanActivity.this);
                builder.setTitle("你在三思三思~~~~");
                builder.setPositiveButton("去意已决", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("再看看",null);
                builder.show();
                break;
            case R.id.text_dindan:
                Intent intent = new Intent(DingDanActivity.this, DingDanLieBiaoActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_chuangjian:
                Map<String, String> params=new HashMap<>();
                params.put("uid","2797");
                params.put("price",countPriceBean.getPriceString());
                OkHttp3Util.doPost(ApiUtil.createCartUrl, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String json = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DingDanActivity.this,json,Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DingDanActivity.this, DingDanLieBiaoActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
                break;
        }
    }
}
