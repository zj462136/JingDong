package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.GetAllAddrModel;
import com.bwie.zhangjunjingdong.model.bean.GetAllAddrBean;
import com.bwie.zhangjunjingdong.presenter.inter.GetAllAddrPresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.GetAllAddrInter;

public class GetAllAddrPresenter implements GetAllAddrPresenterInter {

    private GetAllAddrInter getAllAddrInter;
    private GetAllAddrModel getAllAddrModel;

    public GetAllAddrPresenter(GetAllAddrInter getAllAddrInter) {
        this.getAllAddrInter = getAllAddrInter;
        getAllAddrModel = new GetAllAddrModel(this);
    }

    public void getAllAddr(String getAllAddrUrl, String uid) {
        getAllAddrModel.getAllAddr(getAllAddrUrl,uid);
    }

    @Override
    public void onGetAllAddrSuccess(GetAllAddrBean getAllAddrBean) {
        getAllAddrInter.onGetAllAddrSuccess(getAllAddrBean);
    }
}
