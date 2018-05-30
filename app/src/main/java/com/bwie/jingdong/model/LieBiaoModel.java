package com.bwie.jingdong.model;

import com.bwie.jingdong.bean.SouSuoBean;
import com.bwie.jingdong.presenter.ILieBiaoPresenter;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/12/16.
 */
public class LieBiaoModel {

    private ILieBiaoPresenter iLieBiaoPresenter;

    public LieBiaoModel(ILieBiaoPresenter iLieBiaoPresenter) {
        this.iLieBiaoPresenter=iLieBiaoPresenter;
    }
    public void getData(String lieBiaoUrl,String keywords,int page){
        Map<String,String> params=new HashMap<>();
        params.put("keywords", keywords);
        params.put("page", String.valueOf(page));
        OkHttp3Util.doPost(lieBiaoUrl, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String json=response.body().string();
                    if (json!=null) {
                        SouSuoBean souSuoBean = new Gson().fromJson(json, SouSuoBean.class);
                        iLieBiaoPresenter.getSuccess(souSuoBean);
                    }
                }
            }
        });

    }
}
