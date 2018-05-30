package com.bwie.zhangjunjingdong.model;

import com.bwie.zhangjunjingdong.model.bean.ChildFenLeiBean;
import com.bwie.zhangjunjingdong.presenter.inter.FenLeiRightPresenterInter;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.util.OkHttp3Util_03;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentFenLeiRightModel {
    private FenLeiRightPresenterInter fenLeiRightPresenterInter;

    public FragmentFenLeiRightModel(FenLeiRightPresenterInter fenLeiRightPresenterInter) {
        this.fenLeiRightPresenterInter = fenLeiRightPresenterInter;
    }

    public void getChildData(String childFenLeiUrl, int cid) {
        Map<String, String> params = new HashMap<>();
        params.put("cid", String.valueOf(cid));
        OkHttp3Util_03.doPost(childFenLeiUrl, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    final ChildFenLeiBean childFenLeiBean = new Gson().fromJson(json, ChildFenLeiBean.class);
                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            fenLeiRightPresenterInter.onSuncess(childFenLeiBean);
                        }
                    });
                }
            }
        });
    }
}
