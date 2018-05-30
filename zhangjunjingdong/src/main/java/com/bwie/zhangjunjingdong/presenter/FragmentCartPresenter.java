package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.CartModel;
import com.bwie.zhangjunjingdong.model.bean.CartBean;
import com.bwie.zhangjunjingdong.presenter.inter.CartPresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.FragmentCartInter;

public class FragmentCartPresenter implements CartPresenterInter {

    private FragmentCartInter fragmentCartInter;
    private CartModel cartModel;

    public FragmentCartPresenter(FragmentCartInter fragmentCartInter) {
        this.fragmentCartInter = fragmentCartInter;

        cartModel = new CartModel(this);
    }

    public void getCartData(String selectCart, String uid) {

        cartModel.getCartData(selectCart,uid);

    }

    @Override
    public void getCartDataNull() {
        fragmentCartInter.getCartDataNull();
    }

    @Override
    public void getCartDataSuccess(CartBean cartBean) {
        fragmentCartInter.getCartDataSuccess(cartBean);
    }
}
