package com.example.lotus_kproject2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ShortReviewBoardRecyclerViewAdapter extends RecyclerView.Adapter<ShortReviewBoardRecyclerViewAdapter.ViewHolder> {
    ArrayList<ReviewDataList> dataLists = new ArrayList<>();

    public ShortReviewBoardRecyclerViewAdapter(ArrayList dataLists) {
        this.dataLists = dataLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.short_review_board_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMbti.setText(dataLists.get(holder.getAdapterPosition()).getMbti());
        holder.tvNickname.setText(dataLists.get(holder.getAdapterPosition()).getNickname());
        holder.tvWriting.setText(dataLists.get(holder.getAdapterPosition()).getWriting());
//        holder.tvMbti.setText(dataLists.get(holder.getAdapterPosition()).getMbti());
        holder.ratingBar.setRating(dataLists.get(holder.getAdapterPosition()).getStar());

    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMbti, tvNickname, tvWriting, tvThumbUpNum;
        MaterialRatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMbti = itemView.findViewById(R.id.tvMbtiShortReviewInBoard);
            tvNickname = itemView.findViewById(R.id.tvNicknameShortReviewInBoard);
            tvWriting = itemView.findViewById(R.id.tvShortReviewWritingInBoard);
            ratingBar = itemView.findViewById(R.id.ratingbarShortReviewInBoard);
        }
    }
}
