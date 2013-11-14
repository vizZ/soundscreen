package com.arturglier.mobile.android.soundscreen.net;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.arturglier.mobile.android.soundscreen.BuildConfig;
import com.arturglier.mobile.android.soundscreen.data.DataContentProvider;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;
import com.arturglier.mobile.android.soundscreen.data.models.Track;
import com.arturglier.mobile.android.soundscreen.data.models.User;
import com.google.gson.Gson;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Http;
import com.soundcloud.api.Request;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.ArrayList;

public class SoundcloudService extends IntentService {
    public static final String KEY_REQUEST_TYPE = "request_type";

    public static final int VAL_REQUEST_TYPE_NONE = 0;
    public static final int VAL_REQUEST_TYPE_FAVORITES = 1;
    public static final int VAL_REQUEST_TYPE_WAVEFORMS = 2;

    private static final ApiWrapper sWrapper = new ApiWrapper(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, null, null);
    private static final Gson sGson = new Gson();

    public static void fetchFavorites(Context context) {
        Intent intent = new Intent(context, SoundcloudService.class);
        intent.putExtra(KEY_REQUEST_TYPE, VAL_REQUEST_TYPE_FAVORITES);
        context.startService(intent);
    }

    public static void fetchWaveforms(Context context) {
        Intent intent = new Intent(context, SoundcloudService.class);
        intent.putExtra(KEY_REQUEST_TYPE, VAL_REQUEST_TYPE_WAVEFORMS);
        context.startService(intent);
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

    }

    private void handleFetchFavorites() {
        HttpResponse resp = null;
        try {
            resp = sWrapper.get(Request.to("/users/mr-scruff/tracks.json"));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
