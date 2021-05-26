package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

//首页为图片轮换设置的数据适配器类
public class AdapterPagerViewHome extends PagerAdapter {
    private List<ImageView> views;
    private Context mContext;//作为能够准确定位到首页图片轮转view的媒介

    public AdapterPagerViewHome(List<ImageView> views, Context context) {
        this.views = views;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // return super.instantiateItem(container, position);
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView(views.get(position));
    }




}
