package com.bwie.jingdong.model;

import com.bwie.jingdong.bean.DingDanLieBiaoBean;
import com.bwie.jingdong.presenter.IDaiZhiFuPresenter;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DaiZhifuModel {

    private IDaiZhiFuPresenter iDaiZhiFuPresenter;

    public DaiZhifuModel(IDaiZhiFuPresenter iDaiZhiFuPresenter) {
        this.iDaiZhiFuPresenter=iDaiZhiFuPresenter;
    }
    public void getData(String url,int page){
        Map<String, String> params=new HashMap<>();
        params.put("uid", "2797");
        params.put("status", "0");
        params.put("page", page + "");
        OkHttp3Util.doPost(url, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String json = response.body().string();
                    DingDanLieBiaoBean dingDanLieBiaoBean = new Gson().fromJson(json, DingDanLieBiaoBean.class);
                    iDaiZhiFuPresenter.getSuccess(dingDanLieBiaoBean);
                }
            }
        });
    }
}
