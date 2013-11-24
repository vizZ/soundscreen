package com.arturglier.mobile.android.soundscreen.test.data.utils.cursors;

import android.database.MatrixCursor;

import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;

public class UserMockCursor extends MatrixCursor {

    public static final String _ID = "7";
    public static final String ID = "321321";
    public static final String PERMA_LINK = "some-user";
    public static final String URI = "https://api.soundcloud.com/users/321321";

    public static final String USERNAME = "some-user";
    public static final String AVATAR_URL = "https://i1.sndcdn.com/avatars-000012341234-1234mm-large.jpg?1asdf23";

    public UserMockCursor() {
        super(new String[]{
            UsersContract._ID,
            UsersContract.ID,
            UsersContract.PERMA_LINK,
            UsersContract.URI,
            UsersContract.USERNAME,
            UsersContract.AVATAR_URL
        });
    }
}
