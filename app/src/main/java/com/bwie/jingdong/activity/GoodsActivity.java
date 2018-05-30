package com.bwie.jingdong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bwie.jingdong.R;
import com.bwie.jingdong.bean.GoodsBean;
import com.bwie.jingdong.presenter.GoodsPresenter;
import com.bwie.jingdong.util.ApiUtil;
import com.bwie.jingdong.util.CommonUtils;
import com.bwie.jingdong.util.OkHttp3Util;
import com.bwie.jingdong.view.IGoodsView;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GoodsActivity extends FragmentActivity implements IGoodsView {

    private ImageView image_fanhui;
    private TextView text_title;
    private TextView text_youhui;
    private TextView text_yuanjia;
    private Banner banner;
    private List<String> list;
    private TextView text_buy;
    private GoodsBean goodsBean;
    private GoodsPresenter goodsPresenter;
    private int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        image_fanhui = findViewById(R.id.image_fanhui);
        banner = findViewById(R.id.banner);
        text_title = findViewById(R.id.text_title);
        text_youhui = findViewById(R.id.text_youhui);
        text_yuanjia = findViewById(R.id.text_yuanjia);
        image_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pid = getIntent().getIntExtra("pid", 0);
        goodsPresenter = new GoodsPresenter(this);
        goodsPresenter.getData(ApiUtil.GOODSURL, pid);
    }

    @Override
    public void getSuccess(final GoodsBean goodsBean) {
        this.goodsBean = goodsBean;
        CommonUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                GoodsBean.DataBean data = goodsBean.getData();
                String images = data.getImages();
                String[] split = images.split("\\!");
                list = new ArrayList<String>();
                for (int i = 0; i < split.length; i++) {
                    list.add(split[i]);
                }
                banner.setImageLoader(new GlideImageLoader());
                banner.setImages(list);
                banner.start();
                text_title.setText(data.getTitle());
                text_youhui.setText("¥" + data.getBargainPrice() + "元");
                text_yuanjia.setText("¥" + data.getPrice() + "元");
                text_yuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });
    }

    public void add(View view) {
        OkHttp3Util.doGet("https://www.zhaoapi.cn/product/addCart?uid=2797&pid=2&source=android", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GoodsActivity.this,"加购成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void gouWuChe(View view) {
        Intent intent = new Intent(GoodsActivity.this, CartActivity.class);
        startActivity(intent);
    }

    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
//                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);
        }
    }
}