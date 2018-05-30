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
import com.bwie.jingdong.adapter.DaiZhiFuAdapter;
import com.bwie.jingdong.bean.DingDanLieBiaoBean;
import com.bwie.jingdong.presenter.DaiZhiFuPresenter;
import com.bwie.jingdong.view.IDaiZhifu;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class FragmentDaiZhifu extends Fragment implements IDaiZhifu {
    private RecyclerView rv;
    private int page=1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                String a = (String) msg.obj;
                daiZhiFuAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "订单已取消" + a, Toast.LENGTH_SHORT).show();
            }else if (msg.what==2){
                daiZhiFuAdapter.notifyDataSetChanged();
            }
        }
    };
    private DaiZhiFuPresenter daiZhiFuPresenter;
    private DingDanLieBiaoBean dingDanLieBiaoBean;
    private DaiZhiFuAdapter daiZhiFuAdapter;
    private SmartRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dingdan,container,false);
        rv = view.findViewById(R.id.rv);
        refreshLayout = view.findViewById(R.id.refreshLayout);
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
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                daiZhiFuPresenter.getData("https://www.zhaoapi.cn/product/getOrders",1);
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                //加载
                daiZhiFuPresenter.getData("https://www.zhaoapi.cn/product/getOrders",page);
                refreshlayout.finishLoadmore();
            }
        });
    }

    @Override
    public void getSuccess(final DingDanLieBiaoBean dingDanLieBiaoBean) {
        this.dingDanLieBiaoBean=dingDanLieBiaoBean;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<DingDanLieBiaoBean.DataBean> data = dingDanLieBiaoBean.getData();
                daiZhiFuAdapter = new DaiZhiFuAdapter(getActivity(),data,handler);
                rv.setAdapter(daiZhiFuAdapter);
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
    }
}