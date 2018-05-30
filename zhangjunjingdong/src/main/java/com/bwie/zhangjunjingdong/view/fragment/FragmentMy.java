package com.bwie.zhangjunjingdong.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.FenLeiBean;
import com.bwie.zhangjunjingdong.model.bean.HomeBean;
import com.bwie.zhangjunjingdong.presenter.FragmentHomeP;
import com.bwie.zhangjunjingdong.util.ApiUtil;
import com.bwie.zhangjunjingdong.util.ChenJinUtil;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.util.GlideImgManager;
import com.bwie.zhangjunjingdong.view.Iview.InterFragmentHome;
import com.bwie.zhangjunjingdong.view.Iview.OnItemListner;
import com.bwie.zhangjunjingdong.view.activity.DetailActivity;
import com.bwie.zhangjunjingdong.view.activity.LoginActivity;
import com.bwie.zhangjunjingdong.view.activity.OrderListActivity;
import com.bwie.zhangjunjingdong.view.activity.UserSettingActivity;
import com.bwie.zhangjunjingdong.view.adapter.TuiJianAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * Created by Dash on 2018/1/23.
 *
 * 当前页面对用户可见的时候...判断是否登录
 */
public class FragmentMy extends Fragment implements InterFragmentHome, View.OnClickListener {

    private RecyclerView tui_jian_recycler;
    private FragmentHomeP fragmentHomeP;
    private LinearLayout my_linear_login;
    private ImageView my_user_icon;
    private TextView my_user_name;
    private LinearLayout my_order_dfk;
    private LinearLayout my_order_dpj;
    private LinearLayout my_order_dsh;
    private LinearLayout my_order_th;
    private LinearLayout my_order_all;
    private ScrollView fragment_my_scroll;
    private RelativeLayout login_back_pic;
    private SmartRefreshLayout smart_refresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_layout,container,false);

        tui_jian_recycler = view.findViewById(R.id.tui_jian_recycler);
        my_linear_login = view.findViewById(R.id.my_linear_login);
        my_user_icon = view.findViewById(R.id.my_user_icon);
        my_user_name = view.findViewById(R.id.my_user_name);
        my_order_dfk = view.findViewById(R.id.my_order_dfk);
        my_order_dpj = view.findViewById(R.id.my_order_dpj);
        my_order_dsh = view.findViewById(R.id.my_order_dsh);
        my_order_th = view.findViewById(R.id.my_order_th);
        my_order_all = view.findViewById(R.id.my_order_all);
        fragment_my_scroll = view.findViewById(R.id.fragment_my_scroll);
        login_back_pic = view.findViewById(R.id.login_back_pic);
        smart_refresh = view.findViewById(R.id.smart_refresh);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initChenJin();

        tui_jian_recycler.setFocusable(false);

        fragmentHomeP = new FragmentHomeP(this);
        fragmentHomeP.getNetData(ApiUtil.HOME_URL);

        my_linear_login.setOnClickListener(this);
        my_order_dfk.setOnClickListener(this);
        my_order_dpj.setOnClickListener(this);
        my_order_dsh.setOnClickListener(this);
        my_order_th.setOnClickListener(this);

        smart_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                smart_refresh.finishRefresh(3000);
            }
        });
        smart_refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                smart_refresh.finishLoadmore(3000);
            }
        });
    }

    private void initChenJin() {
        ChenJinUtil.setStatusBarColor(getActivity(), Color.TRANSPARENT);
    }

    private void initData() {

        boolean isLogin = CommonUtils.getBoolean("isLogin");

        if (isLogin) {
            if ( "".equals(CommonUtils.getString("iconUrl"))  || "null".equals(CommonUtils.getString("iconUrl"))){
                my_user_icon.setImageResource(R.drawable.user);
            }else {


                GlideImgManager.glideLoader(getActivity(), CommonUtils.getString("iconUrl"), R.drawable.user, R.drawable.user, my_user_icon, 0);
            }
            my_user_name.setText(CommonUtils.getString("name"));

            login_back_pic.setBackgroundResource(R.drawable.reg_bg);
        }else {
            my_user_icon.setImageResource(R.drawable.user);
            my_user_name.setText("登录/注册 >");

            login_back_pic.setBackgroundResource(R.drawable.normal_regbg);

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        initData();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (! hidden) {
            initChenJin();
            initData();
        }

    }

    @Override
    public void onSuccess(final HomeBean homeBean) {

        tui_jian_recycler.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
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

    }

    @Override
    public void onClick(View view) {
        boolean isLogin = CommonUtils.getBoolean("isLogin");

        Intent intent = new Intent();

        if (! isLogin) {
            intent.setClass(getActivity(),LoginActivity.class);

        }else {
            switch (view.getId()) {

                case R.id.my_linear_login:
                    intent.setClass(getActivity(), UserSettingActivity.class);

                    break;
                case R.id.my_order_dfk:
                    intent.setClass(getActivity(), OrderListActivity.class);
                    intent.putExtra("flag",1);
                    break;
                case R.id.my_order_dpj://已支付

                    intent.setClass(getActivity(), OrderListActivity.class);
                    intent.putExtra("flag",2);

                    break;
                case R.id.my_order_dsh://已取消

                    intent.setClass(getActivity(), OrderListActivity.class);
                    intent.putExtra("flag",3);
                    break;
                case R.id.my_order_th://退化

                    intent.setClass(getActivity(), OrderListActivity.class);

                    break;

            }
        }

        //开启
        startActivity(intent);

    }
}
