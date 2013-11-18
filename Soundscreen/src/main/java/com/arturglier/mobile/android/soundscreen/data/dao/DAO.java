package com.arturglier.mobile.android.soundscreen.data.dao;

import android.net.Uri;

import java.util.List;

public interface DAO<T> {

    T get(Uri uri);

    T get(long id);

    List<T> getAll();

    T add(T t);

    T update(T t);

    void delete(T t);

    void deleteAll();
}
