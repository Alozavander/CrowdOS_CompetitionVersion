package com.hills.mcs_02.previewImage;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Parcel;

import androidx.annotation.Nullable;

import com.previewlibrary.enitity.IThumbViewInfo;

@SuppressLint("ParcelCreator")
public class PreviewImageInfo implements IThumbViewInfo {
    private String url;  //图片地址
    private Rect mBounds; // 记录坐标

    public PreviewImageInfo(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public Rect getBounds() {//将你的图片显示坐标字段返回
        return mBounds;
    }

    @Nullable
    @Override
    public String getVideoUrl() {
        //用不到view，所以返回空
        return null;
    }

    public void setBounds(Rect bounds) {
        mBounds = bounds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
