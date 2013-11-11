package com.arturglier.mobile.android.soundscreen.data.contracts;

import android.net.Uri;

import com.arturglier.mobile.android.soundscreen.data.DataProvider;

public interface DataContract {
    Uri COMMON_CONTENT_URI = DataProvider.CONTENT_URI;
    
    String COMMON_CONTENT_TYPE = "vnd.arturglier.mobile.android.soundscreen.cursor.dir";
    String COMMON_CONTENT_ITEM_TYPE = "vnd.arturglier.mobile.android.soundscreen.cursor.item";
}
