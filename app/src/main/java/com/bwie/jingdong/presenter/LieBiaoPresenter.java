package com.bwie.jingdong.presenter;

import com.bwie.jingdong.bean.SouSuoBean;
import com.bwie.jingdong.model.LieBiaoModel;
import com.bwie.jingdong.view.ILieBiaoView;

/**
 * Created by lenovo on 2017/12/16.
 */
public class LieBiaoPresenter implements ILieBiaoPresenter {

    private ILieBiaoView iLieBiaoView;
    private final LieBiaoModel lieBiaoModel;

    public LieBiaoPresenter(ILieBiaoView iLieBiaoView){
        this.iLieBiaoView =iLieBiaoView;
        lieBiaoModel = new LieBiaoModel(this);
    }
    public void getData(String lieBiaoUrl,String keywords,int page){
        lieBiaoModel.getData(lieBiaoUrl,keywords,page);
    }

    @Override
    public void getSuccess(SouSuoBean souSuoBean) {
        iLieBiaoView.getSuccess(souSuoBean);
    }


}
