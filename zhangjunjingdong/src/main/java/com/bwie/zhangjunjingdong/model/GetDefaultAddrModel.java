package com.bwie.zhangjunjingdong.model;

import com.bwie.zhangjunjingdong.model.bean.DefaultAddrBean;
import com.bwie.zhangjunjingdong.presenter.inter.GetDefaultAddrPresenterInter;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.util.OkHttp3Util_03;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GetDefaultAddrModel {
    private GetDefaultAddrPresenterInter getDefaultAddrPresenterInter;

    public GetDefaultAddrModel(GetDefaultAddrPresenterInter getDefaultAddrPresenterInter) {
        this.getDefaultAddrPresenterInter = getDefaultAddrPresenterInter;
    }

    public void getDefaultAddr(String getDefaultAddrUrl, String uid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        OkHttp3Util_03.doPost(getDefaultAddrUrl, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json = response.body().string();//1M
                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("{}".equals(json)) {//没有地址数据
                                getDefaultAddrPresenterInter.onGetDefaultAddrEmpty();
                            } else {
                                DefaultAddrBean defaultAddrBean = new Gson().fromJson(json, DefaultAddrBean.class);
                                getDefaultAddrPresenterInter.onGetDefaultAddrSuccess(defaultAddrBean);
                            }
                        }
                    });
                }
            }
        });
    }
}
