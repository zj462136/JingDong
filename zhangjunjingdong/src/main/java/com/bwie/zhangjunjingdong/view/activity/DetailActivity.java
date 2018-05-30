package com.bwie.zhangjunjingdong.view.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.AddCartBean;
import com.bwie.zhangjunjingdong.model.bean.DeatilBean;
import com.bwie.zhangjunjingdong.presenter.AddCartPresenter;
import com.bwie.zhangjunjingdong.presenter.DeatailPresenter;
import com.bwie.zhangjunjingdong.util.ApiUtil;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.util.GlideImageLoader;
import com.bwie.zhangjunjingdong.util.ShareUtil;
import com.bwie.zhangjunjingdong.view.Iview.ActivityAddCartInter;
import com.bwie.zhangjunjingdong.view.Iview.DetailActivityInter;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements DetailActivityInter, View.OnClickListener,ActivityAddCartInter {

    private int pid;
    private DeatailPresenter deatailPresenter;
    private Banner banner;
    private TextView detail_title;
    private TextView detail_bargin_price;
    private TextView detail_yuan_price;
    private TextView detai_add_cart;
    private ImageView detail_image_back;
    private TextView watch_cart;
    private AddCartPresenter addCartPresenter;
    private ImageView detail_share;
    private DeatilBean deatilBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        banner = findViewById(R.id.banner);
        detail_title = findViewById(R.id.detail_title);
        detail_bargin_price = findViewById(R.id.detail_bargin_price);
        detail_yuan_price = findViewById(R.id.detail_yuan_price);
        detai_add_cart = findViewById(R.id.detai_add_cart);
        detail_image_back = findViewById(R.id.detail_image_back);
        watch_cart = findViewById(R.id.watch_cart);
        detail_share = findViewById(R.id.detail_share);


        deatailPresenter = new DeatailPresenter(this);
        addCartPresenter = new AddCartPresenter(this);


        pid = getIntent().getIntExtra("pid", -1);

        if (pid != -1){


            deatailPresenter.getDetailData(ApiUtil.DETAIL_URL,pid);
        }


        initBanner();


        detai_add_cart.setOnClickListener(this);
        detail_image_back.setOnClickListener(this);
        watch_cart.setOnClickListener(this);
        detail_share.setOnClickListener(this);

    }

    @Override
    public void onSuccess(DeatilBean deatilBean) {

        this.deatilBean = deatilBean;


        detail_yuan_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


        detail_title.setText(deatilBean.getData().getTitle());
        detail_bargin_price.setText("优惠价:"+deatilBean.getData().getBargainPrice());
        detail_yuan_price.setText("原价:"+deatilBean.getData().getPrice());

        String[] strings = deatilBean.getData().getImages().split("\\|");

        final ArrayList<String> imageUrls = new ArrayList<>();
        for (int i = 0;i<strings.length;i++){
            imageUrls.add(strings[i]);
        }

        banner.setImages(imageUrls);


        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(DetailActivity.this,ImageScaleActivity.class);
                intent.putStringArrayListExtra("imageUrls",imageUrls);
                intent.putExtra("position",position);

                startActivity(intent);
            }
        });

        banner.start();
    }

    private void initBanner() {

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new GlideImageLoader());
        banner.isAutoPlay(true);
        banner.setDelayTime(2500);
        banner.setIndicatorGravity(BannerConfig.CENTER);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detai_add_cart:

                if (CommonUtils.getBoolean("isLogin")) {

                    addCartPresenter.addToCart(ApiUtil.ADD_CART_URL, CommonUtils.getString("uid"),pid);
                }else {
                    Intent intent = new Intent(DetailActivity.this,LoginActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.detail_image_back:
                finish();
                break;
            case R.id.watch_cart:

                Intent intent = new Intent(DetailActivity.this,CartActivity.class);

                startActivity(intent);
                break;
            case R.id.detail_share:

                if (deatilBean != null) {
                    DeatilBean.DataBean data = deatilBean.getData();
                    ShareUtil.shareWeb(DetailActivity.this,data.getDetailUrl(),data.getTitle(),"我在京东发现一个好的商品,赶紧来看看吧!",data.getImages().split("\\|")[0],R.mipmap.ic_launcher);
                }

                break;
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Toast.makeText(DetailActivity.this,"分享开始",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(DetailActivity.this,"分享成功",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(DetailActivity.this,"分享失败",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(DetailActivity.this,"分享取消",Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onCartAddSuccess(AddCartBean addCartBean) {
        Toast.makeText(DetailActivity.this,addCartBean.getMsg(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
