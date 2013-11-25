package com.arturglier.mobile.android.soundscreen.data.enums;

import com.arturglier.mobile.android.soundscreen.data.contracts.CommonContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;
import com.arturglier.mobile.android.soundscreen.data.helpers.DataHelper;
import com.arturglier.mobile.android.soundscreen.data.helpers.TracksHelper;
import com.arturglier.mobile.android.soundscreen.data.helpers.UsersHelper;
import com.arturglier.mobile.android.soundscreen.data.matchers.CommonMatcher;
import com.arturglier.mobile.android.soundscreen.data.matchers.TracksMatcher;
import com.arturglier.mobile.android.soundscreen.data.matchers.UsersMatcher;

public enum Table {
    USERS(UsersContract.class, UsersMatcher.class, UsersHelper.class),
    TRACKS(TracksContract.class, TracksMatcher.class, TracksHelper.class);

    public final CommonContract contract;
    public final CommonMatcher matcher;
    public final DataHelper helper;

    Table(Class<? extends CommonContract> contractClass, Class<? extends CommonMatcher> matcherClass, Class<? extends DataHelper> helperClass) throws ExceptionInInitializerError {
        try {
            this.contract = contractClass.newInstance();
            this.matcher = matcherClass.newInstance();
            this.helper = helperClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e.getMessage());
        }
    }
}
