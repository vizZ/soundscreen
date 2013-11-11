package com.arturglier.mobile.android.soundscreen.data.matchers;

import com.arturglier.mobile.android.soundscreen.common.utils.CommonUriMatcher;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;

public class TracksMatcher implements DataMatcher {
    private static final String PATH_TRACKS = TracksContract.TABLE_NAME;

    @Override
    public void addURIsTo(CommonUriMatcher matcher) {
        matcher.addURI(USERS, PATH_TRACKS);
        matcher.addURI(USERS_ID, PATH_TRACKS, NUMBER);
    }
}
