package com.arturglier.mobile.android.soundscreen.data.contracts;

public interface TracksColumns extends CommonColumns {
    String TITLE = "title";
    String DURATION = "duration";
    String GENRE = "genre";
    String DESCRIPTION = "description";

    String USER_ID = "user_id";

    String ARTWORK_URL = "artwork_url";
    String WAVEFORM_URL = "waveform_url";

    String PLAYBACK_COUNT = "playback_count";
    String DOWNLOAD_COUNT = "download_count";
    String FAVORITINGS_COUNT = "favoritings_count";
    String COMMENT_COUNT = "comment_count";

    String CACHED = "cached";
    String USED = "used";
}
