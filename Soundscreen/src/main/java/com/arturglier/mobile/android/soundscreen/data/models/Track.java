package com.arturglier.mobile.android.soundscreen.data.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.utils.sql.CursorHelper;
import com.google.gson.annotations.SerializedName;

public class Track extends Common {
    @SerializedName(TracksContract.TITLE)
    private String title;

    @SerializedName(TracksContract.DURATION)
    private Long duration;

    @SerializedName(TracksContract.GENRE)
    private String genre;

    @SerializedName(TracksContract.DESCRIPTION)
    private String description;

    @SerializedName(TracksContract.WAVEFORM_URL)
    private String waveformUrl;

    @SerializedName(TracksContract.PLAYBACK_COUNT)
    private Integer playbackCount;

    @SerializedName(TracksContract.DOWNLOAD_COUNT)
    private Integer downloadCount;

    @SerializedName(TracksContract.FAVORITINGS_COUNT)
    private Integer favoritingsCount;

    @SerializedName(TracksContract.COMMENT_COUNT)
    private Integer commentCount;

    @SerializedName(TracksContract.USER_ID)
    private Integer userId;

    @SerializedName("user")
    private User user;

    private Boolean cached;
    private Boolean used;

    public Track(Cursor cursor) {
        super(cursor);

        CursorHelper helper = new CursorHelper(cursor);

        setTitle(helper.getString(TracksContract.TITLE));
        setDuration(helper.getLong(TracksContract.DURATION));
        setDescription(helper.getString(TracksContract.DESCRIPTION));
        setWaveformUrl(helper.getString(TracksContract.WAVEFORM_URL));
        setPlaybackCount(helper.getInt(TracksContract.PLAYBACK_COUNT));
        setDownloadCount(helper.getInt(TracksContract.DOWNLOAD_COUNT));
        setFavoritingsCount(helper.getInt(TracksContract.FAVORITINGS_COUNT));
        setCommentCount(helper.getInt(TracksContract.COMMENT_COUNT));
        setUserId(helper.getInt(TracksContract.USER_ID));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWaveformUrl() {
        return waveformUrl;
    }

    public void setWaveformUrl(String waveformUrl) {
        this.waveformUrl = waveformUrl;
    }

    public Integer getPlaybackCount() {
        return playbackCount;
    }

    public void setPlaybackCount(Integer playbackCount) {
        this.playbackCount = playbackCount;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Integer getFavoritingsCount() {
        return favoritingsCount;
    }

    public void setFavoritingsCount(Integer favoritingsCount) {
        this.favoritingsCount = favoritingsCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = super.toContentValues();
        values.put(TracksContract.TITLE, getTitle());
        values.put(TracksContract.DURATION, getDuration());
        values.put(TracksContract.GENRE, getGenre());
        values.put(TracksContract.DESCRIPTION, getDescription());
        values.put(TracksContract.USER_ID, getUserId());
        values.put(TracksContract.WAVEFORM_URL, getWaveformUrl());
        values.put(TracksContract.PLAYBACK_COUNT, getPlaybackCount());
        values.put(TracksContract.DOWNLOAD_COUNT, getDownloadCount());
        values.put(TracksContract.FAVORITINGS_COUNT, getFavoritingsCount());
        values.put(TracksContract.COMMENT_COUNT, getCommentCount());
        values.put(TracksContract.CACHED, isCached());
        values.put(TracksContract.USED, wasUsed());
        return values;
    }

    public boolean isCached() {
        return cached == null || cached == Boolean.FALSE ? false : true;
    }

    public void setCached(Boolean cached) {
        this.cached = cached;
    }

    public boolean wasUsed() {
        return used == null || used == Boolean.FALSE ? false : true;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
}
