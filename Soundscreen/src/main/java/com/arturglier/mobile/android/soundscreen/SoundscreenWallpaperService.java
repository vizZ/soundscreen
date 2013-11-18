package com.arturglier.mobile.android.soundscreen;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.net.SoundcloudService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class SoundscreenWallpaperService extends WallpaperService {

    private class SoundscreenEngine extends Engine {

        private Handler mHandler = new Handler();

        private class NextImage implements Runnable {

            @Override
            public void run() {
                Cursor cursor = getContentResolver().query(TracksContract.scheduled(), null, null, null, "RANDOM() LIMIT 1");
                if (cursor.moveToFirst()) {
                    Long _id = null;
                    try {
                        _id = cursor.getLong(cursor.getColumnIndexOrThrow(TracksContract._ID));
                    } finally {
                        cursor.close();
                    }

                    if (_id != null) {
                        Uri trackUri = TracksContract.buildUri(_id);

                        Bitmap image = null;
                        InputStream fis = null;
                        try {
                            fis = getContentResolver().openInputStream(TracksContract.buildWaveformUri(trackUri));
                            image = BitmapFactory.decodeStream(fis);
                            fis.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        draw(image);

                        ContentValues values = new ContentValues();
                        values.put(TracksContract.USED, true);
                        getContentResolver().update(trackUri, values, null, null);
                    }
                } else {
                    SoundcloudService.fetchFavorites(getApplicationContext());
                }

                mHandler.postDelayed(new NextImage(), TimeUnit.SECONDS.toMillis(10));
            }
        }

        private void draw(Bitmap image) {
            Canvas canvas = null;
            try {
                canvas = getSurfaceHolder().lockCanvas();
                if (canvas != null & image != null) {
                    Rect dst = new Rect(0, 0, this.getDesiredMinimumWidth(), this.getDesiredMinimumHeight());
                    Rect src = new Rect(0, 0, image.getWidth(), image.getHeight());
                    canvas.drawBitmap(image, src, dst, new Paint());
                }
            } finally {
                if (canvas != null) {
                    getSurfaceHolder().unlockCanvasAndPost(canvas);
                }
            }
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            SoundcloudService.schedule(getApplicationContext());

            mHandler.post(new NextImage());
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            SoundcloudService.cancel(getApplicationContext());
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                // TODO: resume wallpaper updates
            } else {
                // TODO: pause wallpaper updates
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
        }
    }

    @Override
    public Engine onCreateEngine() {
        return new SoundscreenEngine();
    }
}
