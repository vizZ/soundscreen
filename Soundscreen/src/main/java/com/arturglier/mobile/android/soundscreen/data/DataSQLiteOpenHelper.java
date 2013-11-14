package com.arturglier.mobile.android.soundscreen.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arturglier.mobile.android.soundscreen.data.enums.Table;

public class DataSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "soundscreen.db";
    private static final int DB_VERSION = 2;

    public DataSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Table table : Table.values()) {
            table.helper.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Table table : Table.values()) {
            table.helper.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
