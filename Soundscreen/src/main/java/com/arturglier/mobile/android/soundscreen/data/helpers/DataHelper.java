package com.arturglier.mobile.android.soundscreen.data.helpers;

import android.database.sqlite.SQLiteDatabase;

import com.arturglier.mobile.android.soundscreen.data.utils.sql.SQLBuilder;

public interface DataHelper {
    public void onCreate(SQLiteDatabase db);

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    SQLBuilder SQL = new SQLBuilder();
}
