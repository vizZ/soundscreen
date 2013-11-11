package com.arturglier.mobile.android.soundscreen.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class DataContentProvider extends ContentProvider {

    public static final String AUTHORITY = DataContentProvider.class.getName();
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private DataSQLiteOpenHelper mDatabase;

    private static final UriMatcher sTableMatcher = new DataUriMatcher();

    @Override
    public boolean onCreate() {
        mDatabase = new DataSQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
