package com.arturglier.mobile.android.soundscreen.data.enums;

import com.arturglier.mobile.android.soundscreen.data.DataContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;
import com.arturglier.mobile.android.soundscreen.data.matchers.DataMatcher;
import com.arturglier.mobile.android.soundscreen.data.matchers.TracksMatcher;
import com.arturglier.mobile.android.soundscreen.data.matchers.UsersMatcher;

public enum Table {
    USERS(UsersContract.class, UsersMatcher.class),
    TRACKS(TracksContract.class, TracksMatcher.class);

    public final DataContract contract;
    public final DataMatcher matcher;

    Table(Class<? extends DataContract> contractClass, Class<? extends DataMatcher> matcherClass) throws IllegalAccessException, InstantiationException {
        this.contract = contractClass.newInstance();
        this.matcher = matcherClass.newInstance();
    }
}
