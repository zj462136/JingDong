package com.bwie.jingdong.model;

import com.bwie.jingdong.bean.CartBean;
import com.bwie.jingdong.presenter.ICarPresenter;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/1/17.
 */

public class CarModel {
    private final ICarPresenter iCarPresenter;

    public CarModel(ICarPresenter iCarPresenter) {
        this.iCarPresenter=iCarPresenter;
    }

    public void getData(String carUrl) {
        Map<String, String> parames=new HashMap<>();
        parames.put("uid","2797");
        OkHttp3Util.doPost(carUrl, parames, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String json=response.body().string();
                    CartBean cartBean = new Gson().fromJson(json, CartBean.class);
                    iCarPresenter.getSuccess(cartBean);
                }
            }
        });
    }
}
