package com.shqiperon.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class NutritionActivity extends AppCompatActivity {

    private ViewPager2 nutritionSlide;
    private List<Image> imageList;
    private ImageAdapter adapter;
    private ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        nutritionSlide = findViewById(R.id.nutritionSlideViewPager);
        imageList = new ArrayList<>();


        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(view -> {
            finish();
        });

        imageList.add(new Image(R.drawable.plate_one));
        imageList.add(new Image(R.drawable.plate_two));
        imageList.add(new Image(R.drawable.plate_three));
        imageList.add(new Image(R.drawable.plate_four));
        imageList.add(new Image(R.drawable.plate_five));
        imageList.add(new Image(R.drawable.plate_six));
        imageList.add(new Image(R.drawable.plate_seven));
        imageList.add(new Image(R.drawable.plate_eight));

        adapter = new ImageAdapter(imageList, nutritionSlide);
        nutritionSlide.setAdapter(adapter);

        nutritionSlide.setOffscreenPageLimit(3);
        nutritionSlide.setClipChildren(false);
        nutritionSlide.setClipToPadding(false);
        nutritionSlide.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.14f);
            }
        });

        nutritionSlide.setPageTransformer(transformer);
    }
}