package com.arturglier.mobile.android.soundscreen.data.contracts;

import android.net.Uri;

public class UsersContract implements DataContract, UsersColumns {
    public static final String MODEL_NAME = "user";
    public static final String TABLE_NAME = "users";

    public static final Uri CONTENT_URI = COMMON_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    public static final String CONTENT_TYPE = COMMON_CONTENT_TYPE + "/" + MODEL_NAME;
    public static final String CONTENT_ITEM_TYPE = COMMON_CONTENT_ITEM_TYPE + "/" + MODEL_NAME;
}
