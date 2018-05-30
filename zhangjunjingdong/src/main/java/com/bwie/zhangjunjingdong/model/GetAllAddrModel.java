package com.bwie.zhangjunjingdong.model;

import com.bwie.zhangjunjingdong.model.bean.GetAllAddrBean;
import com.bwie.zhangjunjingdong.presenter.inter.GetAllAddrPresenterInter;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.util.OkHttp3Util_03;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GetAllAddrModel {
    private GetAllAddrPresenterInter getAllAddrPresenterInter;

    public GetAllAddrModel(GetAllAddrPresenterInter getAllAddrPresenterInter) {
        this.getAllAddrPresenterInter = getAllAddrPresenterInter;
    }

    public void getAllAddr(String getAllAddrUrl, String uid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        OkHttp3Util_03.doPost(getAllAddrUrl, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    final GetAllAddrBean getAllAddrBean = new Gson().fromJson(json, GetAllAddrBean.class);
                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            getAllAddrPresenterInter.onGetAllAddrSuccess(getAllAddrBean);
                        }
                    });
                }
            }
        });
    }
}
