package com.arturglier.mobile.android.soundscreen.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.models.Track;

import java.util.ArrayList;
import java.util.List;

public class TracksDAO extends AbstractDAO<Track> {

    public TracksDAO(Context context) {
        super(context);
    }

    @Override
    public Track get(Uri uri) {
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                return new Track(cursor);
            } else {
                return null;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public Track get(long id) {
        return get(TracksContract.buildUri(id));
    }

    @Override
    public List<Track> getAll() {
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(TracksContract.CONTENT_URI, null, null, null, null);
            if (cursor.moveToFirst()) {
                List<Track> list = new ArrayList<Track>();
                do {
                    list.add(new Track(cursor));
                } while (cursor.moveToNext());
                return list;
            } else {
                return null;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public Track add(Track t) {
        return null;
    }

    @Override
    public Track update(Track t) {
        return null;
    }

    @Override
    public void delete(Track t) {

    }

    @Override
    public void deleteAll() {

    }
}
