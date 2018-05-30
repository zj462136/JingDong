package com.bwie.zhangjunjingdong.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bwie.zhangjunjingdong.model.bean.ProvinceBean;
import com.bwie.zhangjunjingdong.model.db.AddrDao;
import com.bwie.zhangjunjingdong.presenter.inter.GetProvincePresenterInter;

import java.util.ArrayList;
import java.util.List;

public class GetProvinceModel {
    private GetProvincePresenterInter getProvincePresenterInter;

    public GetProvinceModel(GetProvincePresenterInter getProvincePresenterInter) {
        this.getProvincePresenterInter = getProvincePresenterInter;
    }

    public void getProvince(Context context) {
        List<ProvinceBean> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = new AddrDao(context).initAddrDao();
        Cursor cursor = sqLiteDatabase.query("bma_regions", null, "parentid = 0", null, null, null, null);
        while (cursor.moveToNext()) {
            int regionid = cursor.getInt(cursor.getColumnIndex("regionid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            ProvinceBean provinceBean = new ProvinceBean(regionid, name);
            list.add(provinceBean);
        }
        cursor.close();
        sqLiteDatabase.close();
        getProvincePresenterInter.onGetProvince(list);
    }
}
