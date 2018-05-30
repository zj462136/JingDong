package com.bwie.jingdong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bwie.jingdong.R;
import com.bwie.jingdong.adapter.YuoFenLeiAdapter;
import com.bwie.jingdong.bean.ZiFenLeiBean;
import com.bwie.jingdong.util.ApiUtil;
import com.bwie.jingdong.util.CommonUtils;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/12/27.
 */

public class ZiFenLeiFragment extends Fragment {

    private RecyclerView rv;
    private int cid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zifenlei, container, false);
        rv = view.findViewById(R.id.rv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        OkHttp3Util.doGet(ApiUtil.ZIFENLEIURL +"?"+ cid, new Callback() {
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
                            ZiFenLeiBean ziFenLeiBean = new Gson().fromJson(json, ZiFenLeiBean.class);
                            List<ZiFenLeiBean.DataBean> data = ziFenLeiBean.getData();
                            YuoFenLeiAdapter yuoFenLeiAdapter = new YuoFenLeiAdapter(getActivity(), data);
                            rv.setAdapter(yuoFenLeiAdapter);
                        }
                    });
                }
            }
        });
    }

    public void getCid(int postion){
        cid = postion;
    }
}
