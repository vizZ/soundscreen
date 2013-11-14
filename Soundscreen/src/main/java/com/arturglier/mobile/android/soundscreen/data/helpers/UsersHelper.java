package com.arturglier.mobile.android.soundscreen.data.helpers;

import android.database.sqlite.SQLiteDatabase;

import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;

public class UsersHelper implements DataHelper {
    private static final String CREATE_TABLE_USERS =
        SQL.table(UsersContract.TABLE_NAME)
            .column(UsersContract._ID).asIntegerPrimaryKey()
            .column(UsersContract.ID).asIntegerNotNullUniqueOnConflictReplace()
            .column(UsersContract.URI).asText()
            .column(UsersContract.PERMA_LINK).asText()
            .column(UsersContract.USERNAME).asText()
            .column(UsersContract.AVATAR_URL).asText()
            .create();

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == 1) {
            db.execSQL("DROP TABLE IF EXISTS " + UsersContract.TABLE_NAME);
            db.execSQL(CREATE_TABLE_USERS);
        }
    }
}
