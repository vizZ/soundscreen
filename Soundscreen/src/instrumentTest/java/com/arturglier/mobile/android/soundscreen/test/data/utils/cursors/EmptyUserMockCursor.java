package com.arturglier.mobile.android.soundscreen.test.data.utils.cursors;

public class EmptyUserMockCursor extends UserMockCursor {

    public EmptyUserMockCursor() {
        super();

        addRow(new String[]{
            null,
            null,
            null,
            null,
            null,
            null
        });
    }
}
