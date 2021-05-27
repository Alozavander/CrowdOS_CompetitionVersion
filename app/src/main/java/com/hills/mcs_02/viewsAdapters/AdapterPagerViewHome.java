package com.hills.mcs_02.viewsAdapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/** Homepage PagerView Adapter */
public class AdapterPagerViewHome extends PagerAdapter {
    private List<ImageView> views;
    private Context mContext;

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
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }




}
