package com.arturglier.mobile.android.soundscreen.test.data.utils.cursors;

public class SingleUserMockCursor extends UserMockCursor {

    public SingleUserMockCursor() {
        super();

        addRow(new String[]{
            _ID,
            ID,
            PERMA_LINK,
            URI,
            USERNAME,
            AVATAR_URL
        });
    }
}
