package com.arturglier.mobile.android.soundscreen.data.contracts;

import android.net.Uri;

import com.arturglier.mobile.android.soundscreen.data.matchers.TracksMatcher;
import com.arturglier.mobile.android.soundscreen.data.utils.sql.SQLBuilder;

public class TracksContract implements DataContract, TracksColumns {
    public static final String MODEL_NAME = "track";
    public static final String TABLE_NAME = "tracks";

    public static final Uri CONTENT_URI = COMMON_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    public static final String CONTENT_TYPE = COMMON_CONTENT_TYPE + "/" + MODEL_NAME;
    public static final String CONTENT_ITEM_TYPE = COMMON_CONTENT_ITEM_TYPE + "/" + MODEL_NAME;

    public static Uri buildUri(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        } else {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }
    }

    public static Uri buildWaveformUri(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        } else {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).appendPath(TracksMatcher.PATH_WAVEFORMS).build();
        }
    }

    public static Uri buildWaveformUri(Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException();
        } else {
            return uri.buildUpon().appendPath(TracksMatcher.PATH_WAVEFORMS).build();
        }
    }

    public static Uri cached() {
        return CONTENT_URI.buildUpon().appendQueryParameter(TracksContract.CACHED, SQLBuilder.TRUE).build();
    }

    public static Uri scheduled() {
        return TracksContract.cached().buildUpon().appendQueryParameter(TracksContract.USED, SQLBuilder.FALSE).build();
    }
}
