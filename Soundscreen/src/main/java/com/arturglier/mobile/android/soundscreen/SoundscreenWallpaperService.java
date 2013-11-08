package com.arturglier.mobile.android.soundscreen;

import android.service.wallpaper.WallpaperService;

public class SoundscreenWallpaperService extends WallpaperService {

    private class SoundscreenEngine extends Engine {

    }

    @Override
    public Engine onCreateEngine() {
        return new SoundscreenEngine();
    }
}
