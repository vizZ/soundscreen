package com.arturglier.mobile.android.soundscreen.net.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.arturglier.mobile.android.soundscreen.net.services.SyncService;

public class ScheduledUpdateReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SyncService.start(context);
    }
}
