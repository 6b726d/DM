package com.example.rabbit_survival;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Fox {
    int x, y, width, height;
    int type, speed;
    Bitmap fox;

    Fox(Resources resources, int n_type, float screenRatioWidth, float screenRatioHeight) {
        type = n_type;
        if (type == 1) {
            fox = BitmapFactory.decodeResource(resources, R.drawable.ic_fox1);
        }
        else if (type == 2) {
            fox = BitmapFactory.decodeResource(resources, R.drawable.ic_fox2);
        }
        else if (type == 3) {
            fox = BitmapFactory.decodeResource(resources, R.drawable.ic_fox3);
        }
        else {
            fox = BitmapFactory.decodeResource(resources, R.drawable.ic_fox);
        }
        width = fox.getWidth();
        height = fox.getHeight();
        width /= 4;
        height /= 4;
        width *= (int) screenRatioWidth;
        height *= (int) screenRatioHeight;

        fox = Bitmap.createScaledBitmap(fox, width, height, false);

        x = 0;
        y = 0;

        speed = 10;
    }

    Rect getRect() {
        return new Rect(x, y, x+width, y+height);
    }
}
