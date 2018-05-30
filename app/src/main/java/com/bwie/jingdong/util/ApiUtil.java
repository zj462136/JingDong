package com.bwie.jingdong.util;

/**
 * Created by Dash on 2017/12/17.
 */
public class ApiUtil {
    //首页
    public static final String SHOUYEURL = "https://www.zhaoapi.cn/ad/getAd";
    //分类，九宫格
    public static final String FENLEIURL ="https://www.zhaoapi.cn/product/getCatagory";
    //子分类                                   https://www.zhaoapi.cn/product/getProducts
    public static final String ZIFENLEIURL ="https://www.zhaoapi.cn/product/getProductCatagory";

    public static final String ZIZIFENLEIURL ="https://www.zhaoapi.cn/product/getProducts";
    //商品详情                              https://www.zhaoapi.cn/product/getProductDetail?pid
    public static final String GOODSURL ="https://www.zhaoapi.cn/product/getProductDetail";
    //        https://www.zhaoapi.cn/product/getProductDetail
    //搜索            ?keywords=笔记本&page=1
    public static final String SOUSUOURL ="https://www.zhaoapi.cn/product/searchProducts";
//购物车
    public static final String cartUrl = "https://www.zhaoapi.cn/product/getCarts";

//添加
    public static final String addCartUrl = "https://www.zhaoapi.cn/product/addCart";//uid,pid
    //删除
    public static final String deleteCartUrl = "https://www.zhaoapi.cn/product/deleteCart";//uid,pid
//更新
    //?uid=71&sellerid=1&pid=1&selected=0&num=10
    public static final String updateCartUrl = "https://www.zhaoapi.cn/product/updateCarts";

    //创建订单
    public static final String createCartUrl = "https://www.zhaoapi.cn/product/createOrder";





}
