package com.arturglier.mobile.android.soundscreen.net;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.arturglier.mobile.android.soundscreen.BuildConfig;
import com.arturglier.mobile.android.soundscreen.data.DataContentProvider;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;
import com.arturglier.mobile.android.soundscreen.data.models.Track;
import com.arturglier.mobile.android.soundscreen.data.models.User;
import com.arturglier.mobile.android.soundscreen.data.utils.sql.SQLBuilder;
import com.arturglier.mobile.android.soundscreen.net.receivers.ScheduledUpdateReceiver;
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
import java.util.concurrent.TimeUnit;

public class SoundcloudService extends IntentService {
    public static final String KEY_REQUEST_TYPE = "request_type";

    public static final int VAL_REQUEST_TYPE_NONE = 0;
    public static final int VAL_REQUEST_TYPE_FAVORITES = 1;
    public static final int VAL_REQUEST_TYPE_WAVEFORMS = 2;

    private static final ApiWrapper sWrapper = new ApiWrapper(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, null, null);
    private static final Gson sGson = new Gson();

    // TODO: this is gonna be set by the preferences
    private static final long EXEC_INTERVAL = TimeUnit.MINUTES.toMillis(15);

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

    public static void schedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ScheduledUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), EXEC_INTERVAL, pendingIntent);
    }

    public static void cancel(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ScheduledUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    public SoundcloudService() {
        super(SoundcloudService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
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
    }

    private void handleFetchWaveforms() {
        getContentResolver().delete(TracksContract.buildWaveformUri(TracksContract.CONTENT_URI), null, null);
        Cursor query = null;
        try {
            query = getContentResolver().query(TracksContract.CONTENT_URI, null, null, null, "RANDOM() LIMIT 10");
            if (query.moveToFirst()) {
                do {
                    Long _id = query.getLong(query.getColumnIndexOrThrow(TracksContract._ID));
                    String url = query.getString(query.getColumnIndexOrThrow(TracksContract.WAVEFORM_URL));

                    Uri trackUri = TracksContract.buildUri(_id);
                    fetchAndStoreWaveform(trackUri, url);
                } while (query.moveToNext());
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
