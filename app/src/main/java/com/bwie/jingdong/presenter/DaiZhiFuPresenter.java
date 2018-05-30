package com.bwie.jingdong.presenter;

import com.bwie.jingdong.bean.DingDanLieBiaoBean;
import com.bwie.jingdong.model.DaiZhifuModel;
import com.bwie.jingdong.view.IDaiZhifu;

public class DaiZhiFuPresenter implements IDaiZhiFuPresenter {

    private final IDaiZhifu iDaiZhifu;
    private final DaiZhifuModel daiZhifuModel;

    public DaiZhiFuPresenter(IDaiZhifu iDaiZhifu) {
        this.iDaiZhifu =iDaiZhifu;
        daiZhifuModel = new DaiZhifuModel(this);
    }
    public void getData(String url,int page){
        daiZhifuModel.getData(url,page);
    }

    @Override
    public void getSuccess(DingDanLieBiaoBean dingDanLieBiaoBean) {
        iDaiZhifu.getSuccess(dingDanLieBiaoBean);
    }
}
