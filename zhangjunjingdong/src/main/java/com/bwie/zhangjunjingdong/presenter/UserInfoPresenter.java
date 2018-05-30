package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.UserInfoModel;
import com.bwie.zhangjunjingdong.model.bean.UserInfoBean;
import com.bwie.zhangjunjingdong.presenter.inter.UserInfoPresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.UserInforInter;

public class UserInfoPresenter implements UserInfoPresenterInter {

    private final UserInfoModel userInfoModel;
    private final UserInforInter userInforInter;

    public UserInfoPresenter(UserInforInter userInforInter) {
        this.userInforInter = userInforInter;
        userInfoModel = new UserInfoModel(this);
    }

    public void getUserInfo(String userInfoUrl, String uid) {

        userInfoModel.getUserInfo(userInfoUrl,uid);

    }

    @Override
    public void onUserInfoSUccess(UserInfoBean userInfoBean) {
        userInforInter.onUserInforSuccess(userInfoBean);
    }
}
