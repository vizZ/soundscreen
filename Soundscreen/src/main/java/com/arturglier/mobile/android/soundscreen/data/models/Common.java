package com.arturglier.mobile.android.soundscreen.data.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.arturglier.mobile.android.soundscreen.data.contracts.CommonColumns;
import com.google.gson.annotations.SerializedName;

public abstract class Common {
    private Long _id;

    @SerializedName(CommonColumns.ID)
    private Long id;

    @SerializedName("uri")
    private String uri;

    @SerializedName(CommonColumns.PERMA_LINK)
    private String permaLink;

    public Common(Cursor cursor) {

    }

    public Long getLocalId() {
        return _id;
    }

    public void setLocalId(Long _id) {
        this._id = _id;
    }

    public Long getServerId() {
        return id;
    }

    public void setServerId(Long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPermaLink() {
        return permaLink;
    }

    public void setPermaLink(String permaLink) {
        this.permaLink = permaLink;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(CommonColumns._ID, getLocalId());
        values.put(CommonColumns.ID, getServerId());
        values.put(CommonColumns.URI, getUri());
        values.put(CommonColumns.PERMA_LINK, getPermaLink());
        return values;
    }
}
