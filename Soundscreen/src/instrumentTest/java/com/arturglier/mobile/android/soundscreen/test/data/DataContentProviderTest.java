package com.arturglier.mobile.android.soundscreen.test.data;

import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import com.arturglier.mobile.android.soundscreen.data.DataContentProvider;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;
import com.arturglier.mobile.android.soundscreen.data.models.Track;
import com.arturglier.mobile.android.soundscreen.data.models.User;
import com.arturglier.mobile.android.soundscreen.test.data.utils.cursors.SingleUserMockCursor;
import com.arturglier.mobile.android.soundscreen.test.data.utils.cursors.TrackMockCursor;

import static org.fest.assertions.api.Assertions.assertThat;

public class DataContentProviderTest extends ProviderTestCase2<DataContentProvider> {

    public DataContentProviderTest() {
        super(DataContentProvider.class, DataContentProvider.AUTHORITY);
    }

    public void test_insert_User() {
        SingleUserMockCursor mockCursor = new SingleUserMockCursor();
        assertThat(mockCursor.moveToFirst()).isTrue();

        User user1 = new User(mockCursor);

        Uri uri = getMockContentResolver().insert(UsersContract.CONTENT_URI, user1.toContentValues());
        assertThat(uri).isNotNull();

        Cursor cursor = getMockContentResolver().query(uri, null, null, null, null);
        assertThat(cursor.moveToFirst()).isTrue();
        assertThat(cursor.getCount()).isEqualTo(1);

        User user2 = new User(cursor);

        assertThat(user1.getServerId()).isEqualTo(user2.getServerId());
        assertThat(user1.getUri()).isEqualTo(user2.getUri());
        assertThat(user1.getPermaLink()).isEqualTo(user2.getPermaLink());

        assertThat(user1.getUsername()).isEqualTo(user2.getUsername());
        assertThat(user1.getAvatarUrl()).isEqualTo(user2.getAvatarUrl());
    }

    public void test_insert_Track() {
        TrackMockCursor mockCursor = new TrackMockCursor();
        assertThat(mockCursor.moveToFirst()).isTrue();

        Track track1 = new Track(mockCursor);

        Uri uri = getMockContentResolver().insert(TracksContract.CONTENT_URI, track1.toContentValues());
        assertThat(uri).isNotNull();

        Cursor cursor = getMockContentResolver().query(uri, null, null, null, null);
        assertThat(cursor.moveToFirst()).isTrue();
        assertThat(cursor.getCount()).isEqualTo(1);

        Track track2 = new Track(cursor);

        assertThat(track1.getServerId()).isEqualTo(track2.getServerId());
        assertThat(track1.getUri()).isEqualTo(track2.getUri());
        assertThat(track1.getPermaLink()).isEqualTo(track2.getPermaLink());

        assertThat(track1.getTitle()).isEqualTo(track2.getTitle());
        assertThat(track1.getDuration()).isEqualTo(track2.getDuration());
        assertThat(track1.getGenre()).isEqualTo(track2.getGenre());
        assertThat(track1.getDescription()).isEqualTo(track2.getDescription());
        assertThat(track1.getUserId()).isEqualTo(track2.getUserId());
        assertThat(track1.getArtworkUrl()).isEqualTo(track2.getArtworkUrl());
        assertThat(track1.getWaveformUrl()).isEqualTo(track2.getWaveformUrl());
        assertThat(track1.getPlaybackCount()).isEqualTo(track2.getPlaybackCount());
        assertThat(track1.getDownloadCount()).isEqualTo(track2.getDownloadCount());
        assertThat(track1.getFavoritingsCount()).isEqualTo(track2.getFavoritingsCount());
        assertThat(track1.getCommentCount()).isEqualTo(track2.getCommentCount());

        assertThat(track1.isCached()).isEqualTo(track2.isCached());
        assertThat(track1.wasUsed()).isEqualTo(track2.wasUsed());
    }

}
