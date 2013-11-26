package com.arturglier.mobile.android.soundscreen.net.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.arturglier.mobile.android.soundscreen.BuildConfig;
import com.arturglier.mobile.android.soundscreen.R;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.utils.sql.SQLBuilder;
import com.soundcloud.api.ApiWrapper;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;

public class FileService extends IntentService {

    public static void start(Context context) {
        context.startService(new Intent(context, SyncService.class));
    }

    private static final ApiWrapper sWrapper = new ApiWrapper(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, null, null);

    private NotificationsHelper mNotificationsHelper;

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

    public FileService() {
        super(FileService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mNotificationsHelper = new NotificationsHelper(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Messenger messenger = intent.getParcelableExtra("MESSENGER");

        getContentResolver().delete(TracksContract.buildWaveformUri(TracksContract.CONTENT_URI), null, null);
        Cursor query = null;
        try {
            query = getContentResolver().query(TracksContract.left(), null, null, null, "RANDOM() LIMIT 10");
            if (query.moveToFirst()) {
//                mNotificationsHelper.waveformsStart();
                do {
                    Long _id = query.getLong(query.getColumnIndexOrThrow(TracksContract._ID));
                    String url = query.getString(query.getColumnIndexOrThrow(TracksContract.WAVEFORM_URL));

                    Uri trackUri = TracksContract.buildUri(_id);
                    fetchAndStoreWaveform(trackUri, url);

//                    mNotificationsHelper.waveformsUpdate();
                } while (query.moveToNext());
//                mNotificationsHelper.waveformsComplete();

                if (messenger != null) {
                    Message message = Message.obtain();
                    message.arg1 = 0;
                    try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                SyncService.start(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (query != null) {
                query.close();
            }
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
