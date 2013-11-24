package com.arturglier.mobile.android.soundscreen.data.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.arturglier.mobile.android.soundscreen.data.contracts.UsersContract;
import com.arturglier.mobile.android.soundscreen.data.utils.sql.CursorHelper;
import com.google.gson.annotations.SerializedName;

public class User extends Common {
    @SerializedName(UsersContract.USERNAME)
    private String username;

    @SerializedName(UsersContract.AVATAR_URL)
    private String avatarUrl;

    public User(Cursor cursor) {
        super(cursor);

        CursorHelper helper = new CursorHelper(cursor);

        setUsername(helper.getString(UsersContract.USERNAME));
        setAvatarUrl(helper.getString(UsersContract.AVATAR_URL));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = super.toContentValues();
        values.put(UsersContract.USERNAME, getUsername());
        values.put(UsersContract.AVATAR_URL, getAvatarUrl());
        return values;
    }
}
