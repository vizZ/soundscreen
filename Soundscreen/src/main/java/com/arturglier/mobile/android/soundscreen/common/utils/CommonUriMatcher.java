package com.arturglier.mobile.android.soundscreen.common.utils;

import android.content.UriMatcher;
import android.text.TextUtils;

public abstract class CommonUriMatcher extends UriMatcher {
    private static final String SEPARATOR = "/";

    private final String mAuthority;

    public CommonUriMatcher(String authority) {
        super(UriMatcher.NO_MATCH);

        mAuthority = authority;
    }

    public void addURI(int id, String... paths) {
        this.addURI(mAuthority, TextUtils.join(SEPARATOR, paths), id);
    }
}
