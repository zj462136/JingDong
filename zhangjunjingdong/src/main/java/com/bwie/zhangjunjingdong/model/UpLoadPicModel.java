package com.bwie.zhangjunjingdong.model;

import com.bwie.zhangjunjingdong.model.bean.UpLoadPicBean;
import com.bwie.zhangjunjingdong.presenter.inter.UpLoadPicPresenterInter;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.util.OkHttp3Util_03;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpLoadPicModel {

    private UpLoadPicPresenterInter upLoadPicPresenterInter;

    public UpLoadPicModel(UpLoadPicPresenterInter upLoadPicPresenterInter) {
        this.upLoadPicPresenterInter = upLoadPicPresenterInter;
    }

    public void uploadPic(String uploadIconUrl, File saveIconFile, String uid, String fileName) {

        Map<String, String> params = new HashMap<>();
        params.put("uid",uid);

        OkHttp3Util_03.uploadFile(uploadIconUrl, saveIconFile, fileName, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    String json = response.body().string();

                    final UpLoadPicBean upLoadPicBean = new Gson().fromJson(json,UpLoadPicBean.class);

                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            upLoadPicPresenterInter.uploadPicSuccess(upLoadPicBean);
                        }
                    });

                }
            }
        });

    }
}
