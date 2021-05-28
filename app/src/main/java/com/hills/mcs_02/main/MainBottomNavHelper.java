package com.hills.mcs_02.main;

import android.content.Context;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainBottomNavHelper<T> {
    private final SparseArray<Tab<T>> TAB = new SparseArray<Tab<T>>();

    private final Context M_CONTEXT;
    private final int CONTAINER_ID;
    private final FragmentManager FRAGMENT_MANAGER;
    private final OnTabChangeListener<T> M_LISTENER;

    private Tab<T> currentTab;

    public MainBottomNavHelper(Context mContext, int containerId, FragmentManager fragmentManager, OnTabChangeListener<T> mListener) {
        this.M_CONTEXT = mContext;
        this.CONTAINER_ID = containerId;
        this.FRAGMENT_MANAGER = fragmentManager;
        this.M_LISTENER = mListener;
    }

    public MainBottomNavHelper<T> add(int menuId, Tab<T> tab) {
        TAB.put(menuId, tab);
        return this;
    }

    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    public boolean performClickMenu(int menuId) {
        Tab<T> tab = TAB.get(menuId);
        if (tab != null) {
            doSelect(tab);
            return true;
        }

        return false;
    }

    private void doSelect(Tab<T> tab){
        Tab<T> oldTab = null;
        if (currentTab!=null){
            oldTab=currentTab;
            if (oldTab==tab){
                notifyReselect(tab);
                return;
            }
        }
        currentTab = tab;
        doTabChanged(currentTab,oldTab);
    }

    private void doTabChanged(Tab<T> newTab,Tab<T> oldTab){
        FragmentTransaction ft = FRAGMENT_MANAGER.beginTransaction();

        if (oldTab!=null){
            if (oldTab.fragment!=null){
                ft.detach(oldTab.fragment);
            }
        }

        if (newTab!=null){
            if (newTab.fragment==null){
               /** Create and cache for the first time */
                Fragment fragment = Fragment.instantiate(M_CONTEXT,newTab.clx.getName(),null);
                newTab.fragment = fragment;
                ft.add(CONTAINER_ID,fragment,newTab.clx.getName());
            }else {
                ft.attach(newTab.fragment);
            }
        }
        ft.commit();
        notifySelect(newTab,oldTab);
    }

    private void notifySelect(Tab<T> newTab,Tab<T> oldTab){
        if (M_LISTENER !=null){
            M_LISTENER.onTabChange(newTab,oldTab);
        }

    }

    private void notifyReselect(Tab<T> tab){

    }

    public static class Tab<T> {
        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        Class<?> clx;
        public T extra;
        private Fragment fragment;
    }

    public interface OnTabChangeListener<T> {
        void onTabChange(Tab<T> newTab, Tab<T> oldTab);
    }
}
