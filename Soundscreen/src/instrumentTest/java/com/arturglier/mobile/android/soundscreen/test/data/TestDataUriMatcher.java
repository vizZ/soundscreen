package com.arturglier.mobile.android.soundscreen.test.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.suitebuilder.annotation.SmallTest;

import com.arturglier.mobile.android.soundscreen.data.DataUriMatcher;
import com.arturglier.mobile.android.soundscreen.data.matchers.DataMatcher;

import junit.framework.TestCase;

public class TestDataUriMatcher extends TestCase {

    private static final UriMatcher sUriMatcher = new DataUriMatcher("auth");

    @SmallTest
    public void testContentUris() {
        check("content://auth/foo", UriMatcher.NO_MATCH);

        check("content://auth/tracks", DataMatcher.TRACKS);
        check("content://auth/tracks/foo", UriMatcher.NO_MATCH);
        check("content://auth/tracks/waveforms", DataMatcher.TRACKS_WAVEFORMS);
        check("content://auth/tracks/1", DataMatcher.TRACKS_ID);

        check("content://auth/users", DataMatcher.USERS);
        check("content://auth/users/foo", UriMatcher.NO_MATCH);
        check("content://auth/users/2", DataMatcher.USERS_ID);
    }

    private void check(String uri, int expected) {
        int result = sUriMatcher.match(Uri.parse(uri));
        if (result != expected) {
            throw new RuntimeException(String.format("Failed on: %1$s, expected: %2$s, got: %3$s", uri, expected, result));
        }
    }
}