package com.arturglier.mobile.android.soundscreen.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;
import com.arturglier.mobile.android.soundscreen.data.matchers.TracksMatcher;
import com.arturglier.mobile.android.soundscreen.data.matchers.UsersMatcher;

public class DataContentProvider extends ContentProvider {

    public static final String AUTHORITY = DataContentProvider.class.getName();
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private DataSQLiteOpenHelper mDatabase;

    private static final UriMatcher sUriMatcher = new DataUriMatcher();

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
        SQLiteDatabase db = mDatabase.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case UsersMatcher.USERS:
                return UsersContract.CONTENT_URI.buildUpon().appendPath(
                    String.valueOf(db.insert(UsersContract.TABLE_NAME, null, values))
                ).build();
            case TracksMatcher.TRACKS:
                return TracksContract.CONTENT_URI.buildUpon().appendPath(
                    String.valueOf(db.insert(TracksContract.TABLE_NAME, null, values))
                ).build();
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
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
