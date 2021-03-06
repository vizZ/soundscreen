package com.arturglier.mobile.android.soundscreen.net.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.arturglier.mobile.android.soundscreen.BuildConfig;
import com.arturglier.mobile.android.soundscreen.R;
import com.arturglier.mobile.android.soundscreen.common.utils.PreferenceUtils;
import com.arturglier.mobile.android.soundscreen.data.DataContentProvider;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;
import com.arturglier.mobile.android.soundscreen.data.models.Track;
import com.arturglier.mobile.android.soundscreen.data.models.User;
import com.arturglier.mobile.android.soundscreen.net.receivers.ScheduledUpdateReceiver;
import com.google.gson.Gson;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Http;
import com.soundcloud.api.Request;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;

public class SyncService extends IntentService implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String URL = "/users/mr-scruff/tracks.json";

    private static long EXEC_INTERVAL = 60 * 60 * 1000;

    private static final ApiWrapper sWrapper = new ApiWrapper(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, null, null);
    private static final Gson sGson = new Gson();

    public static void start(Context context) {
        context.startService(new Intent(context, SyncService.class));
    }

    public static void restart(Context context) {
        SyncService.stop(context);
        SyncService.startAndRepeat(context);
    }

    public static void startAndRepeat(Context context) {
        SyncService.start(context);

        PendingIntent pendingIntent = SyncService.getPendingIntent(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), PreferenceUtils.getSyncInterval(context), pendingIntent);
    }

    public static void stop(Context context) {
        PendingIntent pendingIntent = getPendingIntent(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, ScheduledUpdateReceiver.class);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_download))) {
            SyncService.start(this);
        } else if(key.equals(getString(R.string.pref_sync))) {
            SyncService.restart(this);
        }
    }

    public SyncService() {
        super(SyncService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (PreferenceUtils.downloadPossible(this)) {
            HttpResponse resp = null;
            try {
                resp = sWrapper.get(Request.to(URL));
                if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String content = Http.getString(resp);
                    Log.d("RESPONSE", Http.formatJSON(content));

                    Track[] tracks = sGson.fromJson(content, Track[].class);
                    Log.d("GSON", "Number of Tracks: " + tracks.length);

                    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                    ops.add(ContentProviderOperation.newDelete(UsersContract.CONTENT_URI).build());
                    ops.add(ContentProviderOperation.newDelete(TracksContract.CONTENT_URI).build());

                    for (Track track : tracks) {
                        User user = track.getUser();

                        ops.add(
                            ContentProviderOperation
                                .newInsert(UsersContract.CONTENT_URI)
                                .withValues(user.toContentValues())
                                .build()
                        );

                        ops.add(
                            ContentProviderOperation
                                .newInsert(TracksContract.CONTENT_URI)
                                .withValues(track.toContentValues())
                                .build()
                        );
                    }

                    try {
                        getContentResolver().applyBatch(DataContentProvider.AUTHORITY, ops);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (OperationApplicationException e) {
                        e.printStackTrace();
                    }

                    FileService.start(this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
