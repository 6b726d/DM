package com.example.rabbit_survival;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable, SensorEventListener {
    private Thread thread;
    private Paint paint;
    private boolean isPlaying, isGameOver;
    private int screenWidth, screenHeight;
    private float screenRatioWidth, screenRatioHeight;
    private boolean isTouch;
    private int score;
    private SharedPreferences preferences;
    private Background background;
    private Player player; //rabbit
    private Random random;
    private Carrot[] carrots;
    private Fox[] foxes;
    private GameActivity activity;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int speed, min_speed, max_speed;
    private SoundPool soundPool;
    private int sound_carrot, sound_fox;
    private Vibrator vibrator;

    GameView(GameActivity activity, Resources resources, boolean b_control) {
        super(activity);
        this.activity = activity;
        preferences = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
        }
        else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        sound_carrot = soundPool.load(activity, R.raw.pickup_coin, 1);
        sound_fox = soundPool.load(activity, R.raw.hit_hurt, 1);

        Display screen = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        screenWidth = screen.getWidth();
        screenHeight = screen.getHeight();
        screenRatioWidth = 1920f/screenWidth;
        screenRatioHeight = 1080f/screenHeight;
        isTouch = b_control;
        isGameOver = false;
        score = 0;
        paint = new Paint();
        paint.setTextSize(64);
        paint.setColor(Color.WHITE);
        background = new Background(resources, screenWidth, screenHeight);
        player = new Player(resources, screenRatioWidth, screenRatioHeight);
        player.x = screenWidth/2;
        player.y = screenHeight/2;
        random = new Random();
        carrots = new Carrot[8];
        for (int i=0; i < 8; i++) {
            carrots[i] = new Carrot(resources,screenRatioWidth,screenRatioHeight);
            carrots[i].x = random.nextInt(screenWidth);
            carrots[i].y = random.nextInt(screenHeight);
        }
        foxes = new Fox[10];
        for (int i=0; i < 3; i++) {
            foxes[i] = new Fox(resources,0,screenRatioWidth,screenRatioHeight);
            foxes[i].x = random.nextInt(screenWidth - foxes[i].width);
            foxes[i].y = 0;
        }
        for (int i=3; i < 5; i++) {
            foxes[i] = new Fox(resources,1,screenRatioWidth,screenRatioHeight);
            foxes[i].x = screenWidth;
            foxes[i].y = random.nextInt(screenHeight - foxes[i].height);
        }
        for (int i=5; i < 8; i++) {
            foxes[i] = new Fox(resources,2,screenRatioWidth,screenRatioHeight);
            foxes[i].x = random.nextInt(screenWidth - foxes[i].width);
            foxes[i].y = screenHeight;
        }
        for (int i=8; i < 10; i++) {
            foxes[i] = new Fox(resources,3,screenRatioWidth,screenRatioHeight);
            foxes[i].x = 0;
            foxes[i].y = random.nextInt(screenHeight - foxes[i].height);
        }
        sensorManager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        speed = 10;
        min_speed = 5;
        max_speed = 15;
        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    public void update() {
        //Player begin
        if (player.up) {
            player.y -= 20 * screenRatioHeight;
        }
        if (player.down) {
            player.y += 20 * screenRatioHeight;
        }
        if (player.left) {
            player.x -= 20 * screenRatioWidth;
        }
        if (player.right) {
            player.x += 20 * screenRatioWidth;
        }
        //Height
        if (player.y < 0) {
            player.y = 0;
        }
        if (player.y > screenHeight - player.height) {
            player.y = screenHeight - player.height;
        }
        //Width
        if (player.x < 0) {
            player.x = 0;
        }
        if (player.x > screenWidth - player.width) {
            player.x = screenWidth - player.width;
        }
        //Player end
        for (Carrot carrot: carrots) {
            //Height
            if (carrot.y < 0) {
                carrot.y = 0;
            }
            if (carrot.y > screenHeight - carrot.height) {
                carrot.y = screenHeight - carrot.height;
            }
            //Width
            if (carrot.x < 0) {
                carrot.x = 0;
            }
            if (carrot.x > screenWidth - carrot.width) {
                carrot.x = screenWidth - carrot.width;
            }
            if (Rect.intersects(carrot.getRect(), player.getRect())) {
                score++;
                soundPool.play(sound_carrot, 1, 1, 0, 0, 1);
                carrot.x = random.nextInt(screenWidth);
                carrot.y = random.nextInt(screenHeight);
            }
        }
        //Speed begin
        if ((score >= 0) && (score <= 20)) {
            speed = 10;
            min_speed = 5;
            max_speed = 15;
        }
        else if ((score > 20) && (score <= 40)) {
            speed = 20;
            min_speed = 15;
            max_speed = 25;
        }
        else if ((score > 40) && (score <= 60)) {
            speed = 30;
            min_speed = 25;
            max_speed = 35;
        }
        else if ((score > 60) && (score <= 80)) {
            speed = 40;
            min_speed = 35;
            max_speed = 45;
        }
        else if ((score > 100) && (score <= 120)) {
            speed = 50;
            min_speed = 45;
            max_speed = 55;
        }
        else {
            speed = 60;
            min_speed = 50;
            max_speed = 70;
        }
        //Speed end
        for (Fox fox: foxes) {
            fox.speed = speed;
            int min_x_bound = (int) (min_speed * screenRatioWidth);
            int max_x_bound = (int) (max_speed * screenRatioWidth);
            int min_y_bound = (int) (min_speed * screenRatioHeight);
            int max_y_bound = (int) (max_speed * screenRatioHeight);
            if (fox.type == 1) {
                fox.x -= fox.speed;
                if (fox.x < 0) {
                    fox.speed = random.nextInt(max_x_bound);
                    if (fox.speed < min_x_bound) {
                        fox.speed = min_x_bound;
                    }
                    fox.x = screenWidth;
                    fox.y = random.nextInt(screenHeight - fox.height);
                }
            }
            else if (fox.type == 2) {
                fox.y -= fox.speed;
                if (fox.y < 0) {
                    fox.speed = random.nextInt(max_y_bound);
                    if (fox.speed < min_y_bound) {
                        fox.speed = min_y_bound;
                    }
                    fox.x = random.nextInt(screenWidth - fox.width);
                    fox.y = screenHeight;
                }
            }
            else if (fox.type == 3) {
                fox.x += fox.speed;
                if (fox.x + fox.width > screenWidth) {
                    fox.speed = random.nextInt(max_x_bound);
                    if (fox.speed < min_x_bound) {
                        fox.speed = min_x_bound;
                    }
                    fox.x = 0;
                    fox.y = random.nextInt(screenHeight - fox.height);
                }
            }
            else {
                fox.y += fox.speed;
                if (fox.y + fox.height > screenHeight) {
                    fox.speed = random.nextInt(max_y_bound);
                    if (fox.speed < min_y_bound) {
                        fox.speed = min_y_bound;
                    }
                    fox.x = random.nextInt(screenWidth - fox.width);
                    fox.y = 0;
                }
            }
            if (Rect.intersects(fox.getRect(), player.getRect())) {
                soundPool.play(sound_fox, 1, 1, 0, 0, 1);
                vibrator.vibrate(1000);
                isGameOver = true;
                return;
            }
        }
    }

    public void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.ground,background.x,background.y,paint);
            canvas.drawBitmap(player.rabbit,player.x,player.y,paint);
            for (Carrot carrot: carrots) {
                canvas.drawBitmap(carrot.carrot,carrot.x,carrot.y,paint);
            }
            for (Fox fox: foxes) {
                canvas.drawBitmap(fox.fox,fox.x,fox.y,paint);
            }
            canvas.drawText("Score: "+score, 32, 64, paint);
            if (isGameOver) {
                isPlaying = false;
                getHolder().unlockCanvasAndPost(canvas);
                saveHighScore();
                goToMain();
                return;
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void goToMain() {
        try {
            Thread.sleep(2000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveHighScore() {
        if (preferences.getInt("highScore", 0) < score) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("highScore", score);
            editor.apply();
        }
    }

    public void sleep() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isTouch) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (event.getY() < screenHeight/2) {
                        player.up = true;
                    }
                    if (event.getY() > screenHeight/2) {
                        player.down = true;
                    }
                    if (event.getX() < screenWidth/2) {
                        player.left = true;
                    }
                    if (event.getX() > screenWidth/2) {
                        player.right = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    player.up = false;
                    player.down = false;
                    player.left = false;
                    player.right = false;
                    break;
            }
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!isTouch) {
            float y, z;
            y = event.values[1];
            z = event.values[2];
            if (y < -0.1) {
                player.left = true;
                player.right = false;
            }
            else if (y > 0.1) {
                player.right = true;
                player.left = false;
            }
            else {
                player.left = false;
                player.right = false;
            }
            if (z < -0.1) {
                player.down = true;
                player.up = false;
            }
            else if (z > 0.1) {
                player.up = true;
                player.down = false;
            }
            else {
                player.down = false;
                player.up = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //
    }
}
