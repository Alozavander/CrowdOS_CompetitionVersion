package com.hills.mcs_02.languageChange;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;


import com.hills.mcs_02.LogUtils;

import java.util.Locale;

public class MultiLanguageUtil {
    private static final String TAG = "MultiLanguageUtil_reference";
    /**
     * TODO 1、 修改应用内语言设置
     * @param language    语言  zh/en
     * @param area      地区
     */
    public static void changeLanguage(Context context,String language, String area) {
        if (TextUtils.isEmpty(language) && TextUtils.isEmpty(area)) {
            //如果语言和地区都是空，那么跟随系统s
            SpUtils.put(context, Constants.SP_LANGUAGE,"");
            SpUtils.put(context, Constants.SP_COUNTRY,"");
        } else {
            //不为空，那么修改app语言，并true是把语言信息保存到sp中，false是不保存到sp中
            Locale newLocale = new Locale(language, area);
            setAppLanguage(context,newLocale);
            saveLanguageSetting(context, newLocale);
        }
    }

    /**
     * Todo 更新应用语言
     * @param context
     * @param locale
     */
    private static Context setAppLanguage(Context context, Locale locale) {
        Context context1 = context;
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale);
            configuration.setLocales(new LocaleList(locale));
            context1 = context.createConfigurationContext(configuration);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration,metrics);
        } else {
            configuration.locale = locale;
            resources.updateConfiguration(configuration,metrics);
        }
        return context1;
    }

    /**
     * TODO 3、 跟随系统语言
     */
    public static Context attachBaseContext(Context context) {
        String spLanguage = (String) SpUtils.get(context, Constants.SP_LANGUAGE,"");
        String spCountry = (String) SpUtils.get(context, Constants.SP_COUNTRY,"");
        if(!TextUtils.isEmpty(spLanguage)&&!TextUtils.isEmpty(spCountry)){
            Locale locale = new Locale(spLanguage, spCountry);
            //这里为8.0以后适配
            return setAppLanguage(context, locale);
        }
        return context;
    }

    /**
     * 判断sp中和app中的多语言信息是否相同
     */
    public static boolean isSameWithSetting(Context context) {
        Locale locale = getAppLocale(context);
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String spLanguage = (String) SpUtils.get(context, Constants.SP_LANGUAGE,"");
        String spCountry = (String) SpUtils.get(context, Constants.SP_COUNTRY,"");
        if (language.equals(spLanguage) && country.equals(spCountry)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存多语言信息到sp中
     */
    public static void saveLanguageSetting(Context context, Locale locale) {
        SpUtils.put(context, Constants.SP_LANGUAGE,locale.getLanguage());
        SpUtils.put(context, Constants.SP_COUNTRY,locale.getCountry());
    }

    /**
     * 获取应用语言
     */
    public static Locale getAppLocale(Context context){
        Locale local;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            local =context.getResources().getConfiguration().getLocales().get(0);
        } else {
            local =context.getResources().getConfiguration().locale;
        }
        return local;
    }

    /**
     * 获取系统语言
     */
    public static LocaleListCompat getSystemLanguage(){
        Configuration configuration = Resources.getSystem().getConfiguration();
        LocaleListCompat locales = ConfigurationCompat.getLocales(configuration);
        return locales;
    }

    //注册Activity生命周期监听回调，此部分一定加上，因为有些版本不加的话多语言切换不回来
    //registerActivityLifecycleCallbacks(callbacks);
    public static  Application.ActivityLifecycleCallbacks callbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            String language = (String) SpUtils.get(activity, Constants.SP_LANGUAGE,"");
            String country = (String) SpUtils.get(activity, Constants.SP_COUNTRY,"");
            LogUtils.e(language);
            if (!TextUtils.isEmpty(language) && !TextUtils.isEmpty(country)) {
                //强制修改应用语言
                if (!isSameWithSetting(activity)) {
                    Locale locale = new Locale(language, country);
                    setAppLanguage(activity,locale);
                }
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

}
