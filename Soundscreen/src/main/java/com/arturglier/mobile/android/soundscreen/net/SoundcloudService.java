package com.arturglier.mobile.android.soundscreen.net;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.arturglier.mobile.android.soundscreen.BuildConfig;
import com.arturglier.mobile.android.soundscreen.R;
import com.arturglier.mobile.android.soundscreen.common.utils.PreferenceUtils;
import com.arturglier.mobile.android.soundscreen.data.DataContentProvider;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;
import com.arturglier.mobile.android.soundscreen.data.models.Track;
import com.arturglier.mobile.android.soundscreen.data.models.User;
import com.arturglier.mobile.android.soundscreen.data.utils.sql.SQLBuilder;
import com.google.gson.Gson;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Http;
import com.soundcloud.api.Request;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class SoundcloudService extends IntentService implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_REQUEST_TYPE = "request_type";

    public static final int VAL_REQUEST_TYPE_NONE = 0;
    public static final int VAL_REQUEST_TYPE_FAVORITES = 1;
    public static final int VAL_REQUEST_TYPE_WAVEFORMS = 2;

    private static final ApiWrapper sWrapper = new ApiWrapper(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, null, null);
    private static final Gson sGson = new Gson();

    private NotificationsHelper mNotificationsHelper;

    public static void fetchFavorites(Context context) {
        context.startService(getFavoritesIntent(context));
    }

    private static Intent getFavoritesIntent(Context context) {
        Intent intent = new Intent(context, SoundcloudService.class);
        intent.putExtra(KEY_REQUEST_TYPE, VAL_REQUEST_TYPE_FAVORITES);
        return intent;
    }

    public static void fetchWaveforms(Context context) {
        context.startService(getWaveformsIntent(context));
    }

    private static Intent getWaveformsIntent(Context context) {
        Intent intent = new Intent(context, SoundcloudService.class);
        intent.putExtra(KEY_REQUEST_TYPE, VAL_REQUEST_TYPE_WAVEFORMS);
        return intent;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_download))) {
            SoundcloudService.fetchFavorites(this);
        }
    }

    private static class NotificationsHelper {

        private static final int ID_NOTIFICATION_DATA = 0;
        private static final int ID_NOTIFICATION_WAVEFORMS = 1;

        private static final int MAX_WAVEFORMS = 10;

        private final NotificationManager mManger;

        private NotificationCompat.Builder mBuilder;

        private int mProgress;

        public NotificationsHelper(Context context) {
            mManger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(context);
        }

        public void waveformsStart() {
            mProgress = 0;

            mBuilder
                .setContentTitle("Downloading waveforms")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_launcher);

            mBuilder.setProgress(MAX_WAVEFORMS, mProgress, false);
            mManger.notify(ID_NOTIFICATION_WAVEFORMS, mBuilder.build());
        }

        public void waveformsUpdate() {
            mBuilder.setProgress(MAX_WAVEFORMS, ++mProgress, false);
            mManger.notify(ID_NOTIFICATION_WAVEFORMS, mBuilder.build());
        }

        public void waveformsComplete() {
            mBuilder.setContentText("Download complete").setProgress(0, 0, false);
            mManger.notify(ID_NOTIFICATION_WAVEFORMS, mBuilder.build());
        }
    }

    public SoundcloudService() {
        super(SoundcloudService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mNotificationsHelper = new NotificationsHelper(this);

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (PreferenceUtils.downloadPossible(this)) {
            int requestType = intent.getIntExtra(KEY_REQUEST_TYPE, VAL_REQUEST_TYPE_NONE);
            switch (requestType) {
                case VAL_REQUEST_TYPE_FAVORITES:
                    handleFetchFavorites();
                    break;
                case VAL_REQUEST_TYPE_WAVEFORMS:
                    handleFetchWaveforms();
                    break;
                default:
                    break;
            }
        } else {
            reuseAvailableWaveforms();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void reuseAvailableWaveforms() {
        ContentValues values = new ContentValues();
        values.put(TracksContract.USED, SQLBuilder.FALSE);
        getContentResolver().update(TracksContract.used(), values, null, null);
    }

    private void handleFetchWaveforms() {
        getContentResolver().delete(TracksContract.buildWaveformUri(TracksContract.CONTENT_URI), null, null);
        Cursor query = null;
        try {
            query = getContentResolver().query(TracksContract.left(), null, null, null, "RANDOM() LIMIT 10");
            if (query.moveToFirst()) {
                mNotificationsHelper.waveformsStart();
                do {
                    Long _id = query.getLong(query.getColumnIndexOrThrow(TracksContract._ID));
                    String url = query.getString(query.getColumnIndexOrThrow(TracksContract.WAVEFORM_URL));

                    Uri trackUri = TracksContract.buildUri(_id);
                    fetchAndStoreWaveform(trackUri, url);

                    mNotificationsHelper.waveformsUpdate();
                } while (query.moveToNext());
                mNotificationsHelper.waveformsComplete();
            } else {
                SoundcloudService.fetchFavorites(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (query != null) {
                query.close();
            }
        }
    }

    private void handleFetchFavorites() {
        HttpResponse resp = null;
        try {
            resp = sWrapper.get(Request.to("/users/mr-scruff/tracks.json"));
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

                SoundcloudService.fetchWaveforms(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchAndStoreWaveform(Uri uri, String url) throws IOException {
        HttpResponse response = sWrapper.getHttpClient().execute(new HttpGet(url));
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            byte[] fileData = EntityUtils.toByteArray(response.getEntity());
            Log.d("FILE", "File size in bytes: " + fileData.length);

            // Write data to file
            OutputStream fos = null;
            try {
                fos = getContentResolver().openOutputStream(TracksContract.buildWaveformUri(uri));
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

            ContentValues values = new ContentValues();
            values.put(TracksContract.CACHED, SQLBuilder.TRUE);
            getContentResolver().update(uri, values, null, null);

            Log.d("CACHED", "Cached: " + uri);
        }
    }
}
