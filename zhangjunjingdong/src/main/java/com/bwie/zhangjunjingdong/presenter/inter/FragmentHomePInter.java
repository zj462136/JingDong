package com.bwie.zhangjunjingdong.presenter.inter;

import com.bwie.zhangjunjingdong.model.bean.FenLeiBean;
import com.bwie.zhangjunjingdong.model.bean.HomeBean;

public interface FragmentHomePInter {

    void onSuccess(HomeBean homeBean);

    void onFenLeiDataSuccess(FenLeiBean fenLeiBean);
}
