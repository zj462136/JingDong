package com.bwie.jingdong.presenter;

import com.bwie.jingdong.bean.WoBean;
import com.bwie.jingdong.model.WoModel;
import com.bwie.jingdong.view.IWoView;

/**
 * Created by lenovo on 2018/1/8.
 */

public class WoPresenter implements IWoPresenter {
    private IWoView iWoView;
    private WoModel woModel;

    public WoPresenter(IWoView iWoView) {
        this.iWoView=iWoView;
        woModel = new WoModel(this);
    }
    public void getData(String woUrl,int uid){
        woModel.getData(woUrl,uid);
    }

    @Override
    public void getSuccess(WoBean woBean) {
        iWoView.getSuccess(woBean);
    }
}
