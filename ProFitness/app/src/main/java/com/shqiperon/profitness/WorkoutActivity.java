package com.shqiperon.profitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class WorkoutActivity extends AppCompatActivity {

    private CardView chestCV, armsCV, legsCV, backCV, cardioCV;
    private Animation chest, arms, legs, back, cardio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        chestCV = findViewById(R.id.chestCV);
        armsCV = findViewById(R.id.armsCV);
        legsCV = findViewById(R.id.legsCV);
        backCV = findViewById(R.id.backCV);
        cardioCV = findViewById(R.id.cardioCV);

        chest = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.chest);
        arms = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.arms);
        legs = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.legs);
        back = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.back);
        cardio = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.cardio);

        chestCV.startAnimation(chest);
        armsCV.startAnimation(arms);
        legsCV.startAnimation(legs);
        backCV.startAnimation(back);
        cardioCV.startAnimation(cardio);

        chestCV.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),ChestActivity.class));
            finish();
        });
    }
}