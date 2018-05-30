package com.bwie.jingdong.model;

import com.bwie.jingdong.bean.BuyBean;
import com.bwie.jingdong.presenter.IBuyPresenter;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/1/10.
 */

public class BuyModel {

    private IBuyPresenter iBuyPresenter;

    public BuyModel(IBuyPresenter iBuyPresenter) {
        this.iBuyPresenter=iBuyPresenter;
    }
    public void getData(String buyUrl){
        Map<String, String> params=new HashMap<>();
        params.put("uid","2797");
        OkHttp3Util.doPost(buyUrl, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String json = response.body().string();
                    BuyBean buyBean = new Gson().fromJson(json, BuyBean.class);
                    iBuyPresenter.getSuccess(buyBean);
                }
            }
        });
    }
}
