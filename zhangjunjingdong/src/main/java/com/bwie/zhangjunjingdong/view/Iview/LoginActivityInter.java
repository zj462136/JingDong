package com.bwie.zhangjunjingdong.view.Iview;

import com.bwie.zhangjunjingdong.model.bean.LoginBean;

public interface LoginActivityInter {

    void getLoginSuccess(LoginBean loginBean);


    void getLoginSuccessByQQ(LoginBean loginBean, String ni_cheng, String iconurl);
}
