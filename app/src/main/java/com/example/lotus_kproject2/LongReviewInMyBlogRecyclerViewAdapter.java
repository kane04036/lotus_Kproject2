package com.example.lotus_kproject2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LongReviewInMyBlogRecyclerViewAdapter extends RecyclerView.Adapter<LongReviewInMyBlogRecyclerViewAdapter.ViewHolder> {
    ArrayList<ReviewDataList> dataLists = new ArrayList<>();

    public LongReviewInMyBlogRecyclerViewAdapter(ArrayList dataLists){
        this.dataLists = dataLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.long_review_myblog_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMovName.setText(dataLists.get(holder.getAdapterPosition()).getMovName());
        holder.tvTitle.setText(dataLists.get(holder.getAdapterPosition()).getTitle());
        holder.tvWriting.setText(dataLists.get(holder.getAdapterPosition()).getWriting());
    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbUp;
        TextView tvMovName, tvTitle, tvWriting, tvThumbUpNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovName = itemView.findViewById(R.id.tvMovNameInLongReviewMyblog);
            tvTitle = itemView.findViewById(R.id.tvLongReviewTitleInMyBlog);
            tvWriting = itemView.findViewById(R.id.tvLongReviewWritingInMyBlog);
            tvThumbUpNum = itemView.findViewById(R.id.tvThumbUpNumInMyBlog);
            imgThumbUp = itemView.findViewById(R.id.imgThumbUpLongReviewMyBlog);


        }
    }
}
