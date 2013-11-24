package com.arturglier.mobile.android.soundscreen.test.data.models;

import android.content.ContentValues;

import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;
import com.arturglier.mobile.android.soundscreen.data.models.User;
import com.arturglier.mobile.android.soundscreen.test.data.utils.cursors.SingleUserMockCursor;
import com.arturglier.mobile.android.soundscreen.test.data.utils.cursors.UserMockCursor;
import com.arturglier.mobile.android.soundscreen.test.data.utils.json.UserMockJSON;
import com.google.gson.Gson;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import static org.fest.assertions.api.Assertions.assertThat;

public class UserTest extends TestCase {

    private UserMockCursor mMockCursor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMockCursor = new SingleUserMockCursor();

        assertThat(mMockCursor).isNotNull();
        assertThat(mMockCursor.moveToFirst()).isTrue();
        assertThat(mMockCursor.getCount()).isEqualTo(1);
    }

    public void test_User_Cursor() {
        User user = new User(mMockCursor);
        assertThat(user).isNotNull();

        assertThat(user.getLocalId()).isEqualTo(Long.parseLong(UserMockCursor._ID));
        assertThat(user.getServerId()).isEqualTo(Long.parseLong(UserMockCursor.ID));
        assertThat(user.getPermaLink()).isEqualTo(UserMockCursor.PERMA_LINK);
        assertThat(user.getUri()).isEqualTo(UserMockCursor.URI);

        assertThat(user.getUsername()).isEqualTo(UserMockCursor.USERNAME);
        assertThat(user.getAvatarUrl()).isEqualTo(UserMockCursor.AVATAR_URL);
    }

    public void test_toContentValues() {
        User user = new User(mMockCursor);
        ContentValues values = user.toContentValues();

        // check the existence of the expected keys
        assertThat(values.containsKey(UsersContract._ID)).isFalse();

        assertThat(values.containsKey(UsersContract.ID)).isTrue();
        assertThat(values.containsKey(UsersContract.URI)).isTrue();
        assertThat(values.containsKey(UsersContract.PERMA_LINK)).isTrue();

        assertThat(values.containsKey(UsersContract.USERNAME)).isTrue();
        assertThat(values.containsKey(UsersContract.AVATAR_URL)).isTrue();

        // check the values of the expected keys
        assertThat(values.getAsLong(UsersContract.ID)).isEqualTo(Long.parseLong(UserMockCursor.ID));
        assertThat(values.getAsString(UsersContract.URI)).isEqualTo(UserMockCursor.URI);
        assertThat(values.getAsString(UsersContract.PERMA_LINK)).isEqualTo(UserMockCursor.PERMA_LINK);

        assertThat(values.getAsString(UsersContract.USERNAME)).isEqualTo(UserMockCursor.USERNAME);
        assertThat(values.getAsString(UsersContract.AVATAR_URL)).isEqualTo(UserMockCursor.AVATAR_URL);
    }

    public void test_fromJson() throws JSONException {
        Gson gson = new Gson();
        JSONObject json = new UserMockJSON();
        User user = gson.fromJson(json.toString(), User.class);

        assertThat(user).isNotNull();

        assertThat(user.getServerId()).isEqualTo(json.getLong(UsersContract.ID));
        assertThat(user.getPermaLink()).isEqualTo(json.getString(UsersContract.PERMA_LINK));
        assertThat(user.getUri()).isEqualTo(json.getString(UsersContract.URI));

        assertThat(user.getUsername()).isEqualTo(json.getString(UsersContract.USERNAME));
        assertThat(user.getAvatarUrl()).isEqualTo(json.getString(UsersContract.AVATAR_URL));

        // these should not be initialized by GSON therefore should be null or false by default
        assertThat(user.getLocalId()).isNull();
    }
}
