package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.FragmentFenLeiRightModel;
import com.bwie.zhangjunjingdong.model.bean.ChildFenLeiBean;
import com.bwie.zhangjunjingdong.presenter.inter.FenLeiRightPresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.FenLeiRightInter;

public class FragmentFenLeiRightPresenter implements FenLeiRightPresenterInter {

    private FenLeiRightInter fenLeiRightInter;
    private FragmentFenLeiRightModel fragmentFenLeiRightModel;

    public FragmentFenLeiRightPresenter(FenLeiRightInter fenLeiRightInter) {
        this.fenLeiRightInter = fenLeiRightInter;

        fragmentFenLeiRightModel = new FragmentFenLeiRightModel(this);
    }

    public void getChildData(String childFenLeiUrl, int cid) {

        fragmentFenLeiRightModel.getChildData(childFenLeiUrl,cid);
    }

    @Override
    public void onSuncess(ChildFenLeiBean childFenLeiBean) {

        fenLeiRightInter.getSuccessChildData(childFenLeiBean);

    }
}
