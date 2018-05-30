package com.bwie.zhangjunjingdong.presenter.inter;

import com.bwie.zhangjunjingdong.model.bean.LoginBean;

public interface LoginPresenterInter {

    void onSuccess(LoginBean loginBean);


    void onSuccessByQQ(LoginBean loginBean, String ni_cheng, String iconurl);
}
