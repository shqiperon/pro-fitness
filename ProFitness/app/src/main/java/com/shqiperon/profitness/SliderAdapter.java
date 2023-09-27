package com.shqiperon.profitness;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
        R.drawable.workout,
        R.drawable.nutrition
    };

    public String[] slide_headings = {
          "Workouts",
          "Nutrition"
    };

    public String[] slide_subtitles = {
            "Here you can find different exercises, for different body parts",
            "Here you can find different recipes, depending on your nutrition needs"
    };



    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ShapeableImageView slideImageView  = (ShapeableImageView) view.findViewById(R.id.slideImageView);
        AppCompatButton slideButton = (AppCompatButton) view.findViewById(R.id.slideBtn);
        TextView slideTextView = (TextView) view.findViewById(R.id.slideTextView);
        TextView slideBelowTV = (TextView) view.findViewById(R.id.slideBelowTV);

        slideImageView.setImageResource(slide_images[position]);
        slideTextView.setText(slide_headings[position]);
        slideBelowTV.setText(slide_subtitles[position]);

        if (position == 0){
            slideButton.setOnClickListener(view1 -> {
                view1.getContext().startActivity(new Intent(context.getApplicationContext(),WorkoutActivity.class));
            });
        }else {
            slideButton.setOnClickListener(view1 -> {
                view1.getContext().startActivity(new Intent(context.getApplicationContext(),NutritionActivity.class));
            });
        }

        container.addView(view);

        return  view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
