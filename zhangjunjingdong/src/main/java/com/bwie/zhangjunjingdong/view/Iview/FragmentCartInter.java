package com.bwie.zhangjunjingdong.view.Iview;

import com.bwie.zhangjunjingdong.model.bean.CartBean;

public interface FragmentCartInter {
    void getCartDataNull();

    void getCartDataSuccess(CartBean cartBean);
}
