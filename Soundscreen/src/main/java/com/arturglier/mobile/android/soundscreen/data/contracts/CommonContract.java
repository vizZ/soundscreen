package com.arturglier.mobile.android.soundscreen.data.contracts;

import android.net.Uri;

import com.arturglier.mobile.android.soundscreen.data.DataContentProvider;

public abstract class CommonContract implements CommonColumns {
    protected final static Uri COMMON_CONTENT_URI = DataContentProvider.CONTENT_URI;

    protected final static String COMMON_CONTENT_TYPE = "vnd.arturglier.mobile.android.soundscreen.cursor.dir";
    protected final static String COMMON_CONTENT_ITEM_TYPE = "vnd.arturglier.mobile.android.soundscreen.cursor.item";
}
