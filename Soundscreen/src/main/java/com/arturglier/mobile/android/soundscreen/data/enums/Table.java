package com.arturglier.mobile.android.soundscreen.data.enums;

import com.arturglier.mobile.android.soundscreen.data.DataContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;

public enum Table {
    USERS(UsersContract.class),
    TRACKS(TracksContract.class);

    private final DataContract contract;

    Table(Class<? extends DataContract> clazz) throws IllegalAccessException, InstantiationException {
        this.contract = clazz.newInstance();
    }
}
