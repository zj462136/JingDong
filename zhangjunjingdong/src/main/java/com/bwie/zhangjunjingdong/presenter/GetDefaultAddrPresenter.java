package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.GetDefaultAddrModel;
import com.bwie.zhangjunjingdong.model.bean.DefaultAddrBean;
import com.bwie.zhangjunjingdong.presenter.inter.GetDefaultAddrPresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.DefaultAddrInter;

public class GetDefaultAddrPresenter implements GetDefaultAddrPresenterInter {

    private DefaultAddrInter defaultAddrInter;
    private GetDefaultAddrModel getDefaultAddrModel;

    public GetDefaultAddrPresenter(DefaultAddrInter defaultAddrInter) {
        this.defaultAddrInter = defaultAddrInter;
        getDefaultAddrModel = new GetDefaultAddrModel(this);
    }

    public void getDefaultAddr(String getDefaultAddrUrl, String uid) {
        getDefaultAddrModel.getDefaultAddr(getDefaultAddrUrl,uid);
    }

    @Override
    public void onGetDefaultAddrSuccess(DefaultAddrBean defaultAddrBean) {
        defaultAddrInter.onGetDefaultAddrSuccess(defaultAddrBean);
    }

    @Override
    public void onGetDefaultAddrEmpty() {
        defaultAddrInter.onGetDefaultAddrEmpty();
    }
}
