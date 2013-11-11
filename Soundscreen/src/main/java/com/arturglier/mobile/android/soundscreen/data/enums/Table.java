package com.arturglier.mobile.android.soundscreen.data.enums;

import com.arturglier.mobile.android.soundscreen.data.DataContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.Tracks;
import com.arturglier.mobile.android.soundscreen.data.contracts.Users;

public enum Table {
    USERS(Users.class),
    TRACKS(Tracks.class);

    private final DataContract contract;

    Table(Class<? extends DataContract> clazz) throws IllegalAccessException, InstantiationException {
        this.contract = clazz.newInstance();
    }
}
