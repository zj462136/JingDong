package com.bwie.jingdong.model;

import com.bwie.jingdong.bean.ShouYeBean;
import com.bwie.jingdong.presenter.IShouYePresenter;
import com.bwie.jingdong.util.ApiUtil;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/1/4.
 */

public class ShouyeModel {
    private IShouYePresenter iShouYePresenter;

    public ShouyeModel(IShouYePresenter iShouYePresenter){
        this.iShouYePresenter=iShouYePresenter;
    }
    public void getData(String shouYeUrl){
        OkHttp3Util.doGet(ApiUtil.SHOUYEURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String json=response.body().string();
                    ShouYeBean shouYeBean = new Gson().fromJson(json, ShouYeBean.class);
                    iShouYePresenter.getSuccess(shouYeBean);
                }
            }
        });
    }
}
