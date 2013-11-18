package com.arturglier.mobile.android.soundscreen.data.dao;

import android.content.Context;
import android.net.Uri;

import java.util.List;

public abstract class AbstractDAO<T> implements DAO<T> {

    protected final Context mContext;

    public AbstractDAO(Context context) {
        mContext = context;
    }

    @Override
    public T get(Uri uri) {
        return null;
    }

    @Override
    public T get(long id) {
        return null;
    }

    @Override
    public List<T> getAll() {
        return null;
    }

    @Override
    public T add(T t) {
        return null;
    }

    @Override
    public T update(T t) {
        return null;
    }

    @Override
    public void delete(T t) {

    }

    @Override
    public void deleteAll() {

    }
}
