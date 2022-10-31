package com.example.lotus_kproject2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ShortReviewInMyBlogRecyclerViewAdapter extends RecyclerView.Adapter<ShortReviewInMyBlogRecyclerViewAdapter.ViewHolder> {
    ArrayList<ReviewDataList> dataLists = new ArrayList<>();

    public ShortReviewInMyBlogRecyclerViewAdapter(ArrayList dataLists){
        this.dataLists = dataLists;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.short_review_myblog_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ratingBar.setRating(dataLists.get(holder.getAdapterPosition()).getStar());
        holder.tvWriting.setText(dataLists.get(holder.getAdapterPosition()).getWriting());
        holder.tvMovName.setText("<"+dataLists.get(holder.getAdapterPosition()).getMovName()+">");
    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialRatingBar ratingBar;
        TextView tvWriting, tvThumbupNum, tvMovName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.ratingbarShortReviewInMyBlog);
            tvWriting = itemView.findViewById(R.id.tvShortReviewWritingInMyBlog);
            tvThumbupNum = itemView.findViewById(R.id.tvThumbUpNumInMyBlog);
            tvMovName = itemView.findViewById(R.id.tvMovNameInShortReviewMyBlog);
        }
    }

}
