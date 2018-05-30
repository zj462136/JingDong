package com.bwie.jingdong.presenter;

import com.bwie.jingdong.bean.BuyBean;
import com.bwie.jingdong.model.BuyModel;
import com.bwie.jingdong.view.IBuyView;

/**
 * Created by lenovo on 2018/1/10.
 */

public class BuyPresenter implements IBuyPresenter{
    private IBuyView iBuyView;
    private BuyModel buyModel;

    public BuyPresenter(IBuyView iBuyView) {
        this.iBuyView =iBuyView;
        buyModel = new BuyModel(this);
    }
    public void getData(String buyUrl){
        buyModel.getData(buyUrl);
    }

    @Override
    public void getSuccess(BuyBean buyBean) {
        iBuyView.getSuccess(buyBean);
    }
}
