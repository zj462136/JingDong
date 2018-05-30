package com.bwie.zhangjunjingdong.model;

import com.bwie.zhangjunjingdong.model.bean.LoginBean;
import com.bwie.zhangjunjingdong.presenter.inter.LoginPresenterInter;
import com.bwie.zhangjunjingdong.util.ApiUtil;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.util.OkHttp3Util_03;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginModel {
    private LoginPresenterInter loginPresenterInter;

    public LoginModel(LoginPresenterInter loginPresenterInter) {
        this.loginPresenterInter = loginPresenterInter;
    }

    public void getLogin(String loginUrl, String phone, String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("password", pwd);
        OkHttp3Util_03.doPost(loginUrl, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    final LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            loginPresenterInter.onSuccess(loginBean);
                        }
                    });
                }
            }
        });
    }

    public void getLoginByQQ(String phone, String pwd, final String ni_cheng, final String iconurl) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("password", pwd);
        OkHttp3Util_03.doPost(ApiUtil.LOGIN_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    final LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            loginPresenterInter.onSuccessByQQ(loginBean, ni_cheng, iconurl);
                        }
                    });

                }
            }
        });

    }
}
