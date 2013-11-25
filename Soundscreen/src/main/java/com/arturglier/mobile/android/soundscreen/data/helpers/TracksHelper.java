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
            .column(TracksContract.ARTWORK_URL).asText()
            .column(TracksContract.WAVEFORM_URL).asText()
            .column(TracksContract.PLAYBACK_COUNT).asInteger()
            .column(TracksContract.DOWNLOAD_COUNT).asInteger()
            .column(TracksContract.FAVORITINGS_COUNT).asInteger()
            .column(TracksContract.COMMENT_COUNT).asInteger()
            .column(TracksContract.CACHED).asBooleanNotNullWithDefault(SQL.FALSE)
            .column(TracksContract.USED).asBooleanNotNullWithDefault(SQL.FALSE)
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

        if (oldVersion == 2) {
            db.execSQL("ALTER TABLE " + TracksContract.TABLE_NAME + " ADD COLUMN " + TracksContract.CACHED + " BOOLEAN NOT NULL DEFAULT FALSE");
            db.execSQL("ALTER TABLE " + TracksContract.TABLE_NAME + " ADD COLUMN " + TracksContract.USED + " BOOLEAN NOT NULL DEFAULT FALSE");
        }

        if (oldVersion == 3) {
            db.execSQL("ALTER TABLE " + TracksContract.TABLE_NAME + " ADD COLUMN " + TracksContract.ARTWORK_URL + " TEXT");
        }
    }
}
