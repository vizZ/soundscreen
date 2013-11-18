package com.arturglier.mobile.android.soundscreen.data.utils.sql;

public class SQLBuilder {

    public static String TRUE = "TRUE";
    public static String FALSE = "FALSE";


    public SQLTable table(String name) {
        return new SQLTable(name);
    }
}
