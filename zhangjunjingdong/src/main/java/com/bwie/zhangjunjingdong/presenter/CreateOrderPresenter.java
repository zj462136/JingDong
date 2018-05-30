package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.CreateOrderModel;
import com.bwie.zhangjunjingdong.model.bean.CreateOrderBean;
import com.bwie.zhangjunjingdong.presenter.inter.CreateOrderPresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.CreateOrderInter;

public class CreateOrderPresenter implements CreateOrderPresenterInter {

    private CreateOrderInter createOrderInter;
    private CreateOrderModel createOrderModel;

    public CreateOrderPresenter(CreateOrderInter createOrderInter) {
        this.createOrderInter = createOrderInter;
        createOrderModel = new CreateOrderModel(this);
    }

    public void createOrder(String createOrderUrl, String uid, double price) {

        createOrderModel.createOrder(createOrderUrl,uid,price);

    }

    @Override
    public void onOrderCreateSuccess(CreateOrderBean createOrderBean) {
        createOrderInter.onCreateOrderSuccess(createOrderBean);
    }
}
