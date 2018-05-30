package com.bwie.jingdong.presenter;

import com.bwie.jingdong.bean.CartBean;
import com.bwie.jingdong.model.CarModel;
import com.bwie.jingdong.view.ICartView;

/**
 * Created by lenovo on 2018/1/17.
 */

public class CarPresenter implements ICarPresenter{
    private final ICartView iCartView;
    private final CarModel carModel;

    public CarPresenter(ICartView iCartView) {
        this.iCartView=iCartView;
        carModel = new CarModel(this);
    }
    public void getData(String carUrl) {
        carModel.getData(carUrl);
    }

    @Override
    public void getSuccess(CartBean cartBean) {
        iCartView.getSuccess(cartBean);
    }
}
