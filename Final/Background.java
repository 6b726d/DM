package com.example.rabbit_survival;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int x, y, width, height;
    Bitmap ground;

    Background(Resources resources, int screenWidth, int screenHeight) {
        ground = BitmapFactory.decodeResource(resources, R.drawable.ic_background);

        width = screenWidth;
        height = screenHeight;

        ground = Bitmap.createScaledBitmap(ground, width, height, false);

        x = 0;
        y = 0;
    }
}
