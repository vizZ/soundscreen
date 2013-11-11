package com.arturglier.mobile.android.soundscreen.data.helpers;

import android.database.sqlite.SQLiteDatabase;

public class UsersHelper implements DataHelper {
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " does not support this operation");
    }
}
