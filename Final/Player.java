package com.example.rabbit_survival;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Player {
    int x, y, width, height;
    boolean up, down, left, right;
    Bitmap rabbit;

    Player(Resources resources, float screenRatioWidth, float screenRatioHeight) {
        rabbit = BitmapFactory.decodeResource(resources, R.drawable.ic_rabbit);
        width = rabbit.getWidth();
        height = rabbit.getHeight();
        width /= 4;
        height /= 4;
        width *= (int) screenRatioWidth;
        height *= (int) screenRatioHeight;

        rabbit = Bitmap.createScaledBitmap(rabbit, width, height, false);

        x = 0;
        y = 0;
    }

    Rect getRect() {
        return new Rect(x, y, x+width, y+height);
    }
}