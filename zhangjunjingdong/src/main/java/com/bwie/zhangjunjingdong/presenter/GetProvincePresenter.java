package com.bwie.zhangjunjingdong.presenter;

import android.content.Context;

import com.bwie.zhangjunjingdong.model.GetProvinceModel;
import com.bwie.zhangjunjingdong.model.bean.ProvinceBean;
import com.bwie.zhangjunjingdong.presenter.inter.GetProvincePresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.GetProvinceInter;

import java.util.List;

public class GetProvincePresenter implements GetProvincePresenterInter {

    private GetProvinceInter getProvinceInter;
    private GetProvinceModel getProvinceModel;

    public GetProvincePresenter(GetProvinceInter getProvinceInter) {
        this.getProvinceInter = getProvinceInter;
        getProvinceModel = new GetProvinceModel(this);
    }

    public void getProvince(Context context) {
        getProvinceModel.getProvince(context);
    }

    @Override
    public void onGetProvince(List<ProvinceBean> list) {
        getProvinceInter.onGetProvince(list);
    }
}
