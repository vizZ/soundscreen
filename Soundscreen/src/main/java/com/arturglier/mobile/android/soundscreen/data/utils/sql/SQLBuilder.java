package com.arturglier.mobile.android.soundscreen.data.utils.sql;

public class SQLBuilder {

    public static String TRUE = "1";
    public static String FALSE = "0 ";


    public SQLTable table(String name) {
        return new SQLTable(name);
    }
}
