package com.arturglier.mobile.android.soundscreen.data.helpers;

import android.database.sqlite.SQLiteDatabase;

import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;

public class TracksHelper implements DataHelper {

    private static final String CREATE_TABLE_TRACKS =
        SQL.table(TracksContract.TABLE_NAME)
            .column(TracksContract._ID).asIntegerPrimaryKey()
            .column(TracksContract.ID).asIntegerNotNullUniqueOnConflictReplace()
            .column(TracksContract.USER_ID).asIntegerNotNullReferences(UsersContract.TABLE_NAME, UsersContract.ID)
            .column(TracksContract.URI).asText()
            .column(TracksContract.PERMA_LINK).asText()
            .column(TracksContract.TITLE).asText()
            .column(TracksContract.DURATION).asInteger()
            .column(TracksContract.GENRE).asText()
            .column(TracksContract.DESCRIPTION).asText()
            .column(TracksContract.WAVEFORM_URL).asText()
            .column(TracksContract.PLAYBACK_COUNT).asInteger()
            .column(TracksContract.DOWNLOAD_COUNT).asInteger()
            .column(TracksContract.FAVORITINGS_COUNT).asInteger()
            .column(TracksContract.COMMENT_COUNT).asInteger()
            .create();


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRACKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL("DROP TABLE IF EXISTS " + TracksContract.TABLE_NAME);
            db.execSQL(CREATE_TABLE_TRACKS);
        }
    }
}
