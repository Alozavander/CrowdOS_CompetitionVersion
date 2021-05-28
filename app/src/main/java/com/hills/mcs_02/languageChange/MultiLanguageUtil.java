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

import java.util.Locale;

import com.hills.mcs_02.LogUtils;

public class MultiLanguageUtil {
    private static final String TAG = "MultiLanguageUtil_reference";
    public static void changeLanguage(Context context,String language, String area) {
        if (TextUtils.isEmpty(language) && TextUtils.isEmpty(area)) {
            /** If the language and locale are empty, follow the system */
            SpUtils.put(context, Constants.SP_LANGUAGE,"");
            SpUtils.put(context, Constants.SP_COUNTRY,"");
        } else {
            /** If it is not empty, change the app language */
            Locale newLocale = new Locale(language, area);
            setAppLanguage(context,newLocale);
            saveLanguageSetting(context, newLocale);
        }
    }

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

    public static Context attachBaseContext(Context context) {
        String spLanguage = (String) SpUtils.get(context, Constants.SP_LANGUAGE,"");
        String spCountry = (String) SpUtils.get(context, Constants.SP_COUNTRY,"");
        if(!TextUtils.isEmpty(spLanguage)&&!TextUtils.isEmpty(spCountry)){
            Locale locale = new Locale(spLanguage, spCountry);
           /**  This is for post-8.0 adaptations */
            return setAppLanguage(context, locale);
        }
        return context;
    }

   /**  Determine whether the multilingual information in SharedPreference and APP is the same */
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

   /** Save multilingual information to SharedPreference */
    public static void saveLanguageSetting(Context context, Locale locale) {
        SpUtils.put(context, Constants.SP_LANGUAGE,locale.getLanguage());
        SpUtils.put(context, Constants.SP_COUNTRY,locale.getCountry());
    }

   /** Get the application language */
    public static Locale getAppLocale(Context context){
        Locale local;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            local =context.getResources().getConfiguration().getLocales().get(0);
        } else {
            local =context.getResources().getConfiguration().locale;
        }
        return local;
    }

   /** Get the system language */
    public static LocaleListCompat getSystemLanguage(){
        Configuration configuration = Resources.getSystem().getConfiguration();
        LocaleListCompat locales = ConfigurationCompat.getLocales(configuration);
        return locales;
    }

  /** Register the Activity lifecycle to listen for callbacks */
    public static  Application.ActivityLifecycleCallbacks callbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            String language = (String) SpUtils.get(activity, Constants.SP_LANGUAGE,"");
            String country = (String) SpUtils.get(activity, Constants.SP_COUNTRY,"");
            LogUtils.e(language);
            if (!TextUtils.isEmpty(language) && !TextUtils.isEmpty(country)) {
                /** Force changes to the application language */
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
