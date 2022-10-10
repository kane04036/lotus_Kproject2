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
import java.util.concurrent.BlockingDeque;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ShortReviewInDetailRecyclerViewAdapter extends RecyclerView.Adapter<ShortReviewInDetailRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> nicknameArray = new ArrayList<>();
    private ArrayList<String> mbtiArray = new ArrayList<>();
    private ArrayList<String> writingArray = new ArrayList<>();
    private ArrayList<Double> starArray = new ArrayList<>();
    private ArrayList<String> thumbUpArray = new ArrayList<>();
    private ArrayList<String> movCodeArray = new ArrayList<>();
    private ArrayList<String> writingIdArray = new ArrayList<>();
    private ArrayList<String> userIdArray = new ArrayList<>();

    private Context context;


    public ShortReviewInDetailRecyclerViewAdapter(Context context, ArrayList mbtiArray, ArrayList nicknameArray, ArrayList starArray,
                                                  ArrayList writingArray, ArrayList thumbUpArray, ArrayList movCodeArray, ArrayList writingIdArray, ArrayList userIdArray) {
        this.mbtiArray = mbtiArray;
        this.nicknameArray = nicknameArray;
        this.writingArray = writingArray;
        this.starArray = starArray;
        this.thumbUpArray = thumbUpArray;
        this.movCodeArray = movCodeArray;
        this.writingIdArray = writingIdArray;
        this.userIdArray = userIdArray;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.short_review_in_detail_item, parent, false);
        return new ShortReviewInDetailRecyclerViewAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMbti.setText(mbtiArray.get(holder.getAdapterPosition()));
        holder.tvNickname.setText(nicknameArray.get(holder.getAdapterPosition()));
        Float rating = Float.valueOf(String.valueOf(starArray.get(holder.getAdapterPosition())));
        holder.ratingBar.setRating(rating);
//        holder.ratingBar.setEnabled(false);
        holder.ratingBar.setIsIndicator(true);
        holder.tvWriting.setText(writingArray.get(holder.getAdapterPosition()));
        Log.d(TAG, "onBindViewHolder: writing"+writingArray.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return writingArray.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNickname, tvMbti, tvWriting, tvThumbupNum;
        private MaterialRatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.tvNicknameShortReviewInDetail);
            tvMbti = itemView.findViewById(R.id.tvMbtiShortReviewInDetail);
            tvWriting = itemView.findViewById(R.id.tvShortReviewWritingInDetail);
            tvThumbupNum = itemView.findViewById(R.id.tvThumbUpNum);
            ratingBar = itemView.findViewById(R.id.ratingbarShortReviewInDetail);
        }
    }
}
