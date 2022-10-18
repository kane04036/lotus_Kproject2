package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LongReviewBoardRecyclerViewAdapter extends RecyclerView.Adapter<LongReviewBoardRecyclerViewAdapter.ViewHolder> {
    ArrayList<ReviewDataList> dataLists = new ArrayList<>();
    public LongReviewBoardRecyclerViewAdapter(ArrayList<ReviewDataList> dataLists){
        this.dataLists = dataLists;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.long_review_board_item, parent, false);
        return new LongReviewBoardRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(dataLists.get(holder.getAdapterPosition()).getTitle());
        holder.tvNickname.setText(dataLists.get(holder.getAdapterPosition()).getNickname());
        holder.tvMbti.setText(dataLists.get(holder.getAdapterPosition()).getMbti());
        holder.tvWriting.setText(dataLists.get(holder.getAdapterPosition()).getWriting());
        holder.tvMovName.setText("<"+dataLists.get(holder.getAdapterPosition()).getMovName()+">");
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: datalist.size():"+dataLists.size());
        return dataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMovName, tvMbti, tvNickname, tvTitle, tvWriting;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovName = itemView.findViewById(R.id.tvMovNameInLongReviewBoard);
            tvMbti = itemView.findViewById(R.id.tvMbtiInLongReviewBoard);
            tvNickname = itemView.findViewById(R.id.tvNicknameInLongReviewBoard);
            tvTitle = itemView.findViewById(R.id.tvLongReviewTitleInBoard);
            tvWriting = itemView.findViewById(R.id.tvLongReviewWritingInBoard);
        }
    }
}
