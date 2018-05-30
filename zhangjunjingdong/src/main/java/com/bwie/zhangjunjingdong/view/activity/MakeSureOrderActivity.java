package com.bwie.zhangjunjingdong.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.CartBean;
import com.bwie.zhangjunjingdong.model.bean.CreateOrderBean;
import com.bwie.zhangjunjingdong.model.bean.DefaultAddrBean;
import com.bwie.zhangjunjingdong.model.bean.GetAllAddrBean;
import com.bwie.zhangjunjingdong.presenter.CreateOrderPresenter;
import com.bwie.zhangjunjingdong.presenter.GetDefaultAddrPresenter;
import com.bwie.zhangjunjingdong.util.ApiUtil;
import com.bwie.zhangjunjingdong.util.CommonUtils;
import com.bwie.zhangjunjingdong.util.OkHttp3Util_03;
import com.bwie.zhangjunjingdong.view.Iview.CreateOrderInter;
import com.bwie.zhangjunjingdong.view.Iview.DefaultAddrInter;
import com.bwie.zhangjunjingdong.view.adapter.SureOrderAdapter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MakeSureOrderActivity extends AppCompatActivity implements View.OnClickListener,CreateOrderInter,DefaultAddrInter {

    private ArrayList<CartBean.DataBean.ListBean> list_selected;
    private ImageView detail_image_back;
    private RecyclerView product_list_recycler;
    private TextView text_shi_fu_kuan;
    private TextView text_submit_order;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private double price;
    private CreateOrderPresenter createOrderPresenter;
    private int index;
    private GetDefaultAddrPresenter getDefaultAddrPresenter;
    private TextView text_name;
    private TextView text_phone;
    private TextView text_addr;
    private RelativeLayout relative_addr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_sure_order);

        //找到控件
        detail_image_back = findViewById(R.id.detail_image_back);
        product_list_recycler = findViewById(R.id.product_list_recycler);
        text_shi_fu_kuan = findViewById(R.id.text_shi_fu_ku);
        text_submit_order = findViewById(R.id.text_submit_order);

        text_name = findViewById(R.id.text_name);
        text_phone = findViewById(R.id.text_phone);
        text_addr = findViewById(R.id.text_addr);
        relative_addr = findViewById(R.id.relative_addr_01);


        //获取选中的购物车数据的集合
        list_selected = (ArrayList<CartBean.DataBean.ListBean>) getIntent().getSerializableExtra("list_selected");

        //获取默认的地址...有默认地址则显示,,,没有则弹出一个dialog
        getDefaultAddrPresenter = new GetDefaultAddrPresenter(this);

        getDefaultAddrPresenter.getDefaultAddr(ApiUtil.GET_DEFAULT_ADDR_URL, CommonUtils.getString("uid"));

    }

    private void initData() {
        createOrderPresenter = new CreateOrderPresenter(this);


        //设置点击事件
        detail_image_back.setOnClickListener(this);
        text_submit_order.setOnClickListener(this);
        relative_addr.setOnClickListener(this);

        //布局管理器
        product_list_recycler.setLayoutManager(new LinearLayoutManager(MakeSureOrderActivity.this));

        //添加分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MakeSureOrderActivity.this, LinearLayout.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.item_decoration_shape));

        product_list_recycler.addItemDecoration(dividerItemDecoration);
        //设置适配器
        SureOrderAdapter sureOrderAdapter = new SureOrderAdapter(MakeSureOrderActivity.this, list_selected);
        product_list_recycler.setAdapter(sureOrderAdapter);

        price = 0;
        //显示实付款...计算价格
        for (int i = 0;i<list_selected.size(); i++) {
            price += list_selected.get(i).getBargainPrice() * list_selected.get(i).getNum();
        }
        //格式化两位
        String priceString = decimalFormat.format(price);
        text_shi_fu_kuan.setText("实付款:¥"+priceString);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_image_back://返回

                finish();

                break;
            case R.id.text_submit_order://提交订单...生成订单

                //实际上点击提交订单的操作是:1.生成这个订单2.调用支付宝/微信/网银进行付款(付款的操作在后边学)

                //https://www.zhaoapi.cn/product/createOrder?uid=71&price=99.99

                createOrderPresenter.createOrder(ApiUtil.CREATE_ORDER_URL, CommonUtils.getString("uid"),price);
                break;
            case R.id.relative_addr_01://跳转到选择收货地址页面...回传数据
                Intent intent = new Intent(MakeSureOrderActivity.this,ChooseAddrActivity.class);

                startActivityForResult(intent,2001);
                break;
        }
    }

    @Override
    public void onCreateOrderSuccess(CreateOrderBean createOrderBean) {

        if ("0".equals(createOrderBean.getCode())) {//创建订单成功...成功之后才能去付款
            //无论付款成功/失败/取消 该订单都已经创建了,,,需要跳转到订单列表,,,并且购物车里面相关商品需要删除

            //1.订单创建成功之后 删除购物车列表中对应的商品...使用递归删除选中的商品
            index = 0;
            deleteProductInCart(list_selected);

        }else {
            Toast.makeText(MakeSureOrderActivity.this,createOrderBean.getMsg(),Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 删除购物车
     * @param list_selected
     */
    private void deleteProductInCart(final ArrayList<CartBean.DataBean.ListBean> list_selected) {

        CartBean.DataBean.ListBean listBean = list_selected.get(index);

        //请求删除购物车的接口...删除成功之后 再次请求查询购物车
        Map<String, String> params = new HashMap<>();
        //?uid=72&pid=1
        params.put("uid",CommonUtils.getString("uid"));
        params.put("pid", String.valueOf(listBean.getPid()));

        OkHttp3Util_03.doPost(ApiUtil.DELETE_CART_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//删除一条成功,,,index++

                    CommonUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            index ++;//判断是否继续删除.,..如果index<list.size() 继续,,,不是代表全部删完了
                            if (index < list_selected.size()) {
                                //继续
                                deleteProductInCart(list_selected);
                            }else {
                                //删除完成...//1.调支付的操作...//2.跳转到订单列表页面
                                Toast.makeText(MakeSureOrderActivity.this,"应该调用支付的操作,然后再跳转订单列表",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MakeSureOrderActivity.this,OrderListActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 回调的是有默认地址
     * @param defaultAddrBean
     */
    @Override
    public void onGetDefaultAddrSuccess(DefaultAddrBean defaultAddrBean) {
       //显示地址...设置适配器
        text_name.setText("收货人: "+defaultAddrBean.getData().getName());
        text_phone.setText(defaultAddrBean.getData().getMobile()+"");
        text_addr.setText("收货地址: "+defaultAddrBean.getData().getAddr());

        //有地址的时候进行设置适配器
        initData();

    }

    /**
     * 回调的是空地址
     */
    @Override
    public void onGetDefaultAddrEmpty() {
        //弹出对话框...取消,,,finish/////确定...添加新地址...没添加点击了返回,当前确认订单页面finish,,,如果添加了显示地址
        AlertDialog.Builder builder = new AlertDialog.Builder(MakeSureOrderActivity.this);
        builder.setTitle("提示")
                .setMessage("您还没有默认的收货地址,请设置收货地址")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //结束确认订单显示
                        MakeSureOrderActivity.this.finish();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //确定...跳转添加新地址...如果没有保存地址,确认订单finish,,,
                        //如果保存了地址...数据传回来进行显示(带有回传值的跳转)
                        Intent intent = new Intent(MakeSureOrderActivity.this,AddNewAddrActivity.class);

                        startActivityForResult(intent,1001);
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == 1002) {
            //如果没有数据...没有保存,,,finish当前订单页面
            if (data == null) {
                finish();

                return;
            }
            //如果有数据...显示地址...设置适配器
            text_name.setText("收货人: "+data.getStringExtra("name"));
            text_phone.setText(data.getStringExtra("phone"));
            text_addr.setText("收货地址: "+data.getStringExtra("addr"));

            initData();
        }

        if (requestCode == 2001 && resultCode == 2002) {//选择新地址过来的
            if (data == null) {
                return;
            }

            GetAllAddrBean.DataBean dataBean = (GetAllAddrBean.DataBean) data.getSerializableExtra("addrBean");

            text_name.setText("收货人: "+dataBean.getName());
            text_phone.setText(String.valueOf(dataBean.getMobile()));
            text_addr.setText("收货地址: "+dataBean.getAddr());

        }

    }
}
