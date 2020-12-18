package com.example.rabbit_survival;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Carrot {
    int x, y, width, height;
    Bitmap carrot;

    Carrot(Resources resources, float screenRatioWidth, float screenRatioHeight) {
        carrot = BitmapFactory.decodeResource(resources, R.drawable.ic_carrot);
        width = carrot.getWidth();
        height = carrot.getHeight();
        width /= 4;
        height /= 4;
        width *= (int) screenRatioWidth;
        height *= (int) screenRatioHeight;

        carrot = Bitmap.createScaledBitmap(carrot, width, height, false);

        x = 0;
        y = 0;
    }

    Rect getRect() {
        return new Rect(x, y, x+width, y+height);
    }
}
