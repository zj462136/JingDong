package com.bwie.jingdong.bean;

import java.util.List;

/**
 * Created by lenovo on 2018/1/17.
 */

public class DingDanLieBiaoBean {
    /**
     * msg : 请求成功
     * code : 0
     * data : [{"createtime":"2017-12-21T16:20:59","orderid":5509,"price":42797,"status":0,"title":"订单测试标题71","uid":71},{"createtime":"2017-12-21T16:21:11","orderid":5510,"price":10026,"status":0,"title":"订单测试标题71","uid":71},{"createtime":"2017-12-21T16:21:13","orderid":5511,"price":15298,"status":2,"title":"订单测试标题71","uid":71},{"createtime":"2017-12-21T16:21:29","orderid":5512,"price":110,"status":0,"title":"订单测试标题71","uid":71},{"createtime":"2017-12-21T16:22:08","orderid":5520,"price":10026,"status":0,"title":"订单测试标题71","uid":71},{"createtime":"2017-12-21T16:23:59","orderid":5529,"price":9.9,"status":0,"title":"订单测试标题71","uid":71},{"createtime":"2017-12-21T16:23:59","orderid":5530,"price":0,"status":0,"title":"订单测试标题71","uid":71},{"createtime":"2017-12-21T16:24:45","orderid":5537,"price":398773,"status":0,"title":"订单测试标题71","uid":71},{"createtime":"2017-12-21T16:24:50","orderid":5538,"price":9.9,"status":0,"title":"订单测试标题71","uid":71},{"createtime":"2017-12-21T16:25:30","orderid":5541,"price":398773,"status":0,"title":"订单测试标题71","uid":71}]
     * page : 24
     */

    private String msg;
    private String code;
    private String page;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * createtime : 2017-12-21T16:20:59
         * orderid : 5509
         * price : 42797
         * status : 0
         * title : 订单测试标题71
         * uid : 71
         */

        private String createtime;
        private int orderid;
        private double price;
        private int status;
        private String title;
        private int uid;

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getOrderid() {
            return orderid;
        }

        public void setOrderid(int orderid) {
            this.orderid = orderid;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
