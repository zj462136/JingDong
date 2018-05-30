package com.bwie.jingdong.model;

import com.bwie.jingdong.bean.FenLeiBean;
import com.bwie.jingdong.presenter.IFenLeiPresenter;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/1/4.
 */

public class FenLeiModel {
    private IFenLeiPresenter iFenLeiPresenter;

    public FenLeiModel(IFenLeiPresenter iFenLeiPresenter){
        this.iFenLeiPresenter=iFenLeiPresenter;
    }
    public void getData(String fenLeiUrl){
        OkHttp3Util.doGet(fenLeiUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json=response.body().string();
                    FenLeiBean fenLeiBean = new Gson().fromJson(json, FenLeiBean.class);
                    iFenLeiPresenter.getSuccess(fenLeiBean);
                }
            }
        });
    }
}
