package com.bwie.zhangjunjingdong.presenter.inter;

import com.bwie.zhangjunjingdong.model.bean.CartBean;

public interface CartPresenterInter {
    void getCartDataNull();

    void getCartDataSuccess(CartBean cartBean);
}
