package com.shqiperon.profitness;

import static com.shqiperon.profitness.R.drawable.custom_horizontal_progress_bar_done;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.Locale;

public class ChestActivity extends AppCompatActivity {


    private MediaPlayer mediaPlayer, shortBeep, softBeep;
    private VideoView chestVideo;
    private TextView timerValue, warmUpTxtView, pushUpTxtView, benchTxtView, flyesTxtView, workoutDoneTxtView,
            toastTxtView, warmupPercentageTxt, pushUpPercentageTxt, benchPercentageTxt, flyesPercentageTxt;
    private ImageView warmUpImg, pushUpImg, benchImg, flyesImg, backArrow;

    private ShapeableImageView chestShapeableIV;
    private AppCompatButton startBtn, resetBtn;
    private ProgressBar progressBar, warmUpProgressBar, pushUpsProgressBar, benchProgressBar, dumbbellFlyesProgressBar;

    private LinearLayout warmUpLayout, pushUpLayout, benchLayout, flyesLayout, progressBarsLayout, workoutExplanationLayout;
    private ConstraintLayout timerLayout;

    private static final long START_TIME_IN_MILLIS = 1020000;// 1020000; //17minutes = 17*60000 = 1020000millis

    private Toast toast;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long warmUp = mTimeLeftInMillis - 1000;
    private long pushUps = mTimeLeftInMillis - 180000;
    private long benchPress = pushUps - 180000;
    private long dumbbellFlyes = benchPress - 300000;

    private int warmUpProgress = 0, pushUpProgress = 0, benchProgress = 0, flyesProgress = 0;
    private double warmUpPercentage = 0, pushUpPercentage = 0, benchPercentage = 0, flyesPercentage = 0;
    private Animation right, chest, arms, legs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chest);

        mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        shortBeep = MediaPlayer.create(this, R.raw.shortbeep);
        softBeep = MediaPlayer.create(this, R.raw.softbeep);

        timerValue = findViewById(R.id.timerValue);

        warmUpTxtView = findViewById(R.id.warmUpTv);
        pushUpTxtView = findViewById(R.id.pushUpTv);
        benchTxtView = findViewById(R.id.benchTv);
        flyesTxtView = findViewById(R.id.flyesTv);
        workoutDoneTxtView = findViewById(R.id.workoutDoneTv);
        warmupPercentageTxt = findViewById(R.id.warmupPercentage);
        pushUpPercentageTxt = findViewById(R.id.pushUpPercentage);
        benchPercentageTxt = findViewById(R.id.benchPercentage);
        flyesPercentageTxt = findViewById(R.id.flyesPercentage);


        warmUpImg = findViewById(R.id.warmUpTick);
        pushUpImg = findViewById(R.id.pushUpTick);
        benchImg = findViewById(R.id.benchTick);
        flyesImg = findViewById(R.id.flyesTick);
        chestShapeableIV = findViewById(R.id.chestShapeableIV);
        backArrow = findViewById(R.id.backArrow);

        startBtn = findViewById(R.id.startBtn);
        resetBtn = findViewById(R.id.resetBtn);

        progressBar = findViewById(R.id.progressBar);
        warmUpProgressBar = findViewById(R.id.warmUpProgressBar);
        pushUpsProgressBar = findViewById(R.id.pushUpsProgressBar);
        benchProgressBar = findViewById(R.id.benchProgressBar);
        dumbbellFlyesProgressBar = findViewById(R.id.dumbbellFlyesProgressBar);

        warmUpLayout = findViewById(R.id.warmUpLayout);
        pushUpLayout = findViewById(R.id.pushUpLayout);
        benchLayout = findViewById(R.id.benchLayout);
        flyesLayout = findViewById(R.id.flyesLayout);
        progressBarsLayout = findViewById(R.id.progressBarsLayout);
        workoutExplanationLayout = findViewById(R.id.workoutExplanationLayout);
        timerLayout = findViewById(R.id.timerLayout);


        chest = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.chest);
        right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right);
        arms = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.arms);
        legs = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.legs);
        chestShapeableIV.setAnimation(right);
        progressBarsLayout.setAnimation(chest);
        workoutExplanationLayout.setAnimation(arms);
        timerLayout.setAnimation(legs);

        backArrow.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(getApplicationContext(), WorkoutActivity.class));
        });

        //Custom Toast
        toast = new Toast(getApplicationContext());

        View view1 = getLayoutInflater().inflate(R.layout.custom_toast, this.<ViewGroup>findViewById(R.id.customToast));
        toastTxtView = view1.findViewById(R.id.toastTv);
        toast.setView(view1);
        toast.setDuration(Toast.LENGTH_LONG);

        //toast.setGravity(Gravity.CENTER,0,0);
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.FILL_VERTICAL, 0, 0);

        //Displaying video in video viewer
        //chestVideo = findViewById(R.id.chestWorkoutTutorial);
        /*String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.chest;
        Uri uri = Uri.parse(videoPath);
        chestVideo.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        chestVideo.setMediaController(mediaController);
        mediaController.setAnchorView(chestVideo);

        //Displaying video in full width
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) chestVideo.getLayoutParams();
        params.width = screenWidth;
        params.height = (int) (screenWidth * 0.5625); // Assuming 16:9 aspect ratio
        chestVideo.setLayoutParams(params);*/

        startBtn.setOnClickListener(view -> {
            startStop();
        });

        resetBtn.setOnClickListener(view -> {
            resetTimer();
        });

    }


    private void startTimer(){
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisLeftUntilFinished) {

                mTimeLeftInMillis = millisLeftUntilFinished;
                validateWorkout(millisLeftUntilFinished);
                int progress = (int) (millisLeftUntilFinished/1000);
                progressBar.setMax(1020);
                progressBar.setProgress(progress);

                if (millisLeftUntilFinished <= warmUp && millisLeftUntilFinished > pushUps){
                    //warm up progress bar setup
                    warmUpProgress++;
                    warmUpProgressBar.setMax(180);
                    warmUpProgressBar.setProgress(warmUpProgress);
                }else if(millisLeftUntilFinished <= pushUps && millisLeftUntilFinished > benchPress){
                    //push up progress bar setup
                    pushUpProgress++;
                    pushUpsProgressBar.setMax(180);
                    pushUpsProgressBar.setProgress(pushUpProgress);
                }else if(millisLeftUntilFinished <= benchPress && millisLeftUntilFinished > dumbbellFlyes){
                    benchProgress++;
                    benchProgressBar.setMax(300);
                    benchProgressBar.setProgress(benchProgress);
                }else if(millisLeftUntilFinished <= dumbbellFlyes && millisLeftUntilFinished > 30000) {
                    flyesProgress++;
                    dumbbellFlyesProgressBar.setMax(330);
                    dumbbellFlyesProgressBar.setProgress(flyesProgress);
                }

                /*if (millisLeftUntilFinished + 100 > warmUp && millisLeftUntilFinished - 100 < warmUp) {
                    warmUpTxtView.setVisibility(View.VISIBLE);
                    warmUpLayout.setBackgroundResource(R.drawable.red_layout);
                    toastTxtView.setText("Start warming up");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Start warming up", Toast.LENGTH_LONG).show();
                } else if(millisLeftUntilFinished + 500 > warmUp - 175000 && millisLeftUntilFinished - 500 < warmUp - 175000) {
                    mediaPlayer.start();
                } else if(millisLeftUntilFinished + 500 > pushUps && millisLeftUntilFinished - 500 < pushUps){
                    warmUpTxtView.setVisibility(View.VISIBLE);
                    warmUpTxtView.setText("Warm up is done");
                    warmUpLayout.setBackgroundResource(R.drawable.green_layout);
                    warmUpImg.setVisibility(View.VISIBLE);
                    pushUpLayout.setBackgroundResource(R.drawable.red_layout);
                    pushUpTxtView.setVisibility(View.VISIBLE);
                    toastTxtView.setText("Start push up sets");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Start push up sets", Toast.LENGTH_LONG).show();
                } if(millisLeftUntilFinished + 500 > pushUps - 38000 && millisLeftUntilFinished - 500 < pushUps - 38000) {
                    shortBeep.start();
                } else if(millisLeftUntilFinished + 500 > pushUps-40000 && millisLeftUntilFinished - 500 < pushUps-40000){
                    pushUpTxtView.setVisibility(View.VISIBLE);
                    pushUpTxtView.setText("Rest for 20 seconds...");
                    toastTxtView.setText("Rest 20 seconds");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Rest 20 seconds", Toast.LENGTH_LONG).show();
                } else if(millisLeftUntilFinished + 500 > pushUps-60000 && millisLeftUntilFinished - 500 < pushUps-60000){
                    pushUpTxtView.setVisibility(View.VISIBLE);
                    pushUpTxtView.setText("Set 2 of push ups...");
                    toastTxtView.setText("Set 2 of push ups");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Set 2 of push ups", Toast.LENGTH_LONG).show();
                } if(millisLeftUntilFinished + 500 > pushUps - 98000 && millisLeftUntilFinished - 500 < pushUps - 98000) {
                    shortBeep.start();
                } else if(millisLeftUntilFinished + 500 > pushUps-100000 && millisLeftUntilFinished - 500 < pushUps-100000){
                    pushUpTxtView.setVisibility(View.VISIBLE);
                    pushUpTxtView.setText("Rest for 20 seconds...");
                    toastTxtView.setText("Rest 20 seconds");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Rest 20 seconds", Toast.LENGTH_LONG).show();
                } else if(millisLeftUntilFinished + 500 > pushUps-120000 && millisLeftUntilFinished - 500 < pushUps-120000){
                    pushUpTxtView.setVisibility(View.VISIBLE);
                    pushUpTxtView.setText("Set 3 of push ups...");
                    toastTxtView.setText("Set 3 of push ups");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Set 3 of push ups", Toast.LENGTH_LONG).show();
                } if(millisLeftUntilFinished + 500 > pushUps - 158000 && millisLeftUntilFinished - 500 < pushUps - 158000) {
                    shortBeep.start();
                } else if(millisLeftUntilFinished + 500 > pushUps-160000 && millisLeftUntilFinished - 500 < pushUps-160000){
                    pushUpTxtView.setVisibility(View.VISIBLE);
                    pushUpTxtView.setText("Rest for 20 seconds...");
                    toastTxtView.setText("Rest 20 seconds");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Rest 20 seconds", Toast.LENGTH_LONG).show();
                } if(millisLeftUntilFinished + 500 > pushUps - 176000 && millisLeftUntilFinished - 500 < pushUps - 176000) {
                    mediaPlayer.start();
                } else if(millisLeftUntilFinished + 500 > benchPress && millisLeftUntilFinished - 500 < benchPress){
                    pushUpTxtView.setVisibility(View.VISIBLE);
                    pushUpTxtView.setText("Push ups are done");
                    pushUpImg.setVisibility(View.VISIBLE);
                    pushUpLayout.setBackgroundResource(R.drawable.green_layout);
                    benchTxtView.setVisibility(View.VISIBLE);
                    benchLayout.setBackgroundResource(R.drawable.red_layout);
                    toastTxtView.setText("Start dumbbell bench press sets");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Start dumbbell bench press sets", Toast.LENGTH_LONG).show();
                } if(millisLeftUntilFinished + 500 > benchPress - 68000 && millisLeftUntilFinished - 500 < benchPress - 68000) {
                    shortBeep.start();
                } else if(millisLeftUntilFinished + 500 > benchPress-70000 && millisLeftUntilFinished - 500 < benchPress-70000){
                    benchTxtView.setVisibility(View.VISIBLE);
                    benchTxtView.setText("Rest for 30 seconds...");
                    toastTxtView.setText("Rest 30 seconds");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Rest 30 seconds", Toast.LENGTH_LONG).show();
                } else if(millisLeftUntilFinished + 500 > benchPress-100000 && millisLeftUntilFinished - 500 < benchPress-100000){
                    benchTxtView.setVisibility(View.VISIBLE);
                    benchTxtView.setText("Set 2 of bench press...");
                    toastTxtView.setText("Set 2 of bench press");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Set 2 of bench press", Toast.LENGTH_LONG).show();
                } if(millisLeftUntilFinished + 100 > benchPress - 168000 && millisLeftUntilFinished - 500 < benchPress - 168000) {
                    shortBeep.start();
                } else if(millisLeftUntilFinished + 500 > benchPress-170000 && millisLeftUntilFinished - 500 < benchPress-170000){
                    benchTxtView.setVisibility(View.VISIBLE);
                    benchTxtView.setText("Rest for 30 seconds...");
                    toastTxtView.setText("Rest 30 seconds");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Rest 30 seconds", Toast.LENGTH_LONG).show();
                } else if(millisLeftUntilFinished + 500 > benchPress-200000 && millisLeftUntilFinished - 500 < benchPress-200000){
                    benchTxtView.setVisibility(View.VISIBLE);
                    benchTxtView.setText("Set 3 of bench press...");
                    toastTxtView.setText("Set 3 of bench press");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Set 3 of bench press", Toast.LENGTH_LONG).show();
                } if(millisLeftUntilFinished + 500 > benchPress - 268000 && millisLeftUntilFinished - 500 < benchPress - 268000) {
                    shortBeep.start();
                } else if(millisLeftUntilFinished + 500 > benchPress-270000 && millisLeftUntilFinished - 500 < benchPress-270000){
                    benchTxtView.setVisibility(View.VISIBLE);
                    benchTxtView.setText("Rest for 30 seconds...");
                    toastTxtView.setText("Rest 30 seconds");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Rest 30 seconds", Toast.LENGTH_LONG).show();
                } if(millisLeftUntilFinished + 500 > benchPress - 296000 && millisLeftUntilFinished - 500 < benchPress - 296000) {
                    mediaPlayer.start();
                } else if(millisLeftUntilFinished + 500 > dumbbellFlyes && millisLeftUntilFinished - 500 < dumbbellFlyes){
                    benchTxtView.setVisibility(View.VISIBLE);
                    benchTxtView.setText("Bench press done");
                    benchImg.setVisibility(View.VISIBLE);
                    benchLayout.setBackgroundResource(R.drawable.green_layout);
                    flyesTxtView.setVisibility(View.VISIBLE);
                    flyesLayout.setBackgroundResource(R.drawable.red_layout);
                    toastTxtView.setText("Start dumbbell flyes sets");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Start dumbbell flyes sets", Toast.LENGTH_LONG).show();
                } if(millisLeftUntilFinished + 500 > dumbbellFlyes - 88000 && millisLeftUntilFinished - 500 < dumbbellFlyes - 88000) {
                    shortBeep.start();
                } else if (millisLeftUntilFinished + 500 > dumbbellFlyes- 90000 && millisLeftUntilFinished - 500 < dumbbellFlyes-90000) {
                    flyesTxtView.setVisibility(View.VISIBLE);
                    flyesTxtView.setText("Rest for 30 seconds...");
                    toastTxtView.setText("Rest 30 seconds");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Rest 30 seconds", Toast.LENGTH_LONG).show();
                } else if(millisLeftUntilFinished + 500 > dumbbellFlyes- 120000 && millisLeftUntilFinished - 500 < dumbbellFlyes-120000) {
                    flyesTxtView.setVisibility(View.VISIBLE);
                    flyesTxtView.setText("Set 2 of dumbbell flyes...");
                    toastTxtView.setText("Set 2 of dumbbell flyes");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Set 2 of dumbbell flyes", Toast.LENGTH_LONG).show();
                } if(millisLeftUntilFinished + 500 > dumbbellFlyes - 208000 && millisLeftUntilFinished - 500 < dumbbellFlyes - 208000) {
                    shortBeep.start();
                } else if(millisLeftUntilFinished + 500 > dumbbellFlyes- 210000 && millisLeftUntilFinished - 500 < dumbbellFlyes-210000){
                    flyesTxtView.setVisibility(View.VISIBLE);
                    flyesTxtView.setText("Rest for 30 seconds...");
                    toastTxtView.setText("Rest 30 seconds");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Rest 30 seconds", Toast.LENGTH_LONG).show();
                } else if(millisLeftUntilFinished + 500 > dumbbellFlyes- 240000 && millisLeftUntilFinished - 500 < dumbbellFlyes-240000) {
                    flyesTxtView.setVisibility(View.VISIBLE);
                    flyesTxtView.setText("Set 3 of dumbbell flyes...");
                    toastTxtView.setText("Set 3 of dumbbell flyes");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Set 3 of dumbbell flyes", Toast.LENGTH_LONG).show();
                } if(millisLeftUntilFinished + 500 > dumbbellFlyes - 328000 && millisLeftUntilFinished - 500 < dumbbellFlyes - 328000) {
                    shortBeep.start();
                } else if(millisLeftUntilFinished + 500 > dumbbellFlyes- 330000 && millisLeftUntilFinished - 500 < dumbbellFlyes-330000){
                    flyesTxtView.setVisibility(View.VISIBLE);
                    flyesTxtView.setText("Rest for 30 seconds...");
                    toastTxtView.setText("Rest 30 seconds");
                    toast.show();
                    //Toast.makeText(ChestActivity.this, "Rest 30 seconds", Toast.LENGTH_LONG).show();
                } if(millisLeftUntilFinished + 500 > dumbbellFlyes - 356000 && millisLeftUntilFinished - 500 < dumbbellFlyes - 356000) {
                    mediaPlayer.start();
                }else if(millisLeftUntilFinished + 500 > dumbbellFlyes - 359000 && millisLeftUntilFinished - 500 < dumbbellFlyes - 359000){
                    flyesTxtView.setVisibility(View.VISIBLE);
                    flyesTxtView.setText("Dumbbell flyes done");
                    flyesImg.setVisibility(View.VISIBLE);
                    flyesLayout.setBackgroundResource(R.drawable.green_layout);
                    workoutDoneTxtView.setVisibility(View.VISIBLE);
                    toastTxtView.setText("See you next time. Good job!");
                    toast.show();
                    //Toast.makeText(getApplicationContext(), "Workout is done", Toast.LENGTH_SHORT).show();
                }*/
                updateCountDownText();
                updatePercentageText();
            }

            @Override
            public void onFinish() {
                //progressBar.setProgress(0);
                toastTxtView.setText("See you next time. Good job!");
                toast.show();
                resetTimer();
                mTimerRunning = false;
                //Toast.makeText(getApplicationContext(), "See you next time.", Toast.LENGTH_LONG).show();
            }
        }.start();
        startBtn.setText("Pause");
        resetBtn.setVisibility(View.GONE);
        workoutDoneTxtView.setVisibility(View.GONE);
        mTimerRunning = true;
    }

    private void startStop(){
        if(mTimerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void stopTimer(){
        countDownTimer.cancel();
        startBtn.setText("Resume");
        resetBtn.setVisibility(View.VISIBLE);
        mTimerRunning = false;

    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        resetBtn.setVisibility(View.GONE);
        warmUpTxtView.setVisibility(View.GONE);
        warmUpImg.setVisibility(View.GONE);
        warmUpLayout.setBackgroundColor(0);
        pushUpTxtView.setVisibility(View.GONE);
        pushUpImg.setVisibility(View.GONE);
        pushUpLayout.setBackgroundColor(0);
        benchTxtView.setVisibility(View.GONE);
        benchImg.setVisibility(View.GONE);
        benchLayout.setBackgroundColor(0);
        flyesLayout.setBackgroundColor(0);
        flyesTxtView.setVisibility(View.GONE);
        flyesImg.setVisibility(View.GONE);
        // Progress Bars RESET
        progressBar.setProgress(0);
        warmUpProgressBar.setProgress(0);
        warmUpProgressBar.setProgressDrawable(getApplicationContext().getDrawable(R.drawable.custom_horizontal_progress_bar));
        pushUpsProgressBar.setProgressDrawable(getApplicationContext().getDrawable(R.drawable.custom_horizontal_progress_bar));
        pushUpsProgressBar.setProgress(0);
        benchProgressBar.setProgress(0);
        benchProgressBar.setProgressDrawable(getApplicationContext().getDrawable(R.drawable.custom_horizontal_progress_bar));
        dumbbellFlyesProgressBar.setProgress(0);
        dumbbellFlyesProgressBar.setProgressDrawable(getApplicationContext().getDrawable(R.drawable.custom_horizontal_progress_bar));
        warmUpProgress = 0;
        pushUpProgress = 0;
        benchProgress = 0;
        flyesProgress = 0;
        warmUpPercentage = 0;
        pushUpPercentage = 0;
        benchPercentage = 0;
        flyesPercentage = 0;
        warmupPercentageTxt.setText("warm up");
        warmupPercentageTxt.setTextColor(getApplicationContext().getColor(R.color.logoBg));
        pushUpPercentageTxt.setText("push ups");
        pushUpPercentageTxt.setTextColor(getApplicationContext().getColor(R.color.logoBg));
        benchPercentageTxt.setText("bench press");
        benchPercentageTxt.setTextColor(getApplicationContext().getColor(R.color.logoBg));
        flyesPercentageTxt.setText("dumbbell fly");
        flyesPercentageTxt.setTextColor(getApplicationContext().getColor(R.color.logoBg));
        startBtn.setText("Start Workout");
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis/1000)/60;
        int seconds = (int) (mTimeLeftInMillis/1000)%60;
       /* if(minutes >= 14 && minutes <= 17 ){
            chestShapeableIV.setImageResource(R.drawable.warmup);
            chestShapeableIV.setAnimation(chest);
            warmUpPercentage = warmUpPercentage + 0.5555;
            String percentageTxt = String.format(Locale.getDefault(),"%.0f%%", warmUpPercentage);
            warmupPercentageTxt.setText(percentageTxt);
        }else if (minutes >= 11 && minutes < 14){
            warmupPercentageTxt.setText("Warm up is finished");
            warmupPercentageTxt.setTextColor(getResources().getColor(R.color.white));
            chestShapeableIV.setImageResource(R.drawable.push_ups);
            pushUpPercentage = pushUpPercentage + 0.5555;
            String percentageTxt = String.format(Locale.getDefault(),"%.0f%%", pushUpPercentage);
            pushUpPercentageTxt.setText(percentageTxt);
        }else if(minutes >= 6 && minutes < 11) {
            pushUpPercentageTxt.setText("Push ups are finished");
            chestShapeableIV.setImageResource(R.drawable.bench_press);
            pushUpPercentageTxt.setTextColor(getResources().getColor(R.color.white));
            benchPercentage = benchPercentage + 0.3333;
            String percentageTxt = String.format(Locale.getDefault(),"%.0f%%", benchPercentage);
            benchPercentageTxt.setText(percentageTxt);
        }else if(minutes >= 0 && minutes < 6){
            benchPercentageTxt.setText("Bench press is finished");
            benchPercentageTxt.setTextColor(getResources().getColor(R.color.white));
            chestShapeableIV.setImageResource(R.drawable.dumbbell_fly);
            flyesPercentage = flyesPercentage + 0.27777;
            String percentageTxt = String.format(Locale.getDefault(),"%.0f%%", flyesPercentage);
            flyesPercentageTxt.setText(percentageTxt);
        }else if(minutes == 0 && seconds<=30){
            flyesPercentageTxt.setText("Dumbbell flyes are finished");
            flyesPercentageTxt.setTextColor(getResources().getColor(R.color.white));
        }*/

        String timeLeft = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        timerValue.setText(timeLeft);

    }
    private void updatePercentageText() {
        int minutes = (int) (mTimeLeftInMillis/1000)/60;
        int seconds = (int) (mTimeLeftInMillis/1000)%60;
        if(minutes >= 14 && minutes <=17 ){
            chestShapeableIV.setImageResource(R.drawable.warmup);
            warmUpPercentage = warmUpPercentage + 0.5555;
            String percentageTxt = String.format(Locale.getDefault(), "%.0f%%", warmUpPercentage);
            warmupPercentageTxt.setText(percentageTxt);
        } else if (minutes >= 11 && minutes < 14){
            chestShapeableIV.setImageResource(R.drawable.push_ups);
            warmupPercentageTxt.setText("Warm up is finished");
            warmupPercentageTxt.setTextColor(getApplicationContext().getColor(R.color.white));
            pushUpPercentage = pushUpPercentage + 0.5555;
            String percentageTxt = String.format( Locale.getDefault(), "%.0f%%", pushUpPercentage);
            pushUpPercentageTxt.setText(percentageTxt);
        } else if(minutes >= 6 && minutes < 11) {
            pushUpPercentageTxt.setText("Push ups are finished");
            chestShapeableIV.setImageResource(R.drawable.bench_press);
            pushUpPercentageTxt.setTextColor(getApplicationContext().getColor(R.color.white));
            benchPercentage = benchPercentage + 0.3333;
            String percentageTxt = String.format(Locale.getDefault(),"%.0f%%", benchPercentage);
            benchPercentageTxt.setText(percentageTxt);
        } else if(minutes > 0 && minutes < 6 || minutes == 0 && seconds > 30){
            benchPercentageTxt.setText("Bench press is finished");
            benchPercentageTxt.setTextColor(getApplicationContext().getColor(R.color.white));
            chestShapeableIV.setImageResource(R.drawable.dumbbell_fly);
            flyesPercentage = flyesPercentage + 0.303;
            String percentageTxt = String.format(Locale.getDefault(),"%.0f%%", flyesPercentage);
            flyesPercentageTxt.setText(percentageTxt);
        } else if(minutes == 0){
            flyesPercentageTxt.setText("Dumbbell flyes are finished");
            flyesPercentageTxt.setTextColor(getApplicationContext().getColor(R.color.white));
        }
    }

    private void validateWorkout(long millisLeftUntilFinished){
        if (millisLeftUntilFinished + 100 > warmUp && millisLeftUntilFinished - 100 < warmUp) {
            warmUpProgressBar.setProgress(0);
            warmUpTxtView.setVisibility(View.VISIBLE);
            warmUpTxtView.setText("You should be warming up during this time");
            warmUpLayout.setBackgroundResource(R.drawable.red_layout);
            toastTxtView.setText("Start warming up");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Start warming up", Toast.LENGTH_LONG).show();
        } else if(millisLeftUntilFinished + 500 > warmUp - 175000 && millisLeftUntilFinished - 500 < warmUp - 175000) {
            mediaPlayer.start();
        } else if(millisLeftUntilFinished + 500 > pushUps && millisLeftUntilFinished - 500 < pushUps){
            warmUpProgressBar.setProgressDrawable(getApplicationContext().getDrawable(custom_horizontal_progress_bar_done));
            warmUpTxtView.setVisibility(View.VISIBLE);
            warmUpTxtView.setText("Warm up is done");
            warmUpLayout.setBackgroundResource(R.drawable.green_layout);
            warmUpImg.setVisibility(View.VISIBLE);
            pushUpLayout.setBackgroundResource(R.drawable.red_layout);
            pushUpTxtView.setVisibility(View.VISIBLE);
            pushUpTxtView.setText("You should be doing push ups during this time");
            toastTxtView.setText("Start push up sets");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Start push up sets", Toast.LENGTH_LONG).show();
        } if(millisLeftUntilFinished + 500 > pushUps - 38000 && millisLeftUntilFinished - 500 < pushUps - 38000) {
            shortBeep.start();
        } else if(millisLeftUntilFinished + 500 > pushUps-40000 && millisLeftUntilFinished - 500 < pushUps-40000){
            pushUpTxtView.setVisibility(View.VISIBLE);
            pushUpTxtView.setText("Rest for 20 seconds...");
            toastTxtView.setText("Rest 20 seconds");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Rest 20 seconds", Toast.LENGTH_LONG).show();
        } else if(millisLeftUntilFinished + 500 > pushUps-60000 && millisLeftUntilFinished - 500 < pushUps-60000){
            pushUpTxtView.setVisibility(View.VISIBLE);
            pushUpTxtView.setText("Set 2 of push ups...");
            toastTxtView.setText("Set 2 of push ups");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Set 2 of push ups", Toast.LENGTH_LONG).show();
        } if(millisLeftUntilFinished + 500 > pushUps - 98000 && millisLeftUntilFinished - 500 < pushUps - 98000) {
            shortBeep.start();
        } else if(millisLeftUntilFinished + 500 > pushUps-100000 && millisLeftUntilFinished - 500 < pushUps-100000){
            pushUpTxtView.setVisibility(View.VISIBLE);
            pushUpTxtView.setText("Rest for 20 seconds...");
            toastTxtView.setText("Rest 20 seconds");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Rest 20 seconds", Toast.LENGTH_LONG).show();
        } else if(millisLeftUntilFinished + 500 > pushUps-120000 && millisLeftUntilFinished - 500 < pushUps-120000){
            pushUpTxtView.setVisibility(View.VISIBLE);
            pushUpTxtView.setText("Set 3 of push ups...");
            toastTxtView.setText("Set 3 of push ups");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Set 3 of push ups", Toast.LENGTH_LONG).show();
        } if(millisLeftUntilFinished + 500 > pushUps - 158000 && millisLeftUntilFinished - 500 < pushUps - 158000) {
            shortBeep.start();
        } else if(millisLeftUntilFinished + 500 > pushUps-160000 && millisLeftUntilFinished - 500 < pushUps-160000){
            pushUpTxtView.setVisibility(View.VISIBLE);
            pushUpTxtView.setText("Rest for 20 seconds...");
            toastTxtView.setText("Rest 20 seconds");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Rest 20 seconds", Toast.LENGTH_LONG).show();
        } if(millisLeftUntilFinished + 500 > pushUps - 176000 && millisLeftUntilFinished - 500 < pushUps - 176000) {
            mediaPlayer.start();
        } else if(millisLeftUntilFinished + 500 > benchPress && millisLeftUntilFinished - 500 < benchPress){
            pushUpsProgressBar.setProgressDrawable(getApplicationContext().getDrawable(custom_horizontal_progress_bar_done));
            pushUpTxtView.setVisibility(View.VISIBLE);
            pushUpTxtView.setText("Push ups are done");
            pushUpImg.setVisibility(View.VISIBLE);
            pushUpLayout.setBackgroundResource(R.drawable.green_layout);
            benchTxtView.setVisibility(View.VISIBLE);
            benchTxtView.setText("You should be doing bench press during this time");
            benchLayout.setBackgroundResource(R.drawable.red_layout);
            toastTxtView.setText("Start dumbbell bench press sets");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Start dumbbell bench press sets", Toast.LENGTH_LONG).show();
        } if(millisLeftUntilFinished + 500 > benchPress - 68000 && millisLeftUntilFinished - 500 < benchPress - 68000) {
            shortBeep.start();
        } else if(millisLeftUntilFinished + 500 > benchPress-70000 && millisLeftUntilFinished - 500 < benchPress-70000){
            benchTxtView.setVisibility(View.VISIBLE);
            benchTxtView.setText("Rest for 30 seconds...");
            toastTxtView.setText("Rest 30 seconds");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Rest 30 seconds", Toast.LENGTH_LONG).show();
        } else if(millisLeftUntilFinished + 500 > benchPress-100000 && millisLeftUntilFinished - 500 < benchPress-100000){
            benchTxtView.setVisibility(View.VISIBLE);
            benchTxtView.setText("Set 2 of bench press...");
            toastTxtView.setText("Set 2 of bench press");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Set 2 of bench press", Toast.LENGTH_LONG).show();
        } if(millisLeftUntilFinished + 100 > benchPress - 168000 && millisLeftUntilFinished - 500 < benchPress - 168000) {
            shortBeep.start();
        } else if(millisLeftUntilFinished + 500 > benchPress-170000 && millisLeftUntilFinished - 500 < benchPress-170000){
            benchTxtView.setVisibility(View.VISIBLE);
            benchTxtView.setText("Rest for 30 seconds...");
            toastTxtView.setText("Rest 30 seconds");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Rest 30 seconds", Toast.LENGTH_LONG).show();
        } else if(millisLeftUntilFinished + 500 > benchPress-200000 && millisLeftUntilFinished - 500 < benchPress-200000){
            benchTxtView.setVisibility(View.VISIBLE);
            benchTxtView.setText("Set 3 of bench press...");
            toastTxtView.setText("Set 3 of bench press");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Set 3 of bench press", Toast.LENGTH_LONG).show();
        } if(millisLeftUntilFinished + 500 > benchPress - 268000 && millisLeftUntilFinished - 500 < benchPress - 268000) {
            shortBeep.start();
        } else if(millisLeftUntilFinished + 500 > benchPress-270000 && millisLeftUntilFinished - 500 < benchPress-270000){
            benchTxtView.setVisibility(View.VISIBLE);
            benchTxtView.setText("Rest for 30 seconds...");
            toastTxtView.setText("Rest 30 seconds");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Rest 30 seconds", Toast.LENGTH_LONG).show();
        } if(millisLeftUntilFinished + 500 > benchPress - 296000 && millisLeftUntilFinished - 500 < benchPress - 296000) {
            mediaPlayer.start();
        } else if(millisLeftUntilFinished + 500 > dumbbellFlyes && millisLeftUntilFinished - 500 < dumbbellFlyes){
            benchProgressBar.setProgressDrawable(getApplicationContext().getDrawable(custom_horizontal_progress_bar_done));
            benchTxtView.setVisibility(View.VISIBLE);
            benchTxtView.setText("Bench press done");
            benchImg.setVisibility(View.VISIBLE);
            benchLayout.setBackgroundResource(R.drawable.green_layout);
            flyesTxtView.setVisibility(View.VISIBLE);
            flyesTxtView.setText("You should be doing dumbbell flyes during this time");
            flyesLayout.setBackgroundResource(R.drawable.red_layout);
            toastTxtView.setText("Start dumbbell flyes sets");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Start dumbbell flyes sets", Toast.LENGTH_LONG).show();
        } if(millisLeftUntilFinished + 500 > dumbbellFlyes - 88000 && millisLeftUntilFinished - 500 < dumbbellFlyes - 88000) {
            shortBeep.start();
        } else if (millisLeftUntilFinished + 500 > dumbbellFlyes- 90000 && millisLeftUntilFinished - 500 < dumbbellFlyes-90000) {
            flyesTxtView.setVisibility(View.VISIBLE);
            flyesTxtView.setText("Rest for 30 seconds...");
            toastTxtView.setText("Rest 30 seconds");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Rest 30 seconds", Toast.LENGTH_LONG).show();
        } else if(millisLeftUntilFinished + 500 > dumbbellFlyes- 120000 && millisLeftUntilFinished - 500 < dumbbellFlyes-120000) {
            flyesTxtView.setVisibility(View.VISIBLE);
            flyesTxtView.setText("Set 2 of dumbbell flyes...");
            toastTxtView.setText("Set 2 of dumbbell flyes");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Set 2 of dumbbell flyes", Toast.LENGTH_LONG).show();
        } if(millisLeftUntilFinished + 500 > dumbbellFlyes - 208000 && millisLeftUntilFinished - 500 < dumbbellFlyes - 208000) {
            shortBeep.start();
        } else if(millisLeftUntilFinished + 500 > dumbbellFlyes- 210000 && millisLeftUntilFinished - 500 < dumbbellFlyes-210000){
            flyesTxtView.setVisibility(View.VISIBLE);
            flyesTxtView.setText("Rest for 30 seconds...");
            toastTxtView.setText("Rest 30 seconds");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Rest 30 seconds", Toast.LENGTH_LONG).show();
        } else if(millisLeftUntilFinished + 500 > dumbbellFlyes- 240000 && millisLeftUntilFinished - 500 < dumbbellFlyes-240000) {
            flyesTxtView.setVisibility(View.VISIBLE);
            flyesTxtView.setText("Set 3 of dumbbell flyes...");
            toastTxtView.setText("Set 3 of dumbbell flyes");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Set 3 of dumbbell flyes", Toast.LENGTH_LONG).show();
        } if(millisLeftUntilFinished + 500 > dumbbellFlyes - 328000 && millisLeftUntilFinished - 500 < dumbbellFlyes - 328000) {
            shortBeep.start();
        } else if(millisLeftUntilFinished + 500 > dumbbellFlyes- 330000 && millisLeftUntilFinished - 500 < dumbbellFlyes-330000){
            dumbbellFlyesProgressBar.setProgressDrawable(getApplicationContext().getDrawable(custom_horizontal_progress_bar_done));
            flyesTxtView.setVisibility(View.VISIBLE);
            flyesTxtView.setText("Rest for 30 seconds...");
            toastTxtView.setText("Rest 30 seconds");
            toast.show();
            //Toast.makeText(ChestActivity.this, "Rest 30 seconds", Toast.LENGTH_LONG).show();
        } if(millisLeftUntilFinished + 500 > dumbbellFlyes - 356000 && millisLeftUntilFinished - 500 < dumbbellFlyes - 356000) {
            mediaPlayer.start();
        }else if(millisLeftUntilFinished + 500 > dumbbellFlyes - 359000 && millisLeftUntilFinished - 500 < dumbbellFlyes - 359000){
            flyesTxtView.setVisibility(View.VISIBLE);
            flyesTxtView.setText("Dumbbell flyes done");
            flyesImg.setVisibility(View.VISIBLE);
            flyesLayout.setBackgroundResource(R.drawable.green_layout);
            workoutDoneTxtView.setVisibility(View.VISIBLE);
            toastTxtView.setText("See you next time. Good job!");
            toast.show();
            //Toast.makeText(getApplicationContext(), "Workout is done", Toast.LENGTH_SHORT).show();
        }
    }
}