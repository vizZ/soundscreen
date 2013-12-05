package com.arturglier.mobile.android.soundscreen.test.data.models;

import com.arturglier.mobile.android.soundscreen.test.data.RobolectricGradleTestRunner;
import com.arturglier.mobile.android.soundscreen.data.models.Track;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.api.Assertions.assertThat;


@RunWith(RobolectricTestRunner.class)
public class TrackTest {

    private static Gson sGson;
    private Track mTrack;

    @BeforeClass
    public static void beforeClass() {
        sGson = new Gson();
    }

    @Before
    public void before() {
        mTrack = sGson.fromJson("{'kind':'track','id':103108986,'user_id':415058,'duration':5758710,'genre':'DeepHouse','title':'LiveMix@TematRzeka(RadioRoxy)[06.07.2013]','description':'LiveMixfromPlazoweIgraszki','uri':'https://api.soundcloud.com/tracks/103108986','user':{'id':415058,'kind':'user','permalink':'phil-jensky','username':'PhilJensky','uri':'https://api.soundcloud.com/users/415058','permalink_url':'http://soundcloud.com/phil-jensky','avatar_url':'https://i1.sndcdn.com/avatars-000024818254-2898mm-large.jpg?3eddc42'},'permalink_url':'http://soundcloud.com/phil-jensky/live-mix-temat-rzeka-radio','waveform_url':'https://w1.sndcdn.com/mZ5zwKxzEWy7_m.png','playback_count':465,'download_count':49,'favoritings_count':15,'comment_count':3}", Track.class);
        assertThat(mTrack).isNotNull();
    }

    @Test
    public void hasValidID() {
        assertEquals(mTrack.getServerId(), Long.valueOf(103108986));
    }
}
