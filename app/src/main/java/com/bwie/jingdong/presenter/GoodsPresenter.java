package com.bwie.jingdong.presenter;

import com.bwie.jingdong.bean.GoodsBean;
import com.bwie.jingdong.model.GoodsModel;
import com.bwie.jingdong.view.IGoodsView;

/**
 * Created by lenovo on 2017/12/25.
 */

public class GoodsPresenter implements IGoodsPresenter {
    private IGoodsView iGoodsView;
    private GoodsModel goodsModel;

    public GoodsPresenter(IGoodsView iGoodsView){
        this.iGoodsView=iGoodsView;
        goodsModel = new GoodsModel(this);
    }
    public void getData(String goodsUrl,int pid){
        goodsModel.getData(goodsUrl,pid);
    }

    @Override
    public void getSuccess(GoodsBean goodsBean) {
        iGoodsView.getSuccess(goodsBean);
    }
}
