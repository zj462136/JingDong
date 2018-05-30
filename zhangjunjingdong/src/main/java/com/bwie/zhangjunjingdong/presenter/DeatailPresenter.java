package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.DeatilModel;
import com.bwie.zhangjunjingdong.model.bean.DeatilBean;
import com.bwie.zhangjunjingdong.presenter.inter.DeatilPresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.DetailActivityInter;

public class DeatailPresenter implements DeatilPresenterInter {

    private DeatilModel deatilModel;
    private DetailActivityInter detailActivityInter;

    public DeatailPresenter(DetailActivityInter detailActivityInter) {
        this.detailActivityInter = detailActivityInter;

        deatilModel = new DeatilModel(this);

    }

    public void getDetailData(String detailUrl, int pid) {

        deatilModel.getDetailData(detailUrl,pid);
    }

    @Override
    public void onSuccess(DeatilBean deatilBean) {

        detailActivityInter.onSuccess(deatilBean);
    }
}
