package com.bwie.jingdong.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bwie.jingdong.helper.MyHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/12/28.
 */

public class Dao {

    MyHelper helper;

    public Dao(Context context) {
        helper = new MyHelper(context);
    }

    public List<String> select() {
        SQLiteDatabase database = helper.getReadableDatabase();
        List<String> list=new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from shuju1", null);
        while(cursor.moveToNext()){
            String string = cursor.getString(1);
            list.add(string);
        }
        database.close();
        return list;
    }

    public int delYi(String s) {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL("delete from shuju1 where json=?",new String[]{s});
        database.close();
        return 1;
    }

    public void insert(String json) {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("json",json);
        database.insert("shuju1",null,values);
        database.close();
    }

    public void del() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL("delete from shuju1");
        database.close();
    }
}