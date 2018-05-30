package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.OrderListModel;
import com.bwie.zhangjunjingdong.model.bean.OrderListBean;
import com.bwie.zhangjunjingdong.presenter.inter.OrderListPresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.FragmentOrderListInter;

public class OrderListPresenter implements OrderListPresenterInter {

    private FragmentOrderListInter fragmentOrderListInter;
    private OrderListModel orderListModel;

    public OrderListPresenter(FragmentOrderListInter fragmentOrderListInter) {
        this.fragmentOrderListInter = fragmentOrderListInter;
        orderListModel = new OrderListModel(this);
    }

    public void getOrderData(String orderListUrl, String uid, int page) {

        orderListModel.getOrderData(orderListUrl,uid,page);

    }

    @Override
    public void onOrderDataSuccess(OrderListBean orderListBean) {

        fragmentOrderListInter.onOrderDataSuccess(orderListBean);
    }
}
