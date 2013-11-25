package com.arturglier.mobile.android.soundscreen.net.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.arturglier.mobile.android.soundscreen.net.SoundcloudService;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SoundcloudService.fetchFavorites(context);
    }
}
