package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class Level1 extends AppCompatActivity {
    private TextView tv_s, tv_s1, tv_l1;
    private ImageButton ib10, ib11, ib12, ib13;     //Cards
    private ArrayList<Boolean> cards_enb;           //false=Disable, true=Enable
    private ArrayList<Integer> cards_img;           //0=cat, 1=dog, 2=rabbit
    private int[] my_pair;                          //pair(two cards)
    private int front_cards;                        //(0-2)
    private int attempts;                           //(cards/2)
    private int score, score1, s1;                  //(0-36) s1(36/attempts)
    private boolean start_enb;                      //see cards
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1);

        tv_s = (TextView) findViewById(R.id.tv_score_val);
        tv_s1 = (TextView) findViewById(R.id.tv_score2_val);
        tv_l1 = (TextView) findViewById(R.id.tv_level2_val);
        ib10 = (ImageButton)findViewById(R.id.ib_20);
        ib11 = (ImageButton)findViewById(R.id.ib_21);
        ib12 = (ImageButton)findViewById(R.id.ib_23);
        ib13 = (ImageButton)findViewById(R.id.ib_24);
        cards_enb = new ArrayList<>();
        cards_img = new ArrayList<>();
        for (int i=0; i<4; i++) {
            cards_enb.add(false);
        }
        //Images Begin
        cards_img.add(R.drawable.ic_cat);
        cards_img.add(R.drawable.ic_cat);
        cards_img.add(R.drawable.ic_dog);
        cards_img.add(R.drawable.ic_dog);
        Collections.shuffle(cards_img); //Shuffle Cards
        //Images End
        //Score Begin
        front_cards = 0;
        attempts = 2;
        score = 0;
        score1 = 0;
        s1 = 18; //Correct
        my_pair = new int[2]; //(Score)
        tv_l1.setText("1");
        //Score End
        //Buttons Begin
        start_enb = true;
        //Buttons End
        //Hint Cards Begin
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                ib10.setImageResource(R.drawable.ic_card);
                ib11.setImageResource(R.drawable.ic_card);
                ib12.setImageResource(R.drawable.ic_card);
                ib13.setImageResource(R.drawable.ic_card);
            }
        };
        //Hint Cards End
    }

    public void next(View view) {
        Intent l2 = new Intent(this, Level2.class);
        l2.putExtra("my_score", score);
        startActivity(l2);
    }

    public void end(View view) {
        Intent e = new Intent(this, MainActivity.class);
        startActivity(e);
    }

    public void start(View view) {
        if (start_enb) {
            ib10.setImageResource(cards_img.get(0));
            ib11.setImageResource(cards_img.get(1));
            ib12.setImageResource(cards_img.get(2));
            ib13.setImageResource(cards_img.get(3));
            handler.postDelayed(runnable, 2000);
            for (int i=0; i<4; i++) {
                cards_enb.set(i, true);
            }
            start_enb = false;
        }
    }

    public void click_card0(View view) {
        int card_id = 0;
        if ((cards_enb.get(card_id)) && (front_cards < 2) && (attempts > 0)) {
            ib10.setImageResource(cards_img.get(card_id));
            front_cards += 1;
            if (front_cards == 1) {
                my_pair[0] = card_id;
                cards_enb.set(my_pair[0], false);
            }
            else if (front_cards == 2) {
                my_pair[1] = card_id;
                if (cards_img.get(my_pair[0]).equals(cards_img.get(my_pair[1]))) {
                    score += s1;
                    score1 += s1;
                    tv_s.setText(String.valueOf(score));
                    tv_s1.setText(String.valueOf(score1));
                    cards_enb.set(my_pair[1], false);
                    attempts -= 1;
                    Toast.makeText(this, "Correct.", Toast.LENGTH_SHORT).show();
                }
                else {
                    cards_enb.set(my_pair[0], true);
                    cards_enb.set(my_pair[1], true);
                    attempts -= 1;
                    Toast.makeText(this, "Incorrect.", Toast.LENGTH_SHORT).show();
                }
                handler.postDelayed(runnable, 1000);
                front_cards = 0;
            }
        }
        else {
            Toast.makeText(this, "This card is disabled.", Toast.LENGTH_SHORT).show();
        }
    }

    public void click_card1(View view) {
        int card_id = 1;
        if ((cards_enb.get(card_id)) && (front_cards < 2) && (attempts > 0)) {
            ib11.setImageResource(cards_img.get(card_id));
            front_cards += 1;
            if (front_cards == 1) {
                my_pair[0] = card_id;
                cards_enb.set(my_pair[0], false);
            }
            else if (front_cards == 2) {
                my_pair[1] = card_id;
                if (cards_img.get(my_pair[0]).equals(cards_img.get(my_pair[1]))) {
                    score += s1;
                    score1 += s1;
                    tv_s.setText(String.valueOf(score));
                    tv_s1.setText(String.valueOf(score1));
                    cards_enb.set(my_pair[1], false);
                    attempts -= 1;
                    Toast.makeText(this, "Correct.", Toast.LENGTH_SHORT).show();
                }
                else {
                    cards_enb.set(my_pair[0], true);
                    cards_enb.set(my_pair[1], true);
                    attempts -= 1;
                    Toast.makeText(this, "Incorrect.", Toast.LENGTH_SHORT).show();
                }
                handler.postDelayed(runnable, 1000);
                front_cards = 0;
            }
        }
        else {
            Toast.makeText(this, "This card is disabled.", Toast.LENGTH_SHORT).show();
        }
    }

    public void click_card2(View view) {
        int card_id = 2;
        if ((cards_enb.get(card_id)) && (front_cards < 2) && (attempts > 0)) {
            ib12.setImageResource(cards_img.get(card_id));
            front_cards += 1;
            if (front_cards == 1) {
                my_pair[0] = card_id;
                cards_enb.set(my_pair[0], false);
            }
            else if (front_cards == 2) {
                my_pair[1] = card_id;
                if (cards_img.get(my_pair[0]).equals(cards_img.get(my_pair[1]))) {
                    score += s1;
                    score1 += s1;
                    tv_s.setText(String.valueOf(score));
                    tv_s1.setText(String.valueOf(score1));
                    cards_enb.set(my_pair[1], false);
                    attempts -= 1;
                    Toast.makeText(this, "Correct.", Toast.LENGTH_SHORT).show();
                }
                else {
                    cards_enb.set(my_pair[0], true);
                    cards_enb.set(my_pair[1], true);
                    attempts -= 1;
                    Toast.makeText(this, "Incorrect.", Toast.LENGTH_SHORT).show();
                }
                handler.postDelayed(runnable, 1000);
                front_cards = 0;
            }
        }
        else {
            Toast.makeText(this, "This card is disabled.", Toast.LENGTH_SHORT).show();
        }
    }

    public void click_card3(View view) {
        int card_id = 3;
        if ((cards_enb.get(card_id)) && (front_cards < 2) && (attempts > 0)) {
            ib13.setImageResource(cards_img.get(card_id));
            front_cards += 1;
            if (front_cards == 1) {
                my_pair[0] = card_id;
                cards_enb.set(my_pair[0], false);
            }
            else if (front_cards == 2) {
                my_pair[1] = card_id;
                if (cards_img.get(my_pair[0]).equals(cards_img.get(my_pair[1]))) {
                    score += s1;
                    score1 += s1;
                    tv_s.setText(String.valueOf(score));
                    tv_s1.setText(String.valueOf(score1));
                    cards_enb.set(my_pair[1], false);
                    attempts -= 1;
                    Toast.makeText(this, "Correct.", Toast.LENGTH_SHORT).show();
                }
                else {
                    cards_enb.set(my_pair[0], true);
                    cards_enb.set(my_pair[1], true);
                    attempts -= 1;
                    Toast.makeText(this, "Incorrect.", Toast.LENGTH_SHORT).show();
                }
                handler.postDelayed(runnable, 1000);
                front_cards = 0;
            }
        }
        else {
            Toast.makeText(this, "This card is disabled.", Toast.LENGTH_SHORT).show();
        }
    }
}
