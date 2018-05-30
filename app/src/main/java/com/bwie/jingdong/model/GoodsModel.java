package com.bwie.jingdong.model;

import com.bwie.jingdong.bean.GoodsBean;
import com.bwie.jingdong.presenter.GoodsPresenter;
import com.bwie.jingdong.presenter.IGoodsPresenter;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/12/25.
 */

public class GoodsModel {
    private final IGoodsPresenter iGoodsPresenter;

    public GoodsModel(GoodsPresenter iGoodsPresenter){
        this.iGoodsPresenter=iGoodsPresenter;
    }
    public void getData(String goodsUrl,int pid){
        Map<String,String> params=new HashMap<>();
        params.put("pid", String.valueOf(pid));
        OkHttp3Util.doPost(goodsUrl, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String string = response.body().string();
                    GoodsBean goodsBean = new Gson().fromJson(string, GoodsBean.class);
                    iGoodsPresenter.getSuccess(goodsBean);
                }
            }
        });
    }
}
