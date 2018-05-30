package com.bwie.jingdong.model;

import com.bwie.jingdong.bean.ZiZiFenLeiBean;
import com.bwie.jingdong.presenter.IZiZiFenLeiPresenter;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/1/4.
 */

public class ZiZiFenLeiModel {
    private IZiZiFenLeiPresenter iZiZiFenLeiPresenter;

    public ZiZiFenLeiModel(IZiZiFenLeiPresenter iZiZiFenLeiPresenter){
        this.iZiZiFenLeiPresenter=iZiZiFenLeiPresenter;
    }
    public void getData(String ziziFenLeiUrl, Map<String,String> param){
        OkHttp3Util.doPost(ziziFenLeiUrl,param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json=response.body().string();
                    ZiZiFenLeiBean ziZiFenLeiBean = new Gson().fromJson(json, ZiZiFenLeiBean.class);
                    iZiZiFenLeiPresenter.getSuccess(ziZiFenLeiBean);
                }
            }
        });
    }
}
