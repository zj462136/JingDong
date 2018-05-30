package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.AddNewAddrModel;
import com.bwie.zhangjunjingdong.model.bean.AddNewAddrBean;
import com.bwie.zhangjunjingdong.presenter.inter.AddNewAddrPresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.AddNewAddrInter;

public class AddNewAddrPresenter implements AddNewAddrPresenterInter {

    private AddNewAddrInter addNewAddrInter;
    private AddNewAddrModel addNewAddrModel;

    public AddNewAddrPresenter(AddNewAddrInter addNewAddrInter) {
        this.addNewAddrInter = addNewAddrInter;
        addNewAddrModel = new AddNewAddrModel(this);
    }

    public void addNewAddr(String addNewAddrUrl, String uid, String addr, String phone, String name) {

        addNewAddrModel.addNewAddr(addNewAddrUrl,uid,addr,phone,name);
    }

    @Override
    public void onAddAddrSuccess(AddNewAddrBean addNewAddrBean) {
        addNewAddrInter.onAddNewAddrSuccess(addNewAddrBean);
    }
}
