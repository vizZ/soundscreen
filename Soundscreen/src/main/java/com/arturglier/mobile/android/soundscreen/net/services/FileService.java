package com.arturglier.mobile.android.soundscreen.net.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.arturglier.mobile.android.soundscreen.BuildConfig;
import com.arturglier.mobile.android.soundscreen.R;
import com.arturglier.mobile.android.soundscreen.common.utils.PreferenceUtils;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.models.Track;
import com.arturglier.mobile.android.soundscreen.data.utils.sql.SQLBuilder;
import com.soundcloud.api.ApiWrapper;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;

public class FileService extends IntentService implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String ACTION_FILE_AVAILABLE = "com.arturglier.mobile.android.soundscreen.net.services.ACTION_FILE_AVAILABLE";
    private long mCacheSize = 10;

    public static void start(Context context) {
        context.startService(new Intent(context, FileService.class));
    }

    private static final ApiWrapper sWrapper = new ApiWrapper(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, null, null);

    private NotificationsHelper mNotificationsHelper;

    private static class NotificationsHelper {

        private static final int ID_NOTIFICATION_DATA = 0;
        private static final int ID_NOTIFICATION_WAVEFORMS = 1;

        private static final int MAX_WAVEFORMS = 10;

        private final Context mContext;
        private final NotificationManager mManger;
        private NotificationCompat.Builder mBuilder;

        private int mProgress;
        private int mCount;

        public NotificationsHelper(Context context) {
            mContext = context;
            mManger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(context);
        }

        public void waveformsStart(int count) {
            mProgress = 0;
            mCount = count;

            mBuilder
                .setContentTitle("Downloading waveforms")
                .setContentText("Download in progress")
                .setTicker("Downloading new waveforms")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_action_refresh)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher))
                .setProgress(mCount, mProgress, false)
                .setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(), 0));

            mManger.notify(ID_NOTIFICATION_WAVEFORMS, mBuilder.build());
        }

        public void waveformsUpdate() {
            mBuilder
                .setProgress(MAX_WAVEFORMS, ++mProgress, false)
                .setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(), 0));

            mManger.notify(ID_NOTIFICATION_WAVEFORMS, mBuilder.build());
        }

        public void waveformsComplete() {
            mBuilder
                .setContentText("Download complete").setProgress(0, 0, false)
                .setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(), 0))
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.ic_launcher);

            mManger.notify(ID_NOTIFICATION_WAVEFORMS, mBuilder.build());
        }
    }

    public FileService() {
        super(FileService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this);

        mNotificationsHelper = new NotificationsHelper(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_cache))) {
            mCacheSize = sharedPreferences.getLong(key, 10);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (PreferenceUtils.downloadPossible(this)) {
            Cursor query = null;
            try {
                // TODO: handle case when having cached all items, but their number is less than cache size => no download needed anyway
                query = getContentResolver().query(TracksContract.left(), null, null, null, "RANDOM() LIMIT " + mCacheSize);
                if (query.moveToFirst()) {
                    getContentResolver().delete(TracksContract.buildArtworksUri(TracksContract.CONTENT_URI), null, null);
                    getContentResolver().delete(TracksContract.buildWaveformUri(TracksContract.CONTENT_URI), null, null);

                    mNotificationsHelper.waveformsStart(query.getCount());
                    boolean first = true;
                    do {
                        Track track = new Track(query);

                        fetchAndStoreFile(TracksContract.buildArtworksUri(track.getLocalId()), track.getArtworkUrl());
                        fetchAndStoreFile(TracksContract.buildWaveformUri(track.getLocalId()), track.getWaveformUrl());

                        ContentValues values = new ContentValues();
                        values.put(TracksContract.CACHED, SQLBuilder.TRUE);
                        getContentResolver().update(TracksContract.buildUri(track.getLocalId()), values, null, null);

                        mNotificationsHelper.waveformsUpdate();

                        if (first) {
                            notifyEngine();
                            first = false;
                        }
                    } while (query.moveToNext());
                    mNotificationsHelper.waveformsComplete();
                } else {
                    resetAll();
                    notifyEngine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (query != null) {
                    query.close();
                }
            }
        } else {
            resetScheduled();
            notifyEngine();
        }
    }

    private void resetScheduled() {
        ContentValues values = new ContentValues();
        values.put(TracksContract.USED, SQLBuilder.FALSE);
        getContentResolver().update(TracksContract.used(), values, null, null);
    }

    private void resetAll() {
        ContentValues values = new ContentValues();
        values.put(TracksContract.CACHED, SQLBuilder.FALSE);
        values.put(TracksContract.USED, SQLBuilder.FALSE);
        getContentResolver().update(TracksContract.CONTENT_URI, values, null, null);
    }

    private void notifyEngine() {
        Intent intent = new Intent(ACTION_FILE_AVAILABLE);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void fetchAndStoreFile(Uri uri, String url) throws IOException {
        HttpResponse response = sWrapper.getHttpClient().execute(new HttpGet(url));
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            byte[] fileData = EntityUtils.toByteArray(response.getEntity());
            Log.d("FILE", "File size in bytes: " + fileData.length);

            // Write data to file
            OutputStream fos = null;
            try {
                fos = getContentResolver().openOutputStream(uri);
                fos.write(fileData);
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: add some better handling?
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }

            Log.d("CACHED", "Cached: " + uri);
        }
    }
}
