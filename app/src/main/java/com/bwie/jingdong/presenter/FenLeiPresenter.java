package com.bwie.jingdong.presenter;

import com.bwie.jingdong.bean.FenLeiBean;
import com.bwie.jingdong.model.FenLeiModel;
import com.bwie.jingdong.view.IFenLeiView;

/**
 * Created by lenovo on 2018/1/4.
 */

public class FenLeiPresenter implements IFenLeiPresenter{

    private FenLeiModel fenLeiModel;
    private IFenLeiView iFenLeiView;

    public FenLeiPresenter(IFenLeiView iFenLeiView) {
        this.iFenLeiView =iFenLeiView;
        fenLeiModel = new FenLeiModel(this);
    }
    public void getData(String fenLeiUrl){
        fenLeiModel.getData(fenLeiUrl);
    }
    @Override
    public void getSuccess(FenLeiBean fenLeiBean) {
        iFenLeiView.getSuccess(fenLeiBean);
    }
}
