package com.bwie.jingdong.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bwie.jingdong.R;
import com.bwie.jingdong.activity.SuoSouActivity;
import com.bwie.jingdong.adapter.ZuoFenLeiAdapter;
import com.bwie.jingdong.bean.FenLeiBean;
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

public class FragmenFenLei extends Fragment {
    private LinearLayout sousuo;
    private ListView lv;
    private FrameLayout frame1;
    private FrameLayout frame2;
    private ZuoFenLeiAdapter zuoFenLeiAdapter;
    private static int totalHeight = 0;//ListView高度
    public static int mPostion;
    private ZiFenLeiFragment ziFenLeiFragment;

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fenlei, container, false);
        sousuo = view.findViewById(R.id.sao_yi_sao);
        lv = view.findViewById(R.id.lv);
//        frame1 = view.findViewById(R.id.frame1);
        frame2 = view.findViewById(R.id.frame2);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SuoSouActivity.class);
                startActivity(intent);
            }
        });
        zuo();
    }

    private void zuo() {
        OkHttp3Util.doGet(ApiUtil.FENLEIURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    final String json=response.body().string();
                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            FenLeiBean fenLeiBean = new Gson().fromJson(json, FenLeiBean.class);
                            List<FenLeiBean.DataBean> data = fenLeiBean.getData();
                            zuoFenLeiAdapter = new ZuoFenLeiAdapter(getActivity(), data);
                            lv.setAdapter(zuoFenLeiAdapter);
                        }
                    });
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPostion=i;
                //点击listViewitem回滚居中
                totalHeight = lv.getMeasuredHeight()-120;
                lv.smoothScrollToPositionFromTop(mPostion,totalHeight/2,50);
                zuoFenLeiAdapter.notifyDataSetChanged();
                ziFenLeiFragment = new ZiFenLeiFragment();
                ziFenLeiFragment.getCid(mPostion);
//                System.out.println("==cid=="+mPostion);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame2, ziFenLeiFragment).commit();
            }
        });
    }
}