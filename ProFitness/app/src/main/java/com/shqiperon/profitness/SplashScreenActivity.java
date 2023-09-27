package com.shqiperon.profitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    Animation left,right;
    ImageView firstPart;
    ImageView secondPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firstPart = findViewById(R.id.firstPartIV);
        secondPart = findViewById(R.id.secondPartIV);

        left = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left);
        firstPart.setAnimation(left);
        right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right);
        secondPart.setAnimation(right);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();

            }
        },3000);

    }
}