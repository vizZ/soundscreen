package com.arturglier.mobile.android.soundscreen.net;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.arturglier.mobile.android.soundscreen.BuildConfig;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Http;
import com.soundcloud.api.Request;

import org.apache.http.HttpResponse;

import java.io.IOException;

public class SoundcloudService extends IntentService {
    public static final String KEY_REQUEST_TYPE = "request_type";

    public static final int VAL_REQUEST_TYPE_NONE = 0;
    public static final int VAL_REQUEST_TYPE_FAVORITES = 1;
    public static final int VAL_REQUEST_TYPE_WAVEFORMS = 2;

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
        ApiWrapper wrapper = new ApiWrapper(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, null, null);
        HttpResponse resp = null;
        try {
            resp = wrapper.get(Request.to("/users/mr-scruff/tracks.json"));

            Log.d("REQUEST_FAVORITES", Http.formatJSON(Http.getString(resp)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
