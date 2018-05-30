package com.bwie.zhangjunjingdong.presenter;

import com.bwie.zhangjunjingdong.model.ProductListModel;
import com.bwie.zhangjunjingdong.model.bean.ProductListBean;
import com.bwie.zhangjunjingdong.presenter.inter.ProductListPresenterInter;
import com.bwie.zhangjunjingdong.view.Iview.ProductListActivityInter;

public class ProductListPresenter implements ProductListPresenterInter {

    private ProductListModel productListModel;
    private ProductListActivityInter productListActivityInter;

    public ProductListPresenter(ProductListActivityInter productListActivityInter) {
        this.productListActivityInter = productListActivityInter;

        productListModel = new ProductListModel(this);
    }

    public void getProductData(String seartchUrl, String keywords, int page) {

        productListModel.getProductData(seartchUrl,keywords,page);
    }

    @Override
    public void onSuccess(ProductListBean productListBean) {
        productListActivityInter.getProductDataSuccess(productListBean);
    }
}
