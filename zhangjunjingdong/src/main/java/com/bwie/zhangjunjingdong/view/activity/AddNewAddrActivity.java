package com.bwie.zhangjunjingdong.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.AddNewAddrBean;
import com.bwie.zhangjunjingdong.presenter.AddNewAddrPresenter;
import com.bwie.zhangjunjingdong.util.ApiUtil;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.view.Iview.AddNewAddrInter;

public class AddNewAddrActivity extends AppCompatActivity implements View.OnClickListener,AddNewAddrInter {

    private ImageView detail_image_back;
    private TextView text_save;
    private EditText edit_person;
    private EditText edit_phone;
    private TextView edit_area;
    private EditText edit_road;
    private AddNewAddrPresenter addNewAddrPresenter;
    private LinearLayout linear_area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_addr);

        detail_image_back = findViewById(R.id.detail_image_back);
        text_save = findViewById(R.id.text_save);
        edit_person = findViewById(R.id.edit_person);
        edit_phone = findViewById(R.id.edit_phone);
        edit_area = findViewById(R.id.edit_area);
        edit_road = findViewById(R.id.edit_road);
        linear_area = findViewById(R.id.linear_area);

        detail_image_back.setOnClickListener(this);
        text_save.setOnClickListener(this);
        linear_area.setOnClickListener(this);//选择地址

        addNewAddrPresenter = new AddNewAddrPresenter(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_image_back:

                finish();//没传值回去...此时确认订单需要finish

                break;
            case R.id.text_save://保存
                //请求添加新地址的接口...点击保存的时候需要做一系列的非空判断
                //uid=71&addr=北京市昌平区金域国际1-1-1&mobile=18612991023&name=kson
                addNewAddrPresenter.addNewAddr(ApiUtil.ADD_NEW_ADDR_URL, CommonUtils.getString("uid"),edit_area.getText().toString()+edit_road.getText().toString(),edit_phone.getText().toString(),edit_person.getText().toString());


                break;
            case R.id.linear_area://选择地址
                Intent intent = new Intent(AddNewAddrActivity.this,ChooseDistinctActivity.class);
                startActivityForResult(intent,3001);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==3001 && resultCode == 3002) {
            if (data == null) {
                return;
            }

            String addr = data.getStringExtra("addr");
            edit_area.setText(addr);

        }

    }

    @Override
    public void onAddNewAddrSuccess(AddNewAddrBean addNewAddrBean) {
        if ("0".equals(addNewAddrBean.getCode())) {//保存成功
            //请求添加成功之后...回传值给确认订单页面进行显示

            Intent intent = new Intent();

            intent.putExtra("name",edit_person.getText().toString());
            intent.putExtra("phone",edit_phone.getText().toString());
            intent.putExtra("addr",edit_area.getText().toString()+edit_road.getText().toString());

            setResult(1002,intent);

            finish();

        }
    }
}
