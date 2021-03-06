package com.arturglier.mobile.android.soundscreen.data.matchers;

import com.arturglier.mobile.android.soundscreen.common.utils.CommonUriMatcher;

public interface CommonMatcher {
    public static final String NUMBER = "#";
    public static final String WILDCARD = "*";
    public static final String SEPARATOR = "/";

    public static final int USERS = 100;
    public static final int USERS_ID = 101;

    public static final int TRACKS = 200;
    public static final int TRACKS_ID = 201;
    public static final int TRACKS_WAVEFORMS = 202;
    public static final int TRACKS_ID_WAVEFORMS = 203;
    public static final int TRACKS_ARTWORKS = 204;
    public static final int TRACKS_ID_ARTWORKS = 205;

    public void addURIsTo(CommonUriMatcher matcher);
}
