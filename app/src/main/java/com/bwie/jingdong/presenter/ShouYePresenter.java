package com.bwie.jingdong.presenter;

import com.bwie.jingdong.bean.ShouYeBean;
import com.bwie.jingdong.model.ShouyeModel;
import com.bwie.jingdong.view.IShouYeView;

/**
 * Created by lenovo on 2018/1/4.
 */

public class ShouYePresenter implements IShouYePresenter{
    private IShouYeView iShouYeView;
    private ShouyeModel shouyeModel;

    public ShouYePresenter(IShouYeView iShouYeView){
        this.iShouYeView=iShouYeView;
        shouyeModel = new ShouyeModel(this);
    }
    public void getData(String shouYeUrl){
        shouyeModel.getData(shouYeUrl);
    }

    @Override
    public void getSuccess(ShouYeBean shouYeBean) {
        iShouYeView.getSuccess(shouYeBean);
    }
}
