package com.shqiperon.profitness;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Image> imageList;
    private ViewPager2 viewPager2;

    public ImageAdapter(List<Image> imageList, ViewPager2 viewPager2){
        this.imageList = imageList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutrition_slide_layout,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.circleImageView.setImageResource(imageList.get(position).getImage());

        if(position == imageList.size() - 2){
            imageList.addAll(imageList);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.nutritionCircleImg);
        }
    }

}
