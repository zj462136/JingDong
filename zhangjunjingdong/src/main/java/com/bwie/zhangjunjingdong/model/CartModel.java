package com.bwie.zhangjunjingdong.model;

import com.bwie.zhangjunjingdong.model.bean.CartBean;
import com.bwie.zhangjunjingdong.presenter.inter.CartPresenterInter;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.util.OkHttp3Util_03;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CartModel {
    private CartPresenterInter cartPresenterInter;

    public CartModel(CartPresenterInter cartPresenterInter) {
        this.cartPresenterInter = cartPresenterInter;
    }

    public void getCartData(String selectCart, String uid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        OkHttp3Util_03.doPost(selectCart, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json = response.body().string();
                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("null".equals(json)) {
                                cartPresenterInter.getCartDataNull();
                            } else {
                                CartBean cartBean = new Gson().fromJson(json, CartBean.class);
                                cartPresenterInter.getCartDataSuccess(cartBean);
                            }
                        }
                    });
                }
            }
        });
    }
}
