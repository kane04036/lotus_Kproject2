package com.example.lotus_kproject2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class LongReviewRecyclerViewAdapter extends RecyclerView.Adapter<LongReviewRecyclerViewAdapter.ViewHolder> {
    ArrayList<String> reviewIdArray = new ArrayList<>();
    ArrayList<String> movieCodeArray = new ArrayList<>();
    ArrayList<Integer> starArray = new ArrayList<>();
    ArrayList<String> userIdArray = new ArrayList<>();
    ArrayList<String> titleArray = new ArrayList<>();
    ArrayList<String> writingArray = new ArrayList<>();
    Context context;
    public LongReviewRecyclerViewAdapter(Context context, ArrayList reviewIdArray, ArrayList movieCodeArray,
                                         ArrayList starArray, ArrayList userIdArray, ArrayList titleArray, ArrayList writingArray){
        this.context = context;
        this.reviewIdArray = reviewIdArray;
        this.movieCodeArray = movieCodeArray;
        this.starArray = starArray;
        this.userIdArray = userIdArray;
        this.titleArray = titleArray;
        this.writingArray = writingArray;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.long_review_item, parent, false);
        return new LongReviewRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ratingBar.setNumStars(starArray.get(position));
        holder.tvLongReivewTitle.setText(titleArray.get(position));
    }


    @Override
    public int getItemCount() {
        return 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        MaterialRatingBar ratingBar;
        TextView tvNicknameLongReview, tvMbti, tvLongReivewTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.ratingBarInLongReview);
            tvNicknameLongReview = itemView.findViewById(R.id.tvNicknameLongReview3);
            tvMbti = itemView.findViewById(R.id.tvMbtiInLongReview);
            tvLongReivewTitle = itemView.findViewById(R.id.tvLongReviewTitle);
        }
    }
}
