package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.AddCartModel;
import com.bwie.zhangjunjingdong.model.bean.AddCartBean;
import com.bwie.zhangjunjingdong.presenter.inter.AddCartPresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.ActivityAddCartInter;

public class AddCartPresenter implements AddCartPresenterInter {

    private ActivityAddCartInter activityAddCartInter;
    private AddCartModel addCartModel;

    public AddCartPresenter(ActivityAddCartInter activityAddCartInter) {
        this.activityAddCartInter = activityAddCartInter;

        addCartModel = new AddCartModel(this);
    }

    public void addToCart(String addCart, String uid, int pid) {

        addCartModel.addToCart(addCart,uid,pid);

    }

    @Override
    public void onCartAddSuccess(AddCartBean addCartBean) {
        activityAddCartInter.onCartAddSuccess(addCartBean);
    }
}
