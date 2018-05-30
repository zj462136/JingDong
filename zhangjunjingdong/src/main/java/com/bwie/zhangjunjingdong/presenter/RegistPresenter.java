package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.RegistModel;
import com.bwie.zhangjunjingdong.model.bean.RegistBean;
import com.bwie.zhangjunjingdong.presenter.inter.RegistPresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.RegistActivityInter;

public class RegistPresenter implements RegistPresenterInter {

    private RegistActivityInter registActivityInter;
    private RegistModel registModel;

    public RegistPresenter(RegistActivityInter registActivityInter) {
        this.registActivityInter = registActivityInter;
        registModel = new RegistModel(this);
    }

    public void registUser(String registUrl, String name, String pwd) {

        registModel.registUser(registUrl,name,pwd);
    }

    @Override
    public void onRegistSuccess(RegistBean registBean) {
        registActivityInter.onRegistSuccess(registBean);
    }
}
