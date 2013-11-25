package com.arturglier.mobile.android.soundscreen.test.data.utils.cursors;

import android.database.MatrixCursor;

import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;

public class TrackMockCursor extends MatrixCursor {

    public static final String _ID = "7";
    public static final String ID = "123123123";
    public static final String PERMA_LINK = "http://soundcloud.com/some-user/some-sound";
    public static final String URI = "https://api.soundcloud.com/tracks/123123123";

    public static final String TITLE = "some-sound";
    public static final String DURATION = "180000";
    public static final String GENRE = "some-genre";
    public static final String DESCRIPTION = "some-description";
    public static final String USER_ID = "321321";
    public static final String ARTWORK_URL = "https://i1.sndcdn.com/artworks-000061541807-oz3o70-large.jpg?8063923";
    public static final String WAVEFORM_URL = "https://w1.sndcdn.com/someSOMEsome_s.png";
    public static final String PLAYBACK_COUNT = "1";
    public static final String DOWNLOAD_COUNT = "1";
    public static final String FAVORITINGS_COUNT = "1";
    public static final String COMMENT_COUNT = "1";
    public static final String CACHED = "0";
    public static final String USED = "0";

    public TrackMockCursor() {
        super(new String[]{
            TracksContract._ID,
            TracksContract.ID,
            TracksContract.PERMA_LINK,
            TracksContract.URI,
            TracksContract.TITLE,
            TracksContract.DURATION,
            TracksContract.GENRE,
            TracksContract.DESCRIPTION,
            TracksContract.USER_ID,
            TracksContract.ARTWORK_URL,
            TracksContract.WAVEFORM_URL,
            TracksContract.PLAYBACK_COUNT,
            TracksContract.DOWNLOAD_COUNT,
            TracksContract.FAVORITINGS_COUNT,
            TracksContract.COMMENT_COUNT,
            TracksContract.CACHED,
            TracksContract.USED
        });

        addRow(new String[]{
            _ID,
            ID,
            PERMA_LINK,
            URI,
            TITLE,
            DURATION,
            GENRE,
            DESCRIPTION,
            USER_ID,
            ARTWORK_URL,
            WAVEFORM_URL,
            PLAYBACK_COUNT,
            DOWNLOAD_COUNT,
            FAVORITINGS_COUNT,
            COMMENT_COUNT,
            CACHED,
            USED
        });
    }
}
