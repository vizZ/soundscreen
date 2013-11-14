package com.arturglier.mobile.android.soundscreen.data.utils.sql;

import android.text.TextUtils;

public class SQLColumn {

    private SQLTable table;
    private String name;
    private String type;

    public SQLColumn(String name, SQLTable table) {
        this.table = table;
        this.name = name;
    }

    public SQLTable asIntegerPrimaryKeyAutoincrement() {
        this.type = "INTEGER PRIMARY KEY AUTOINCREMENT";
        return this.table;
    }

    public SQLTable asIntegerPrimaryKey() {
        this.type = "INTEGER PRIMARY KEY";
        return this.table;
    }

    public SQLTable asText() {
        this.type = "TEXT";
        return this.table;
    }

    public SQLTable asInteger() {
        this.type = "INTEGER";
        return this.table;
    }

    public String create() {
        StringBuilder builder = new StringBuilder();

        if (TextUtils.isEmpty(this.name)) {
            throw new IllegalArgumentException("Column name was not set");
        } else {
            builder.append(this.name);
        }

        if (TextUtils.isEmpty(this.type)) {
            throw new IllegalArgumentException("Not type was set to " + this.name + " column");
        } else {
            builder.append(" ");
            builder.append(this.type);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return this.create();
    }
}
