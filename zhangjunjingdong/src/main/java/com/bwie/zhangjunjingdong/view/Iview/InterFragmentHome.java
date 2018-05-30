package com.bwie.zhangjunjingdong.view.Iview;

import com.bwie.zhangjunjingdong.model.bean.FenLeiBean;
import com.bwie.zhangjunjingdong.model.bean.HomeBean;

public interface InterFragmentHome {
    void onSuccess(HomeBean homeBean);

    void onFenLeiDataSuccess(FenLeiBean fenLeiBean);
}
