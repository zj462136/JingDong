package com.bwie.zhangjunjingdong.model;

import com.bwie.zhangjunjingdong.model.bean.FenLeiBean;
import com.bwie.zhangjunjingdong.model.bean.HomeBean;
import com.bwie.zhangjunjingdong.presenter.inter.FragmentHomePInter;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.util.OkHttp3Util_03;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentHomeModel {
    private FragmentHomePInter fragmentHomePInter;

    public FragmentHomeModel(FragmentHomePInter fragmentHomePInter) {
        this.fragmentHomePInter = fragmentHomePInter;
    }

    public void getData(String homeUrl) {
        Map<String, String> params = new HashMap<>();
        OkHttp3Util_03.doPost(homeUrl, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    final HomeBean homeBean = new Gson().fromJson(json, HomeBean.class);
                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            fragmentHomePInter.onSuccess(homeBean);
                        }
                    });
                }
            }
        });
    }

    public void getFenLeiData(String fenLeiUrl) {
        Map<String, String> params = new HashMap<>();
        OkHttp3Util_03.doPost(fenLeiUrl, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    final FenLeiBean fenLeiBean = new Gson().fromJson(json, FenLeiBean.class);
                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            fragmentHomePInter.onFenLeiDataSuccess(fenLeiBean);
                        }
                    });
                }
            }
        });
    }
}
