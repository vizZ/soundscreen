package com.arturglier.mobile.android.soundscreen;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.arturglier.mobile.android.soundscreen.common.utils.BitmapUtils;
import com.arturglier.mobile.android.soundscreen.common.utils.IntentUtils;
import com.arturglier.mobile.android.soundscreen.data.contracts.TracksContract;
import com.arturglier.mobile.android.soundscreen.data.models.Track;
import com.arturglier.mobile.android.soundscreen.net.services.FileService;
import com.arturglier.mobile.android.soundscreen.net.services.SyncService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class SoundscreenWallpaperService extends WallpaperService {

    private class SoundscreenEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener {

        private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler.post(new NextImage());
            }
        };

        private Handler mHandler = new Handler();

        private long mDuration = TimeUnit.SECONDS.toMillis(15);

        private Track mCurrentTrack;

        private Bitmap mCurrentArtworks;
        private Bitmap mCurrentWaveform;

        private GestureDetectorCompat mGestureDetector;

        private int mWidth;
        private int mHeight;

        private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mCurrentTrack != null) {
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("soundcloud://tracks:" + mCurrentTrack.getServerId()));
                    appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    if (IntentUtils.isActivityAvailable(getApplicationContext(), appIntent)) {
                        startActivity(appIntent);
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCurrentTrack.getPermaLink()));
                        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(browserIntent);
                    }
                }

                return true;
            }
        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(getString(R.string.pref_duration))) {
                int duration = sharedPreferences.getInt(key, getResources().getInteger(R.integer.duration_entry_15sec));
                mDuration = TimeUnit.SECONDS.toMillis(duration);
            }
        }

        private class NextImage implements Runnable {

            @Override
            public void run() {
                if (isVisible()) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(TracksContract.scheduled(), null, null, null, "RANDOM() LIMIT 1");
                        if (cursor.moveToFirst()) {
                            Track track = new Track(cursor);

                            InputStream fis = null;

                            Bitmap artworks = null;
                            try {
                                fis = getContentResolver().openInputStream(TracksContract.buildArtworksUri(track.getLocalId()));
                                artworks = BitmapFactory.decodeStream(fis);
                                fis.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (mCurrentArtworks != null) {
                                mCurrentArtworks = artworks;
                            }

                            Bitmap waveform = null;
                            try {
                                fis = getContentResolver().openInputStream(TracksContract.buildWaveformUri(track.getLocalId()));
                                waveform = BitmapFactory.decodeStream(fis);
                                fis.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (waveform != null) {
                                Bitmap resizedBitmap = BitmapUtils.resize(waveform, mWidth, mHeight);

                                mCurrentWaveform = resizedBitmap;

                                draw();

                                ContentValues values = new ContentValues();
                                values.put(TracksContract.USED, true);
                                getContentResolver().update(TracksContract.buildUri(track.getLocalId()), values, null, null);

                            }

                            mCurrentTrack = track;
                            mHandler.postDelayed(new NextImage(), mDuration);
                        } else {
                            FileService.start(getApplicationContext());
                        }
                    } finally {
                        if (cursor != null) cursor.close();
                    }
                }
            }
        }

        private class DrawImage implements Runnable {

            @Override
            public void run() {
                draw();
            }
        }

        private boolean draw() {
            Canvas canvas = null;
            try {
                canvas = getSurfaceHolder().lockCanvas();
                if (canvas != null) {
                    if (mCurrentWaveform != null) {
                        canvas.drawBitmap(mCurrentWaveform, 0, 0, new Paint());
                    }

                    return true;
                }
            } finally {
                if (canvas != null) {
                    getSurfaceHolder().unlockCanvasAndPost(canvas);
                }
            }
            return false;
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int duration = prefs.getInt(getString(R.string.pref_duration), getResources().getInteger(R.integer.duration_entry_15sec));
            mDuration = TimeUnit.SECONDS.toMillis(duration);

            mGestureDetector = new GestureDetectorCompat(getApplicationContext(), new MyGestureListener());

            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .registerOnSharedPreferenceChangeListener(this);

            LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mMessageReceiver, new IntentFilter(FileService.ACTION_FILE_AVAILABLE));

            SyncService.startAndRepeat(getApplicationContext());
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .unregisterOnSharedPreferenceChangeListener(this);

            LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mMessageReceiver);

            SyncService.stop(getApplicationContext());
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);

            mWidth = width;
            mHeight = height;
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                mHandler.post(new NextImage());
            } else {
                mHandler.removeCallbacksAndMessages(null);
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
            super.onTouchEvent(event);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
    }

    @Override
    public Engine onCreateEngine() {
        return new SoundscreenEngine();
    }
}