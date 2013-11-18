package com.arturglier.mobile.android.soundscreen.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;
import com.arturglier.mobile.android.soundscreen.data.matchers.TracksMatcher;
import com.arturglier.mobile.android.soundscreen.data.matchers.UsersMatcher;
import com.arturglier.mobile.android.soundscreen.data.utils.sql.SelectionBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class DataContentProvider extends ContentProvider {

    public static final String AUTHORITY = DataContentProvider.class.getName();
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private DataSQLiteOpenHelper mDatabase;

    private static final UriMatcher sUriMatcher = new DataUriMatcher();

    private static final HashMap<String, String> MIME_TYPES = new HashMap<String, String>();

    static {
        MIME_TYPES.put(".png", "image/png");
        MIME_TYPES.put(".jpeg", "image/jpeg");
        MIME_TYPES.put(".jpg", "image/jpg");
    }

    @Override
    public boolean onCreate() {
        mDatabase = new DataSQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDatabase.getWritableDatabase();

        SelectionBuilder builder = new SelectionBuilder();

        switch (sUriMatcher.match(uri)) {
            case TracksMatcher.TRACKS_ID:
                builder.where(TracksContract._ID + "=?", uri.getLastPathSegment());
            case TracksMatcher.TRACKS:
                String cached = uri.getQueryParameter(TracksContract.CACHED);
                if (cached != null) {
                    builder.where(TracksContract.CACHED + "=?", cached);
                }
                String used = uri.getQueryParameter(TracksContract.USED);
                if (used != null) {
                    builder.where(TracksContract.USED + "=?", used);
                }
                builder.table(TracksContract.TABLE_NAME);
                return builder.query(db, projection, sortOrder);
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case TracksMatcher.TRACKS_ID_WAVEFORMS:
                for (String extension : MIME_TYPES.keySet()) {
                    if (uri.toString().endsWith(extension)) {
                        return (MIME_TYPES.get(extension));
                    } else {
                        throw new UnsupportedOperationException("Unsupported MIME TYPE: " + uri);
                    }
                }
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
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
        SQLiteDatabase db = mDatabase.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case UsersMatcher.USERS:
                return db.delete(UsersContract.TABLE_NAME, null, null);
            case TracksMatcher.TRACKS:
                return db.delete(TracksContract.TABLE_NAME, null, null);
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        switch (sUriMatcher.match(uri)) {
            case TracksMatcher.TRACKS_ID_WAVEFORMS:
                int imode = 0;

                if (!getContext().getFilesDir().exists()) {
                    getContext().getFilesDir().mkdirs();
                }

                File file = getFile(getContext().getFilesDir(), uri, mode);

                if (mode.contains("w")) imode |= ParcelFileDescriptor.MODE_WRITE_ONLY;
                if (mode.contains("r")) imode |= ParcelFileDescriptor.MODE_READ_ONLY;
                if (mode.contains("+")) imode |= ParcelFileDescriptor.MODE_APPEND;

                Log.d("MODE", "Mode: " + mode + ", imode: " + imode);
                Log.d("FILE", "File: " + file.getAbsoluteFile());

                return ParcelFileDescriptor.open(file, imode);
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
    }

    private File getFile(File root, Uri uri, String mode) throws FileNotFoundException {
        File file = new File(root, uri.getPath());
        if (!file.exists()) {
            if (mode.contains("w")) {
                File parent = file.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    // TODO: digg what to do as a best practice
                }
            } else {
                throw new FileNotFoundException(uri.getPath());
            }
        }
        return file;
    }
}
