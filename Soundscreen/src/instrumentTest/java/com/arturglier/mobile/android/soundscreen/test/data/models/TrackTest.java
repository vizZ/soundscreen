package com.arturglier.mobile.android.soundscreen.test.data.models;

import android.content.ContentValues;

import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.models.Track;
import com.arturglier.mobile.android.soundscreen.test.data.utils.cursors.TrackMockCursor;
import com.arturglier.mobile.android.soundscreen.test.data.utils.json.TrackMockJSON;
import com.google.gson.Gson;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import static org.fest.assertions.api.Assertions.assertThat;

public class TrackTest extends TestCase {

    private TrackMockCursor mMockCursor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMockCursor = new TrackMockCursor();

        assertThat(mMockCursor).isNotNull();
        assertThat(mMockCursor.moveToFirst()).isTrue();
        assertThat(mMockCursor.getCount()).isEqualTo(1);
    }

    public void test_Track_Cursor() {
        Track track = new Track(mMockCursor);
        assertThat(track).isNotNull();

        assertThat(track.getLocalId()).isEqualTo(Long.parseLong(TrackMockCursor._ID));
        assertThat(track.getServerId()).isEqualTo(Long.parseLong(TrackMockCursor.ID));
        assertThat(track.getPermaLink()).isEqualTo(TrackMockCursor.PERMA_LINK);
        assertThat(track.getUri()).isEqualTo(TrackMockCursor.URI);

        assertThat(track.getTitle()).isEqualTo(TrackMockCursor.TITLE);
        assertThat(track.getDuration()).isEqualTo(Long.parseLong(TrackMockCursor.DURATION));
        assertThat(track.getGenre()).isEqualTo(TrackMockCursor.GENRE);
        assertThat(track.getDescription()).isEqualTo(TrackMockCursor.DESCRIPTION);
        assertThat(track.getUserId()).isEqualTo(Integer.parseInt(TrackMockCursor.USER_ID));
        assertThat(track.getWaveformUrl()).isEqualTo(TrackMockCursor.WAVEFORM_URL);
        assertThat(track.getPlaybackCount()).isEqualTo(Integer.parseInt(TrackMockCursor.PLAYBACK_COUNT));
        assertThat(track.getDownloadCount()).isEqualTo(Integer.parseInt(TrackMockCursor.DOWNLOAD_COUNT));
        assertThat(track.getFavoritingsCount()).isEqualTo(Integer.parseInt(TrackMockCursor.FAVORITINGS_COUNT));
        assertThat(track.getCommentCount()).isEqualTo(Integer.parseInt(TrackMockCursor.COMMENT_COUNT));

        assertThat(track.isCached()).isEqualTo(Boolean.parseBoolean(TrackMockCursor.CACHED));
        assertThat(track.wasUsed()).isEqualTo(Boolean.parseBoolean(TrackMockCursor.USED));
    }

    public void test_toContentValues() {
        Track track = new Track(mMockCursor);
        ContentValues values = track.toContentValues();

        // check the existence of the expected keys
        assertThat(values.containsKey(TracksContract._ID)).isFalse();

        assertThat(values.containsKey(TracksContract.ID)).isTrue();
        assertThat(values.containsKey(TracksContract.URI)).isTrue();
        assertThat(values.containsKey(TracksContract.PERMA_LINK)).isTrue();

        assertThat(values.containsKey(TracksContract.TITLE)).isTrue();
        assertThat(values.containsKey(TracksContract.DURATION)).isTrue();
        assertThat(values.containsKey(TracksContract.GENRE)).isTrue();
        assertThat(values.containsKey(TracksContract.DESCRIPTION)).isTrue();
        assertThat(values.containsKey(TracksContract.USER_ID)).isTrue();
        assertThat(values.containsKey(TracksContract.WAVEFORM_URL)).isTrue();
        assertThat(values.containsKey(TracksContract.PLAYBACK_COUNT)).isTrue();
        assertThat(values.containsKey(TracksContract.DOWNLOAD_COUNT)).isTrue();
        assertThat(values.containsKey(TracksContract.FAVORITINGS_COUNT)).isTrue();

        assertThat(values.containsKey(TracksContract.CACHED)).isTrue();
        assertThat(values.containsKey(TracksContract.USED)).isTrue();

        // check the values of the expected keys
        assertThat(values.getAsLong(TracksContract.ID)).isEqualTo(Long.parseLong(TrackMockCursor.ID));
        assertThat(values.getAsString(TracksContract.URI)).isEqualTo(TrackMockCursor.URI);
        assertThat(values.getAsString(TracksContract.PERMA_LINK)).isEqualTo(TrackMockCursor.PERMA_LINK);

        assertThat(values.getAsString(TracksContract.TITLE)).isEqualTo(TrackMockCursor.TITLE);
        assertThat(values.getAsLong(TracksContract.DURATION)).isEqualTo(Long.parseLong(TrackMockCursor.DURATION));
        assertThat(values.getAsString(TracksContract.GENRE)).isEqualTo(TrackMockCursor.GENRE);
        assertThat(values.getAsString(TracksContract.DESCRIPTION)).isEqualTo(TrackMockCursor.DESCRIPTION);
        assertThat(values.getAsLong(TracksContract.USER_ID)).isEqualTo(Long.parseLong(TrackMockCursor.USER_ID));
        assertThat(values.getAsString(TracksContract.WAVEFORM_URL)).isEqualTo(TrackMockCursor.WAVEFORM_URL);
        assertThat(values.getAsInteger(TracksContract.PLAYBACK_COUNT)).isEqualTo(Integer.parseInt(TrackMockCursor.PLAYBACK_COUNT));
        assertThat(values.getAsInteger(TracksContract.DOWNLOAD_COUNT)).isEqualTo(Integer.parseInt(TrackMockCursor.DOWNLOAD_COUNT));
        assertThat(values.getAsInteger(TracksContract.FAVORITINGS_COUNT)).isEqualTo(Integer.parseInt(TrackMockCursor.FAVORITINGS_COUNT));

        assertThat(values.getAsBoolean(TracksContract.CACHED)).isEqualTo(Boolean.FALSE);
        assertThat(values.getAsBoolean(TracksContract.USED)).isEqualTo(Boolean.FALSE);
    }

    public void test_fromJson() throws JSONException {
        Gson gson = new Gson();
        JSONObject json = new TrackMockJSON();
        Track track = gson.fromJson(json.toString(), Track.class);

        assertThat(track).isNotNull();

        assertThat(track.getServerId()).isEqualTo(json.getLong(TracksContract.ID));
        assertThat(track.getPermaLink()).isEqualTo(json.getString(TracksContract.PERMA_LINK));
        assertThat(track.getUri()).isEqualTo(json.getString(TracksContract.URI));

        assertThat(track.getTitle()).isEqualTo(json.getString(TracksContract.TITLE));
        assertThat(track.getDuration()).isEqualTo(json.getLong(TracksContract.DURATION));
        assertThat(track.getGenre()).isEqualTo(json.getString(TracksContract.GENRE));
        assertThat(track.getDescription()).isEqualTo(json.getString(TracksContract.DESCRIPTION));
        assertThat(track.getUserId()).isEqualTo(json.getInt(TracksContract.USER_ID));
        assertThat(track.getWaveformUrl()).isEqualTo(json.getString(TracksContract.WAVEFORM_URL));
        assertThat(track.getPlaybackCount()).isEqualTo(json.getInt(TracksContract.PLAYBACK_COUNT));
        assertThat(track.getDownloadCount()).isEqualTo(json.getInt(TracksContract.DOWNLOAD_COUNT));
        assertThat(track.getFavoritingsCount()).isEqualTo(json.getInt(TracksContract.FAVORITINGS_COUNT));
        assertThat(track.getCommentCount()).isEqualTo(json.getInt(TracksContract.COMMENT_COUNT));

        // these should not be initialized by GSON therefore should be null or false by default
        assertThat(track.getLocalId()).isNull();
        assertThat(track.isCached()).isFalse();
        assertThat(track.wasUsed()).isFalse();
    }
}
