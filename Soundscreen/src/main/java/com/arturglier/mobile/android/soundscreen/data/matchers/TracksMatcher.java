package com.arturglier.mobile.android.soundscreen.data.matchers;

import com.arturglier.mobile.android.soundscreen.common.utils.CommonUriMatcher;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;

public class TracksMatcher implements DataMatcher {
    public static final String PATH_TRACKS = TracksContract.TABLE_NAME;
    public static final String PATH_WAVEFORM = "waveform";

    @Override
    public void addURIsTo(CommonUriMatcher matcher) {
        matcher.addURI(TRACKS, PATH_TRACKS);
        matcher.addURI(TRACKS_ID, PATH_TRACKS, NUMBER);
        matcher.addURI(TRACKS_ID_WAVEFORM, PATH_TRACKS, NUMBER, PATH_WAVEFORM);
    }
}
