package com.hills.mcs_02.sensorFunction;

import android.content.Context;
import android.content.SharedPreferences;

/*
 * Edit by Zeron
 * Time: 2020.2.25
 * Intro: 通过此类完成SP的存储/数据取出
 *
 */

public class SpHelper {
    private String SpName;
    private Context mContext;
    private SharedPreferences sp;

    /**
     * 获取SP实例，多实例
     *
     * @param spName SP表单名称
     * @param context 原本上下文
     */
    private SpHelper(String spName, Context context){
        SpName = spName;
        mContext = context;
        sp = mContext.getSharedPreferences(SpName, Context.MODE_PRIVATE);
    }

    public boolean dataStore(String key, String data){
        return sp.edit().putString(key,data).commit();
    }
}
