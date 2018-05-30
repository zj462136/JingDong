package com.bwie.zhangjunjingdong.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.GetAllAddrBean;
import com.bwie.zhangjunjingdong.presenter.GetAllAddrPresenter;
import com.bwie.zhangjunjingdong.util.ApiUtil;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.view.Iview.GetAllAddrInter;
import com.bwie.zhangjunjingdong.view.adapter.ManageAddrAdapter;

public class ManageAddrActivity extends AppCompatActivity implements GetAllAddrInter, View.OnClickListener {

    private GetAllAddrPresenter getAllAddrPresenter;
    private ImageView detail_image_back;
    private ListView list_view_addr;
    private TextView text_add_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_addr);

        detail_image_back = findViewById(R.id.detail_image_back);
        list_view_addr = findViewById(R.id.list_view_addr);
        text_add_new = findViewById(R.id.text_add_new);

        getAllAddrPresenter = new GetAllAddrPresenter(this);

        text_add_new.setOnClickListener(this);
        detail_image_back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //获取地址列表的最新数据
        getAllAddrPresenter.getAllAddr(ApiUtil.GET_ALL_ADDR_URL, CommonUtils.getString("uid"));


    }

    @Override
    public void onGetAllAddrSuccess(GetAllAddrBean getAllAddrBean) {
        if ("0".equals(getAllAddrBean.getCode())) {

            //设置适配器
            ManageAddrAdapter manageAddrAdapter = new ManageAddrAdapter(ManageAddrActivity.this, getAllAddrBean.getData(),getAllAddrPresenter);
            list_view_addr.setAdapter(manageAddrAdapter);


            //设置条目的点击事件
            list_view_addr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_image_back:
                finish();;
                break;
            case R.id.text_add_new:
                Intent intent = new Intent(ManageAddrActivity.this,AddNewAddrActivity.class);
                startActivity(intent);
                break;
        }
    }
}
