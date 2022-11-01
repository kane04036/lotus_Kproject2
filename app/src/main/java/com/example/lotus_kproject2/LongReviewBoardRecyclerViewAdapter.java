package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LongReviewBoardRecyclerViewAdapter extends RecyclerView.Adapter<LongReviewBoardRecyclerViewAdapter.ViewHolder> {
    ArrayList<ReviewDataList> dataLists = new ArrayList<>();
    Context context;
    public LongReviewBoardRecyclerViewAdapter(Context context,ArrayList<ReviewDataList> dataLists){
        this.dataLists = dataLists;
        this.context = context;
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

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailOfBoardActivity.class);
                intent.putExtra("mbti",dataLists.get(holder.getAdapterPosition()).getMbti());
                intent.putExtra("nickname",dataLists.get(holder.getAdapterPosition()).getNickname());
                intent.putExtra("title",dataLists.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("writing",dataLists.get(holder.getAdapterPosition()).getWriting());
                intent.putExtra("boardId", dataLists.get(holder.getAdapterPosition()).getWritingId());
                intent.putExtra("userId",dataLists.get(holder.getAdapterPosition()).getUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.tvNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherUserBlogActivity.class);
                intent.putExtra("nickname", dataLists.get(holder.getAdapterPosition()).getNickname());
                intent.putExtra("mbti", dataLists.get(holder.getAdapterPosition()).getMbti());
                intent.putExtra("userId",dataLists.get(holder.getAdapterPosition()).getUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMovName, tvMbti, tvNickname, tvTitle, tvWriting;
        RelativeLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovName = itemView.findViewById(R.id.tvMovNameInLongReviewBoard);
            tvMbti = itemView.findViewById(R.id.tvMbtiInLongReviewBoard);
            tvNickname = itemView.findViewById(R.id.tvNicknameInLongReviewBoard);
            tvTitle = itemView.findViewById(R.id.tvLongReviewTitleInBoard);
            tvWriting = itemView.findViewById(R.id.tvLongReviewWritingInBoard);
            layout = itemView.findViewById(R.id.layoutLongReviewItemInBoard);
        }
    }
}
