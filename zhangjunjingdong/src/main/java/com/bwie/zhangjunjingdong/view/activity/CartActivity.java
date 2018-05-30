package com.bwie.zhangjunjingdong.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
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
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.view.Iview.FragmentCartInter;
import com.bwie.zhangjunjingdong.view.Iview.InterFragmentHome;
import com.bwie.zhangjunjingdong.view.Iview.OnItemListner;
import com.bwie.zhangjunjingdong.view.adapter.MyExpanableAdapter;
import com.bwie.zhangjunjingdong.view.adapter.TuiJianAdapter;
import com.bwie.zhangjunjingdong.view.custom.MyExpanableView;

import java.util.List;

public class CartActivity extends AppCompatActivity implements InterFragmentHome,FragmentCartInter, View.OnClickListener{

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
    private ImageView detail_image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frament_cart_layout);

        linear_login = findViewById(R.id.linear_login);
        cart_login = findViewById(R.id.cart_login);
        tui_jian_recycler = findViewById(R.id.tui_jian_recycler);
        my_expanable_view = findViewById(R.id.my_expanable_view);
        relative_progress = findViewById(R.id.relative_progress);
        cart_check_all = findViewById(R.id.cart_check_all);
        text_total = findViewById(R.id.text_total);
        text_buy = findViewById(R.id.text_buy);
        empty_cart_image = findViewById(R.id.empty_cart_image);
        detail_image_back = findViewById(R.id.detail_image_back);


        detail_image_back.setVisibility(View.VISIBLE);

        //去掉默认的指示器
        my_expanable_view.setGroupIndicator(null);


        tui_jian_recycler.setFocusable(false);


        fragmentHomeP = new FragmentHomeP(this);

        fragmentHomeP.getNetData(ApiUtil.HOME_URL);

        fragmentCartPresenter = new FragmentCartPresenter(this);


        cart_check_all.setOnClickListener(this);
        detail_image_back.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();


    }

    private void initData() {

        if (CommonUtils.getBoolean("isLogin")) {

            my_expanable_view.setVisibility(View.VISIBLE);
            linear_login.setVisibility(View.GONE);


            getCartData();


        }else {

            linear_login.setVisibility(View.VISIBLE);
            my_expanable_view.setVisibility(View.GONE);


            cart_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CartActivity.this,LoginActivity.class);
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
    public void onSuccess(final HomeBean homeBean) {

        tui_jian_recycler.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));

        TuiJianAdapter tuiJianAdapter = new TuiJianAdapter(CartActivity.this, homeBean.getTuijian());
        tui_jian_recycler.setAdapter(tuiJianAdapter);

        tuiJianAdapter.setOnItemListner(new OnItemListner() {
            @Override
            public void onItemClick(int position) {


                Intent intent = new Intent(CartActivity.this, DetailActivity.class);

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
        Toast.makeText(CartActivity.this,"购物车为空,先去逛逛吧",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getCartDataSuccess(final CartBean cartBean) {

        relative_progress.setVisibility(View.GONE);
        empty_cart_image.setVisibility(View.GONE);


        for (int i =0;i<cartBean.getData().size();i++) {


            CartBean.DataBean dataBean = cartBean.getData().get(i);
            dataBean.setGroupChecked(isAllChildInGroupChecked(dataBean.getList()));
        }


        cart_check_all.setChecked(isAllGroupChecked(cartBean));

        myExpanableAdapter = new MyExpanableAdapter(CartActivity.this, cartBean,handler,relative_progress,fragmentCartPresenter);
        my_expanable_view.setAdapter(myExpanableAdapter);


        for (int i = 0;i<cartBean.getData().size();i++) {
            my_expanable_view.expandGroup(i);
        }

        myExpanableAdapter.sendPriceAndCount();


        my_expanable_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {


                Intent intent = new Intent(CartActivity.this, DetailActivity.class);

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
            case R.id.cart_check_all://全选

                myExpanableAdapter.setAllChildChecked(cart_check_all.isChecked());
                break;
            case R.id.detail_image_back:

                finish();
                break;
        }
    }
}
