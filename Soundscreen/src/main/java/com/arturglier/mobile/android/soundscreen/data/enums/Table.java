package com.arturglier.mobile.android.soundscreen.data.enums;

import com.arturglier.mobile.android.soundscreen.data.DataContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.Tracks;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;

public enum Table {
    TRACKS(Tracks.class);
    USERS(UsersContract.class),

    private final DataContract contract;

    Table(Class<? extends DataContract> clazz) throws IllegalAccessException, InstantiationException {
        this.contract = clazz.newInstance();
    }
}
