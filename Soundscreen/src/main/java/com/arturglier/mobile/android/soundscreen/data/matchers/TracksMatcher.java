package com.arturglier.mobile.android.soundscreen.data.matchers;

import com.arturglier.mobile.android.soundscreen.common.utils.CommonUriMatcher;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;

public class TracksMatcher implements CommonMatcher {
    public static final String PATH_TRACKS = TracksContract.TABLE_NAME;
    public static final String PATH_WAVEFORMS = "waveforms";
    public static final String PATH_ARTWORKS = "artworks";

    @Override
    public void addURIsTo(CommonUriMatcher matcher) {
        matcher.addURI(TRACKS, PATH_TRACKS);
        matcher.addURI(TRACKS_ID, PATH_TRACKS, NUMBER);
        matcher.addURI(TRACKS_WAVEFORMS, PATH_TRACKS, PATH_WAVEFORMS);
        matcher.addURI(TRACKS_ID_WAVEFORMS, PATH_TRACKS, NUMBER, PATH_WAVEFORMS);
        matcher.addURI(TRACKS_ARTWORKS, PATH_TRACKS, PATH_ARTWORKS);
        matcher.addURI(TRACKS_ID_ARTWORKS, PATH_TRACKS, NUMBER, PATH_ARTWORKS);
    }
}
