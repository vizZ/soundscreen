package com.arturglier.mobile.android.soundscreen.data;

import com.arturglier.mobile.android.soundscreen.common.utils.CommonUriMatcher;
import com.arturglier.mobile.android.soundscreen.data.DataProvider;
import com.arturglier.mobile.android.soundscreen.data.enums.Table;

public class DataUriMatcher extends CommonUriMatcher {

    public DataUriMatcher() {
        super(DataProvider.AUTHORITY);

        for (Table table : Table.values()) {
            table.matcher.addURIsTo(this);
        }
    }
}
