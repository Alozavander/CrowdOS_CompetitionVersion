package com.hills.mcs_02.dataBaseClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class testDB extends SQLiteOpenHelper {
    public testDB(Context context, String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public testDB(Context context,String name){
        super(context,name,null,1);
    }

    @Override
    public String getDatabaseName() {
        return super.getDatabaseName();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //如果首次获取数据库表格，便执行此方法创建一张表
        //这里创建一张测试用的task表
        String sql = "create table task(taskID integer primary key,taskName varchar(64),postTime varchar(64),deadLine varchar(64),postName varchar(64),coin integer,taskText text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
