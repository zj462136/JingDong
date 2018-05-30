package com.bwie.zhangjunjingdong.presenter.inter;

import com.bwie.zhangjunjingdong.model.bean.DefaultAddrBean;

public interface GetDefaultAddrPresenterInter {
    void onGetDefaultAddrSuccess(DefaultAddrBean defaultAddrBean);

    void onGetDefaultAddrEmpty();
}
