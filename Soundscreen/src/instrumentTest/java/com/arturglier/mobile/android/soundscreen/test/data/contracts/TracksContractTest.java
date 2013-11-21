package com.arturglier.mobile.android.soundscreen.test.data.contracts;

import android.net.Uri;
import android.test.suitebuilder.annotation.SmallTest;

import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;

import junit.framework.TestCase;

import static org.fest.assertions.api.Assertions.assertThat;

public class TracksContractTest extends TestCase {

    @SmallTest
    public void testBuildUri() {
        try {
            Uri uri = TracksContract.buildUri(null);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        Long id = new Long(2);
        assertThat(TracksContract.buildUri(id))
            .isEqualTo(Uri.parse(TracksContract.CONTENT_URI + "/2"));
    }

    @SmallTest
    public void testBuildWaveformUri() {
        try {
            Uri uri = TracksContract.buildWaveformUri(null);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        Uri arg = TracksContract.CONTENT_URI;
        assertThat(TracksContract.buildWaveformUri(arg))
            .isEqualTo(Uri.parse(arg + "/waveforms"));
    }
}
