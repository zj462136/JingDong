package com.bwie.jingdong.presenter;

import com.bwie.jingdong.bean.ZiZiFenLeiBean;
import com.bwie.jingdong.model.ZiZiFenLeiModel;
import com.bwie.jingdong.view.IZiZiFenLeiView;

import java.util.Map;

/**
 * Created by lenovo on 2018/1/4.
 */

public class ZiZiFenLeiPresenter implements IZiZiFenLeiPresenter{
    private IZiZiFenLeiView iZiZiFenLeiView;
    private final ZiZiFenLeiModel ziZiFenLeiModel;

    public ZiZiFenLeiPresenter(IZiZiFenLeiView iZiZiFenLeiView){
        this.iZiZiFenLeiView=iZiZiFenLeiView;
        ziZiFenLeiModel = new ZiZiFenLeiModel(this);
    }
    public void getData(String ziziFenLeiUrl, Map<String,String> param){
        ziZiFenLeiModel.getData(ziziFenLeiUrl,param);
    }
    @Override
    public void getSuccess(ZiZiFenLeiBean ziZiFenLeiBean) {
        iZiZiFenLeiView.getSuccess(ziZiFenLeiBean);
    }
}
