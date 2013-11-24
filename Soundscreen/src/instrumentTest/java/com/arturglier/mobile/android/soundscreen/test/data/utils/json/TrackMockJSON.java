package com.arturglier.mobile.android.soundscreen.test.data.utils.json;

import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.test.data.utils.cursors.TrackMockCursor;

import org.json.JSONException;
import org.json.JSONObject;

public class TrackMockJSON extends JSONObject {

    public TrackMockJSON() throws JSONException {
        super();

        put(TracksContract.ID, TrackMockCursor.ID);
        put(TracksContract.PERMA_LINK, TrackMockCursor.PERMA_LINK);
        put(TracksContract.URI, TrackMockCursor.URI);
        put(TracksContract.TITLE, TrackMockCursor.TITLE);
        put(TracksContract.DURATION, TrackMockCursor.DURATION);
        put(TracksContract.GENRE, TrackMockCursor.GENRE);
        put(TracksContract.DESCRIPTION, TrackMockCursor.DESCRIPTION);
        put(TracksContract.USER_ID, TrackMockCursor.USER_ID);
        put(TracksContract.WAVEFORM_URL, TrackMockCursor.WAVEFORM_URL);
        put(TracksContract.PLAYBACK_COUNT, TrackMockCursor.PLAYBACK_COUNT);
        put(TracksContract.DOWNLOAD_COUNT, TrackMockCursor.DOWNLOAD_COUNT);
        put(TracksContract.FAVORITINGS_COUNT, TrackMockCursor.FAVORITINGS_COUNT);
        put(TracksContract.COMMENT_COUNT, TrackMockCursor.COMMENT_COUNT);
        put(TracksContract.CACHED, TrackMockCursor.CACHED);
        put(TracksContract.USED, TrackMockCursor.USED);
    }
}
