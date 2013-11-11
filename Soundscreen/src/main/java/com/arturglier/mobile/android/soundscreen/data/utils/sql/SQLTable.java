package com.arturglier.mobile.android.soundscreen.data.utils.sql;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class SQLTable {
    private static final CharSequence DELIMETER = ", ";

    private final String name;
    private List<SQLColumn> columns = new ArrayList<SQLColumn>();

    public SQLTable(String name) {
        this.name = name;
    }

    public SQLColumn column(String name) {
        SQLColumn column = new SQLColumn(name, this);
        this.columns.add(column);
        return column;
    }

    public String create() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ");

        if (TextUtils.isEmpty(this.name)) {
            throw new IllegalArgumentException("No table name was set");
        } else {
            builder.append(this.name);
            builder.append("(");
        }

        if (columns.isEmpty()) {
            throw new IllegalArgumentException("No column was specified for " + this.name + " table");
        } else {
            builder.append(TextUtils.join(DELIMETER, columns));
        }

        builder.append(");");
        return builder.toString();
    }
}
