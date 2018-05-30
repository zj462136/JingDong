package com.bwie.jingdong.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.bwie.jingdong.adapter.YiQuXiaoAdapter;
import com.bwie.jingdong.bean.DingDanLieBiaoBean;
import com.bwie.jingdong.presenter.DaiZhiFuPresenter;
import com.bwie.jingdong.view.IDaiZhifu;

import java.util.List;

public class FragmentYiQuXiao extends Fragment implements IDaiZhifu {
    private RecyclerView rv;
    private int page=1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                String a = (String) msg.obj;
                yiQuXiaoAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "订单已取消" + a, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private DaiZhiFuPresenter daiZhiFuPresenter;
    private DingDanLieBiaoBean dingDanLieBiaoBean;
    private YiQuXiaoAdapter yiQuXiaoAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dingdan,container,false);
        rv = view.findViewById(R.id.rv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        daiZhiFuPresenter = new DaiZhiFuPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        daiZhiFuPresenter.getData("https://www.zhaoapi.cn/product/getOrders",page);
    }

    @Override
    public void getSuccess(final DingDanLieBiaoBean dingDanLieBiaoBean) {
        this.dingDanLieBiaoBean=dingDanLieBiaoBean;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<DingDanLieBiaoBean.DataBean> data = dingDanLieBiaoBean.getData();
                yiQuXiaoAdapter = new YiQuXiaoAdapter(getActivity(), data, handler);
                rv.setAdapter(yiQuXiaoAdapter);
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
    }
}
