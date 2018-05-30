package com.bwie.zhangjunjingdong.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.FenLeiBean;
import com.bwie.zhangjunjingdong.model.bean.HomeBean;
import com.bwie.zhangjunjingdong.presenter.FragmentHomeP;
import com.bwie.zhangjunjingdong.util.ApiUtil;
import com.bwie.zhangjunjingdong.util.ChenJinUtil;
import com.bwie.zhangjunjingdong.util.GlideImageLoader;
import com.bwie.zhangjunjingdong.view.Iview.InterFragmentHome;
import com.bwie.zhangjunjingdong.view.Iview.OnItemListner;
import com.bwie.zhangjunjingdong.view.activity.CustomCaptrueActivity;
import com.bwie.zhangjunjingdong.view.activity.DetailActivity;
import com.bwie.zhangjunjingdong.view.activity.WebViewActivity;
import com.bwie.zhangjunjingdong.view.adapter.HengXiangAdapter;
import com.bwie.zhangjunjingdong.view.adapter.MiaoShaAdapter;
import com.bwie.zhangjunjingdong.view.adapter.TuiJianAdapter;
import com.bwie.zhangjunjingdong.view.custom.ObservableScrollView;
import com.dash.zxinglibrary.activity.CodeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment implements InterFragmentHome {

    private FragmentHomeP fragmentHomeP;
    private Banner banner;
    private RecyclerView heng_xiang;
    private RecyclerView tui_jian_recycler;
    private MarqueeView marqueeView;
    private RecyclerView miao_sha_recycler;
    private SmartRefreshLayout smart_refresh;
    private LinearLayout linear_include;
    private final String TAG_MARGIN_ADDED = "marginAdded";
    private ObservableScrollView observe_scroll_view;
    private LinearLayout sao_yi_sao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_layout,container,false);

        banner = view.findViewById(R.id.banner);
        heng_xiang = view.findViewById(R.id.heng_xiang_recycler);
        tui_jian_recycler = view.findViewById(R.id.tui_jian_recycler);
        marqueeView = (MarqueeView) view.findViewById(R.id.marqueeView);
        miao_sha_recycler = view.findViewById(R.id.miao_sha_recycler);
        smart_refresh = view.findViewById(R.id.smart_refresh);
        linear_include = view.findViewById(R.id.linear_include);
        observe_scroll_view = view.findViewById(R.id.observe_scroll_view);
        sao_yi_sao = view.findViewById(R.id.sao_yi_sao);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ChenJinUtil.setStatusBarColor(getActivity(), Color.TRANSPARENT);

        initTitleBar();

        fragmentHomeP = new FragmentHomeP(this);

        fragmentHomeP.getNetData(ApiUtil.HOME_URL);
        fragmentHomeP.getFenLeiData(ApiUtil.FEN_LEI_URL);

        initBanner();

        initMarqueeView();

        smart_refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                smart_refresh.finishLoadmore(2000);
            }
        });
        smart_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                smart_refresh.finishRefresh(2000);
            }
        });

        sao_yi_sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=  new Intent(getActivity(), CustomCaptrueActivity.class);
                startActivityForResult(intent, 1001);
            }
        });

    }

    private void initTitleBar() {
        addMargin();
        ViewTreeObserver viewTreeObserver = linear_include.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                linear_include.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                final int height = linear_include.getHeight();

                observe_scroll_view.setScrollViewListener(new ObservableScrollView.IScrollViewListener() {
                    @Override
                    public void onScrollChanged(int x, int y, int oldx, int oldy) {
                        if (y <= 0) {
                            addMargin();
                            ChenJinUtil.setStatusBarColor(getActivity(),Color.TRANSPARENT);
                            linear_include.setBackgroundColor(Color.argb((int) 0, 227, 29, 26));//AGB由相关工具获得，或
                        } else if (y > 0 && y < height) {
                            if (y > ChenJinUtil.getStatusBarHeight(getActivity())) {
                                removeMargin();
                                ChenJinUtil.setStatusBarColor(getActivity(),getResources().getColor(R.color.colorPrimaryDark));
                            }
                            float scale = (float) y / height;
                            float alpha = (255 * scale);
                            linear_include.setBackgroundColor(Color.argb((int) alpha, 227, 29, 26));
                        } else {
                            linear_include.setBackgroundColor(Color.argb((int) 255, 227, 29, 26));
                        }
                    }
                });
            }
        });

    }

    private void removeMargin() {
        if (TAG_MARGIN_ADDED.equals(linear_include.getTag())) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linear_include.getLayoutParams();
            params.topMargin -= ChenJinUtil.getStatusBarHeight(getActivity());
            linear_include.setLayoutParams(params);
            linear_include.setTag(null);
        }
    }

    private void addMargin() {
        if (!TAG_MARGIN_ADDED.equals(linear_include.getTag())) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linear_include.getLayoutParams();
            params.topMargin += ChenJinUtil.getStatusBarHeight(getActivity());
            linear_include.setLayoutParams(params);
            linear_include.setTag(TAG_MARGIN_ADDED);
        }
    }


    private void initMarqueeView() {
        List<String> info = new ArrayList<>();
        info.add("欢迎访问京东app");
        info.add("大家有没有在 听课");
        info.add("是不是还有人在睡觉");
        info.add("你妈妈在旁边看着呢");
        info.add("赶紧的好好学习吧 马上毕业了");
        info.add("你没有事件睡觉了");
        marqueeView.startWithList(info);
    }

    private void initBanner() {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new GlideImageLoader());
        banner.isAutoPlay(true);
        banner.setDelayTime(2500);
        banner.setIndicatorGravity(BannerConfig.CENTER);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (! hidden) {

            if (observe_scroll_view.getScrollY() > ChenJinUtil.getStatusBarHeight(getActivity())) {
                removeMargin();
                ChenJinUtil.setStatusBarColor(getActivity(),getResources().getColor(R.color.colorPrimaryDark));
            }else {
                addMargin();
                ChenJinUtil.setStatusBarColor(getActivity(),Color.TRANSPARENT);
            }
        }
    }

    @Override
    public void onSuccess(final HomeBean homeBean) {

        List<HomeBean.DataBean> datas = homeBean.getData();

        List<String> imageUrls = new ArrayList<>();
        for (int i = 0;i<datas.size();i++){
            imageUrls.add(datas.get(i).getIcon());
        }

        banner.setImages(imageUrls);

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                HomeBean.DataBean dataBean = homeBean.getData().get(position);
                if (dataBean.getType() == 0) {
                    Intent intent = new Intent(getActivity(),WebViewActivity.class);

                    intent.putExtra("detailUrl",dataBean.getUrl());
                    startActivity(intent);

                }else {
                    Toast.makeText(getActivity(),"即将跳转商品详情",Toast.LENGTH_SHORT).show();
                }
            }
        });

        banner.start();
        miao_sha_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL,false));

        MiaoShaAdapter miaoShaAdapter = new MiaoShaAdapter(getActivity(), homeBean.getMiaosha());
        miao_sha_recycler.setAdapter(miaoShaAdapter);
        miaoShaAdapter.setOnItemListner(new OnItemListner() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);

                String detailUrl = homeBean.getMiaosha().getList().get(position).getDetailUrl();
                intent.putExtra("detailUrl",detailUrl);
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(int position) {

            }
        });

        tui_jian_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,OrientationHelper.VERTICAL));
        TuiJianAdapter tuiJianAdapter = new TuiJianAdapter(getActivity(), homeBean.getTuijian());
        tui_jian_recycler.setAdapter(tuiJianAdapter);
        tuiJianAdapter.setOnItemListner(new OnItemListner() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("pid",homeBean.getTuijian().getList().get(position).getPid());
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(int position) {

            }
        });

    }

    @Override
    public void onFenLeiDataSuccess(FenLeiBean fenLeiBean) {
        heng_xiang.setLayoutManager(new GridLayoutManager(getActivity(),2, OrientationHelper.HORIZONTAL,false));

        HengXiangAdapter hengXiangAdapter = new HengXiangAdapter(getActivity(), fenLeiBean);
        heng_xiang.setAdapter(hengXiangAdapter);
        hengXiangAdapter.setOnItemListner(new OnItemListner() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(),"点击事件执行",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int position) {
                Toast.makeText(getActivity(),"长按事件执行",Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }

                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (result.startsWith("http://")) {
                        Intent intent = new Intent(getActivity(),WebViewActivity.class);
                        intent.putExtra("detailUrl",result);
                        startActivity(intent);
                    }else {

                        Toast.makeText(getActivity(), "暂不支持此二维码", Toast.LENGTH_LONG).show();
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
