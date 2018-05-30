package com.bwie.zhangjunjingdong.model;

import com.bwie.zhangjunjingdong.model.bean.UserInfoBean;
import com.bwie.zhangjunjingdong.presenter.inter.UserInfoPresenterInter;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.util.OkHttp3Util_03;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserInfoModel {

    private UserInfoPresenterInter userInfoPresenterInter;

    public UserInfoModel(UserInfoPresenterInter userInfoPresenterInter) {
        this.userInfoPresenterInter = userInfoPresenterInter;
    }

    public void getUserInfo(String userInfoUrl, String uid) {

        Map<String, String> params = new HashMap<>();
        params.put("uid",uid);

        OkHttp3Util_03.doPost(userInfoUrl, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String json = response.body().string();

                    final UserInfoBean userInfoBean = new Gson().fromJson(json,UserInfoBean.class);

                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            userInfoPresenterInter.onUserInfoSUccess(userInfoBean);
                        }
                    });

                }

            }
        });

    }
}
