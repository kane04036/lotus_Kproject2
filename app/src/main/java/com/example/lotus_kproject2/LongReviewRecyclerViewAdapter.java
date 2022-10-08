package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
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
    ArrayList<String> userIdArray = new ArrayList<>();
    ArrayList<String> titleArray = new ArrayList<>();
    ArrayList<String> writingArray = new ArrayList<>();
    ArrayList<String> movieNameArray = new ArrayList<>();

    Context context;
    public LongReviewRecyclerViewAdapter(Context context, ArrayList reviewIdArray, ArrayList movieCodeArray,
                                          ArrayList userIdArray, ArrayList titleArray, ArrayList movieNameArray){
        this.context = context;
        this.reviewIdArray = reviewIdArray;
        this.movieCodeArray = movieCodeArray;
        this.userIdArray = userIdArray;
        this.titleArray = titleArray;
        this.movieNameArray = movieNameArray;
        Log.d(TAG, "LongReviewRecyclerViewAdapter: create");

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_searchresult, parent, false);
        return new LongReviewRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: bind");
        holder.tvMovTitle.setText(movieNameArray.get(position));
        holder.tvReviewTitle.setText(titleArray.get(position));
        Log.d(TAG, "onBindViewHolder: title:"+titleArray.get(position));
    }


    @Override
    public int getItemCount() {
        return reviewIdArray.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
//        MaterialRatingBar ratingBar;
//        TextView tvNicknameLongReview, tvMbti, tvLongReivewTitle;
        TextView tvMovTitle, tvReviewTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovTitle = itemView.findViewById(R.id.tvMovTitle);
            tvReviewTitle = itemView.findViewById(R.id.tvReviewTitle);
        }
    }
}
