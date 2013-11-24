package com.arturglier.mobile.android.soundscreen.test;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.arturglier.mobile.android.soundscreen.SoundscreenWallpaperService;

public class SoundscreenWallpaperServiceTest extends ServiceTestCase<SoundscreenWallpaperService> {

    public SoundscreenWallpaperServiceTest() {
        super(SoundscreenWallpaperService.class);
    }

    /**
     * Test basic startup/shutdown of Service
     */
    @SmallTest
    public void testStartable() {
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(getContext(), SoundscreenWallpaperService.class));
        startService(intent);
    }
}
