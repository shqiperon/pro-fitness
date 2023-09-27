package com.shqiperon.profitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Iterator;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;

    private Animation one, two;

    private int dotsCount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.dotsLayout);

        one = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.one);
        two = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.two);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);


        //addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        dotsCount = sliderAdapter.getCount();
        dots = new ImageView[dotsCount];

        //Notification section
        NotificationManagerCompat.from(this).cancel(0);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            //custom sound for notification
            Uri sound = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/softbeep.mp3" );
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                    .setUsage(AudioAttributes. USAGE_ALARM )
                    .build() ;

            NotificationChannel notificationChannel = new NotificationChannel("pfNotification", "pfNotification", NotificationManager. IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);

            notificationChannel.enableLights( true ) ;
            notificationChannel.setLightColor(Color. RED ) ;
            notificationChannel.enableVibration( true ) ;
            notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
            notificationChannel.setSound(sound , audioAttributes) ;
            assert manager != null;


            manager.createNotificationChannel(notificationChannel);

            notificationChannel.setSound(sound, audioAttributes);

        }

        //check if app is closed or not
        AppController.getInstance().setOnVisibilityChangeListener(new AppController.ValueChangeListener() {
            @Override
            public void onChanged(Boolean value) {
                Log.d("isAppInBackground", String.valueOf(value));
                if(value == true) {
                    startAlarm();
                }
            }
        });

        //------------------------------------------------------------------------------------

        for(int i=0; i<dotsCount; i++){
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8,0,8,0);
            mDotLayout.addView(dots[i],params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));

        mSlideViewPager.startAnimation(one);
        mDotLayout.startAnimation(two);

    }

    /*@Override
    protected void onPause() {
        super.onPause();
        startAlarm();
    }*/

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for(int i=0; i<dotsCount; i++){
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));
            }
            dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void startAlarm() {
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        long triggerTime = SystemClock.elapsedRealtime() + 15000; // 15 seconds
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
    }

}