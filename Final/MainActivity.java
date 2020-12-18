package com.example.rabbit_survival;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public RadioButton rb_j, rb_g;
    public boolean b_control = true; //true: joystick, false: gyroscope

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rb_j = (RadioButton)findViewById(R.id.rbtnj);
        rb_g = (RadioButton)findViewById(R.id.rbtng);
        TextView highScoreText = findViewById(R.id.txt_highScore);
        SharedPreferences preferences = getSharedPreferences("game", MODE_PRIVATE);
        highScoreText.setText("High Score: "+preferences.getInt("highScore", 0));
    }

    public void play(View view) {
        if (rb_g.isChecked()) {
            b_control = false;
        }
        else {
            b_control = true;
        }
        Intent game = new Intent(this, GameActivity.class);
        game.putExtra("control", b_control);
        startActivity(game);
    }
}