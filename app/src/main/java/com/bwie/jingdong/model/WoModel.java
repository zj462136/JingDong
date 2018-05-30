package com.bwie.jingdong.model;

import com.bwie.jingdong.bean.WoBean;
import com.bwie.jingdong.presenter.IWoPresenter;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/1/8.
 */

public class WoModel {
    private IWoPresenter iWoPresenter;

    public WoModel(IWoPresenter iWoPresenter) {
        this.iWoPresenter=iWoPresenter;
    }
    public void getData(String woUrl,int uid){
        Map<String, String> params=new HashMap<>();
        params.put("uid", String.valueOf(uid));
        OkHttp3Util.doPost(woUrl,params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String json=response.body().string();
                    WoBean woBean = new Gson().fromJson(json, WoBean.class);
                    iWoPresenter.getSuccess(woBean);
                }
            }
        });
    }
}
