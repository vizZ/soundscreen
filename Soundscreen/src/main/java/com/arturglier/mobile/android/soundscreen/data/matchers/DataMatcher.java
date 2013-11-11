package com.arturglier.mobile.android.soundscreen.data.matchers;

import com.arturglier.mobile.android.soundscreen.common.utils.CommonUriMatcher;

public interface DataMatcher {
    public static final String NUMBER = "#";
    public static final String WILDCARD = "*";
    public static final String SEPARATOR = "/";

    public static final int USERS = 100;
    public static final int USERS_ID = 101;

    public static final int TRACKS = 200;
    public static final int TRACKS_ID = 201;

    public void addURIsTo(CommonUriMatcher matcher);
}
