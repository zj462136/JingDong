package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.FragmentHomeModel;
import com.bwie.zhangjunjingdong.model.bean.FenLeiBean;
import com.bwie.zhangjunjingdong.model.bean.HomeBean;
import com.bwie.zhangjunjingdong.presenter.inter.FragmentHomePInter;
import com.bwie.zhangjunjingdong.view.Iview.InterFragmentHome;

public class FragmentHomeP implements FragmentHomePInter {

    private FragmentHomeModel fragmentHomeModel;
    private InterFragmentHome interFragmentHome;

    public FragmentHomeP(InterFragmentHome interFragmentHome) {
        this.interFragmentHome = interFragmentHome;

        fragmentHomeModel = new FragmentHomeModel(this);
    }

    public void getNetData(String homeUrl) {

        fragmentHomeModel.getData(homeUrl);

    }

    @Override
    public void onSuccess(HomeBean homeBean) {

        interFragmentHome.onSuccess(homeBean);
    }

    @Override
    public void onFenLeiDataSuccess(FenLeiBean fenLeiBean) {
        interFragmentHome.onFenLeiDataSuccess(fenLeiBean);
    }

    public void getFenLeiData(String fenLeiUrl) {

        fragmentHomeModel.getFenLeiData(fenLeiUrl);
    }
}
