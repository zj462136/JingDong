package com.bwie.zhangjunjingdong.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.CartBean;
import com.bwie.zhangjunjingdong.model.bean.CountPriceBean;
import com.bwie.zhangjunjingdong.model.bean.FenLeiBean;
import com.bwie.zhangjunjingdong.model.bean.HomeBean;
import com.bwie.zhangjunjingdong.presenter.FragmentCartPresenter;
import com.bwie.zhangjunjingdong.presenter.FragmentHomeP;
import com.bwie.zhangjunjingdong.util.ApiUtil;
import com.bwie.zhangjunjingdong.util.ChenJinUtil;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.view.Iview.FragmentCartInter;
import com.bwie.zhangjunjingdong.view.Iview.InterFragmentHome;
import com.bwie.zhangjunjingdong.view.Iview.OnItemListner;
import com.bwie.zhangjunjingdong.view.activity.DetailActivity;
import com.bwie.zhangjunjingdong.view.activity.LoginActivity;
import com.bwie.zhangjunjingdong.view.activity.MakeSureOrderActivity;
import com.bwie.zhangjunjingdong.view.adapter.MyExpanableAdapter;
import com.bwie.zhangjunjingdong.view.adapter.TuiJianAdapter;
import com.bwie.zhangjunjingdong.view.custom.MyExpanableView;

import java.util.ArrayList;
import java.util.List;

public class FragmentShoppingCart extends Fragment implements InterFragmentHome,FragmentCartInter, View.OnClickListener {

    private LinearLayout linear_login;
    private Button cart_login;
    private RecyclerView tui_jian_recycler;
    private MyExpanableView my_expanable_view;
    private FragmentHomeP fragmentHomeP;
    private RelativeLayout relative_progress;
    private FragmentCartPresenter fragmentCartPresenter;
    private CheckBox cart_check_all;
    private TextView text_total;
    private TextView text_buy;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                CountPriceBean countPriceBean = (CountPriceBean) msg.obj;

                text_total.setText("合计:¥"+countPriceBean.getPriceString());
                text_buy.setText("去结算("+countPriceBean.getCount()+")");
            }
        }
    };
    private MyExpanableAdapter myExpanableAdapter;
    private ImageView empty_cart_image;
    private CartBean cartBean;
    private ArrayList<CartBean.DataBean.ListBean> list_selected = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frament_cart_layout,container,false);

        linear_login = view.findViewById(R.id.linear_login);
        cart_login = view.findViewById(R.id.cart_login);
        tui_jian_recycler = view.findViewById(R.id.tui_jian_recycler);
        my_expanable_view = view.findViewById(R.id.my_expanable_view);
        relative_progress = view.findViewById(R.id.relative_progress);
        cart_check_all = view.findViewById(R.id.cart_check_all);
        text_total = view.findViewById(R.id.text_total);
        text_buy = view.findViewById(R.id.text_buy);
        empty_cart_image = view.findViewById(R.id.empty_cart_image);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initChenJin();

        my_expanable_view.setGroupIndicator(null);

        tui_jian_recycler.setFocusable(false);
        fragmentHomeP = new FragmentHomeP(this);
        fragmentHomeP.getNetData(ApiUtil.HOME_URL);

        fragmentCartPresenter = new FragmentCartPresenter(this);

        cart_check_all.setOnClickListener(this);
        text_buy.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        initData();

    }



    private void initData() {

        if (CommonUtils.getBoolean("isLogin")) {
            my_expanable_view.setVisibility(View.VISIBLE);
            empty_cart_image.setVisibility(View.VISIBLE);
            linear_login.setVisibility(View.GONE);

            getCartData();


        }else {
            linear_login.setVisibility(View.VISIBLE);
            my_expanable_view.setVisibility(View.GONE);
            empty_cart_image.setVisibility(View.GONE);
            cart_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }
            });

        }

    }

    private void getCartData() {
        relative_progress.setVisibility(View.VISIBLE);
        fragmentCartPresenter.getCartData(ApiUtil.SELECT_CART,CommonUtils.getString("uid"));

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {

        }else {
            initChenJin();
            initData();

        }

    }

    private void initChenJin() {
        ChenJinUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.colorPrimaryDark));
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
    public void getCartDataNull() {
        relative_progress.setVisibility(View.GONE);
        empty_cart_image.setVisibility(View.VISIBLE);
        my_expanable_view.setVisibility(View.GONE);
        Toast.makeText(getActivity(),"购物车为空,先去逛逛吧",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getCartDataSuccess(final CartBean cartBean) {
        this.cartBean = cartBean;
        relative_progress.setVisibility(View.GONE);
        empty_cart_image.setVisibility(View.GONE);
        my_expanable_view.setVisibility(View.VISIBLE);

        for (int i =0;i<cartBean.getData().size();i++) {

            CartBean.DataBean dataBean = cartBean.getData().get(i);
            dataBean.setGroupChecked(isAllChildInGroupChecked(dataBean.getList()));
        }

        cart_check_all.setChecked(isAllGroupChecked(cartBean));

        myExpanableAdapter = new MyExpanableAdapter(getActivity(), cartBean,handler,relative_progress,fragmentCartPresenter);
        my_expanable_view.setAdapter(myExpanableAdapter);

        for (int i = 0;i<cartBean.getData().size();i++) {
            my_expanable_view.expandGroup(i);
        }

        myExpanableAdapter.sendPriceAndCount();

        my_expanable_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("pid",cartBean.getData().get(groupPosition).getList().get(childPosition).getPid());
                startActivity(intent);

                return false;
            }
        });
    }

    private boolean isAllGroupChecked(CartBean cartBean) {

        for (int i=0;i<cartBean.getData().size();i++) {
            if (! cartBean.getData().get(i).isGroupChecked()) {
                return false;
            }
        }

        return true;
    }

    private boolean isAllChildInGroupChecked(List<CartBean.DataBean.ListBean> list) {

        for (int i =0;i<list.size();i++) {
            if (list.get(i).getSelected() == 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cart_check_all:

                myExpanableAdapter.setAllChildChecked(cart_check_all.isChecked());
                break;
            case R.id.text_buy:
                if ("去结算(0)".equals(text_buy.getText().toString())) {
                    return;
                }

                Intent intent = new Intent(getActivity(), MakeSureOrderActivity.class);
                list_selected.clear();

                for (int i = 0;i<cartBean.getData().size();i++) {

                    List<CartBean.DataBean.ListBean> list = cartBean.getData().get(i).getList();
                    for (int j = 0;j<list.size();j++) {

                        if (list.get(j).getSelected() == 1) {
                            list_selected.add(list.get(j));
                        }
                    }
                }
                intent.putExtra("list_selected",list_selected);

                startActivity(intent);
                break;
        }
    }
}
