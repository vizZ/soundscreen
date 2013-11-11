package com.arturglier.mobile.android.soundscreen.common.utils;

import android.content.UriMatcher;

public abstract class CommonUriMatcher extends UriMatcher {
    private static final String SEPARATOR = "/";

    private final String mAuthority;

    public CommonUriMatcher(String authority) {
        super(UriMatcher.NO_MATCH);

        mAuthority = authority;
    }

    public void addURI(int id, String... paths) {
        StringBuilder builder = new StringBuilder();
        for (String path : paths) {
            builder.append(SEPARATOR);
            builder.append(path);
        }
        this.addURI(mAuthority, builder.toString(), id);
    }
}
