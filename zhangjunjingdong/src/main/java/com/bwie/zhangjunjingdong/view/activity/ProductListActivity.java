package com.bwie.zhangjunjingdong.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.ProductListBean;
import com.bwie.zhangjunjingdong.presenter.ProductListPresenter;
import com.bwie.zhangjunjingdong.util.ApiUtil;
import com.bwie.zhangjunjingdong.view.Iview.OnItemListner;
import com.bwie.zhangjunjingdong.view.Iview.ProductListActivityInter;
import com.bwie.zhangjunjingdong.view.adapter.ProDuctGridAdapter;
import com.bwie.zhangjunjingdong.view.adapter.ProDuctListAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductListActivityInter, View.OnClickListener {

    private String keywords;
    private RecyclerView product_list_recycler;
    private RecyclerView product_grid_recycler;
    private ProductListPresenter productListPresenter;
    private int page = 1;
    private ProDuctListAdapter proDuctListAdapter;
    private ProDuctGridAdapter proDuctGridAdapter;
    private ImageView product_image_back;
    private LinearLayout linear_search;
    private ImageView image_change;
    private boolean isList = true;//是否是列表展示
    private RefreshLayout refreshLayout;
    private List<ProductListBean.DataBean> listAll  = new ArrayList<>();//装当前页面所有的数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        product_list_recycler = findViewById(R.id.product_list_recycler);
        product_grid_recycler = findViewById(R.id.product_grid_recycler);
        product_image_back = findViewById(R.id.product_image_back);
        linear_search = findViewById(R.id.linear_search);
        image_change = findViewById(R.id.image_change);
        refreshLayout = findViewById(R.id.refreshLayout);


        productListPresenter = new ProductListPresenter(this);

        //接收传递的关键词
        keywords = getIntent().getStringExtra("keywords");
        if (keywords != null) {
            //根据关键词和page去请求列表数据

            productListPresenter.getProductData(ApiUtil.SEARTCH_URL,keywords,page);

        }

        //设置列表布局
        product_list_recycler.setLayoutManager(new LinearLayoutManager(ProductListActivity.this));
        product_grid_recycler.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));


        //设置点击事件
        product_image_back.setOnClickListener(this);
        linear_search.setOnClickListener(this);
        image_change.setOnClickListener(this);

        //下拉刷新的监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                //集合清空
                listAll.clear();
                //重新获取数据
                productListPresenter.getProductData(ApiUtil.SEARTCH_URL,keywords,page);

            }
        });
        //上拉加载的监听
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page ++;
                //重新获取数据
                productListPresenter.getProductData(ApiUtil.SEARTCH_URL,keywords,page);

            }
        });



    }

    @Override
    public void getProductDataSuccess(ProductListBean productListBean) {

        //先把数据添加到大集合
        listAll.addAll(productListBean.getData());

        //设置适配器就可以了

        setAdapter();


        //条目的点击事件 调到详情页面
        proDuctListAdapter.setOnItemListner(new OnItemListner() {
            @Override
            public void onItemClick(int position) {

                //跳转详情
                Intent intent = new Intent(ProductListActivity.this,DetailActivity.class);
                intent.putExtra("pid",listAll.get(position).getPid());
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(int position) {

            }
        });
        proDuctGridAdapter.setOnItemListner(new OnItemListner() {
            @Override
            public void onItemClick(int position) {

                //跳转详情
                Intent intent = new Intent(ProductListActivity.this,DetailActivity.class);
                intent.putExtra("pid",listAll.get(position).getPid());
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(int position) {

            }
        });

    }

    private void setAdapter() {

        //设置列表设备器
        if (proDuctListAdapter == null) {
            proDuctListAdapter = new ProDuctListAdapter(ProductListActivity.this, listAll);
            product_list_recycler.setAdapter(proDuctListAdapter);
        }else {
            proDuctListAdapter.notifyDataSetChanged();
        }

        //设置表格适配器
        if (proDuctGridAdapter == null) {
            proDuctGridAdapter = new ProDuctGridAdapter(ProductListActivity.this, listAll);
            product_grid_recycler.setAdapter(proDuctGridAdapter);
        }else {
            proDuctGridAdapter.notifyDataSetChanged();
        }

        //停止刷新和加载更多
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_search:
                Toast.makeText(this,"即将跳转搜索...",Toast.LENGTH_SHORT).show();
                break;
            case R.id.product_image_back:
                finish();
                break;
            case R.id.image_change:

                if (isList) {//表示当前展示的是列表..图标变成列表样式...表格进行显示,列表隐藏...isList---false
                    image_change.setImageResource(R.drawable.kind_liner);

                    product_grid_recycler.setVisibility(View.VISIBLE);
                    product_list_recycler.setVisibility(View.GONE);

                    isList = false;
                }else {
                    image_change.setImageResource(R.drawable.kind_grid);

                    product_list_recycler.setVisibility(View.VISIBLE);
                    product_grid_recycler.setVisibility(View.GONE);

                    isList = true;
                }

                break;
        }
    }
}
