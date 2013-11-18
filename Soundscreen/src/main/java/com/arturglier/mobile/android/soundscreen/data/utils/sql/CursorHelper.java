package com.arturglier.mobile.android.soundscreen.data.utils.sql;

import android.database.Cursor;

public class CursorHelper {

    private final Cursor cursor;

    public CursorHelper(Cursor cursor) {
        this.cursor = cursor;
    }

    public String getString(String column) {
        return this.cursor.getString(this.cursor.getColumnIndexOrThrow(column));
    }

    public int getInt(String column) {
        return this.cursor.getInt(this.cursor.getColumnIndexOrThrow(column));
    }

    public long getLong(String column) {
        return this.cursor.getLong(this.cursor.getColumnIndexOrThrow(column));
    }
}
