package com.arturglier.mobile.android.soundscreen.data.utils.sql;

public class SQLBuilder {

    public SQLTable table(String name) {
        return new SQLTable(name);
    }
}
