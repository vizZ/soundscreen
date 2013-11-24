package com.arturglier.mobile.android.soundscreen.test.data.utils.json;

import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;
import com.arturglier.mobile.android.soundscreen.test.data.utils.cursors.UserMockCursor;

import org.json.JSONException;
import org.json.JSONObject;

public class UserMockJSON extends JSONObject {

    public UserMockJSON() throws JSONException {
        super();

        put(UsersContract.ID, UserMockCursor.ID);
        put(UsersContract.PERMA_LINK, UserMockCursor.PERMA_LINK);
        put(UsersContract.URI, UserMockCursor.URI);
        put(UsersContract.USERNAME, UserMockCursor.USERNAME);
        put(UsersContract.AVATAR_URL, UserMockCursor.AVATAR_URL);
    }
}
