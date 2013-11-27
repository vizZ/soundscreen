package com.arturglier.mobile.android.soundscreen.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.arturglier.mobile.android.soundscreen.R;

public class PreferenceUtils {

    public static boolean downloadPossible(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Integer downloadConf = Integer.valueOf(prefs.getString(context.getString(R.string.pref_download), context.getString(R.string.download_entry_mobile_and_wifi)));

        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        boolean connected = networkInfo.isConnected();

        if (connected) {
            switch (downloadConf) {
                case 0:
                    return false;
                case 1:
                    return true;
                case 2:
                    int type = networkInfo.getType();
                    if (type == ConnectivityManager.TYPE_MOBILE) {
                        return false;
                    } else {
                        return true;
                    }
                default:
                    throw new IllegalArgumentException("Unknown download setting code: " + downloadConf);
            }
        } else {
            return false;
        }
    }
}
