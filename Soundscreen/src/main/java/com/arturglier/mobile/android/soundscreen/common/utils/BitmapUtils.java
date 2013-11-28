package com.arturglier.mobile.android.soundscreen.common.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;

public class BitmapUtils {

    public static Bitmap resize(Bitmap src, int screenWidth, int screenHeight) {
        Bitmap tmp = Bitmap.createScaledBitmap(src, screenWidth, screenHeight, true);

        int srcWidth = tmp.getWidth();
        int srcHeight = tmp.getHeight();

        RectF dstRect = new RectF(0, 0, screenWidth, screenHeight);
        RectF srcRect = new RectF(0, 0, srcWidth, srcHeight);

        Matrix matrix = new Matrix();
        matrix.setRectToRect(srcRect, dstRect, Matrix.ScaleToFit.FILL);

        return Bitmap.createBitmap(tmp, 0, 0, screenWidth, screenHeight, matrix, true);
    }
}
