package com.arturglier.mobile.android.soundscreen.data.matchers;

import com.arturglier.mobile.android.soundscreen.common.utils.CommonUriMatcher;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;

public class UsersMatcher implements DataMatcher {
    private static final String PATH_USERS = UsersContract.TABLE_NAME;

    @Override
    public void addURIsTo(CommonUriMatcher matcher) {
        matcher.addURI(USERS, PATH_USERS);
        matcher.addURI(USERS_ID, PATH_USERS, NUMBER);
    }
}
