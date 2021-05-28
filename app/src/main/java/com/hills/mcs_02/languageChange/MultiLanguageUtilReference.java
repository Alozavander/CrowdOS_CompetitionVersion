package com.hills.mcs_02.languageChange;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public class MultiLanguageUtilReference {
    private static final String TAG = "MultiLanguageUtil_reference";
    private static volatile MultiLanguageUtilReference INSTANCE;

    private MultiLanguageUtilReference() {}

    public static MultiLanguageUtilReference getInstance() {
        if (INSTANCE == null) {
            synchronized (MultiLanguageUtilReference.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MultiLanguageUtilReference();
                }
            }
        }
        return INSTANCE;
    }

    public void setConfiguration(Context context) {
        if (context == null) {
            Log.e(TAG, "No context, MultiLanguageUtil_reference will not work!");
            return;
        }

        Context appContext = context.getApplicationContext();
        Locale preferredLocale = getSysPreferredLocale();
        Log.d(TAG, "Set to preferred locale: " + preferredLocale);
        Configuration configuration = appContext.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(preferredLocale);
        } else {
            configuration.locale = preferredLocale;
        }
        /** Update the language Settings in the context */
        Resources resources = appContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }

    private Locale getSysPreferredLocale() {
        Locale locale;
        /** Get the system default language directly below 7.0 */
        if (Build.VERSION.SDK_INT < 24) {
            locale = Locale.getDefault();
            /** Get the system preferred language above 7.0 */
        } else {
            locale = LocaleList.getDefault().get(0);
    }
        return locale;
    }
}