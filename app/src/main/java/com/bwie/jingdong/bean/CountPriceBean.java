package com.bwie.jingdong.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/1/17.
 */

public class CountPriceBean implements Serializable{
    private String priceString;
    private int count;

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public CountPriceBean() {

    }

    public CountPriceBean(String priceString, int count) {

        this.priceString = priceString;
        this.count = count;
    }
}
