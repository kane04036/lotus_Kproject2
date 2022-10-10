package com.example.lotus_kproject2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LongReviewInDetailRecyclerViewAdapter extends RecyclerView.Adapter<LongReviewInDetailRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> nicknameArray = new ArrayList<>();
    private ArrayList<String> mbtiArray = new ArrayList<>();
    private ArrayList<String> writingArray = new ArrayList<>();
    private ArrayList<String> thumbUpArray = new ArrayList<>();
    private ArrayList<String> movCodeArray = new ArrayList<>();
    private ArrayList<String> writingIdArray = new ArrayList<>();
    private ArrayList<String> userIdArray = new ArrayList<>();
    private ArrayList<String> titleArray = new ArrayList<>();


    private Context context;

    public LongReviewInDetailRecyclerViewAdapter(Context context, ArrayList mbtiArray, ArrayList nicknameArray,ArrayList writingArray, ArrayList titleArray,
                                                 ArrayList thumbUpArray, ArrayList movCodeArray, ArrayList writingIdArray, ArrayList userIdArray){
        this.mbtiArray = mbtiArray;
        this.nicknameArray = nicknameArray;
        this.writingArray = writingArray;
        this.titleArray = titleArray;
        this.thumbUpArray = thumbUpArray;
        this.movCodeArray = movCodeArray;
        this.writingIdArray = writingIdArray;
        this.userIdArray = userIdArray;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.long_review_in_detail_item, parent, false);
        return new LongReviewInDetailRecyclerViewAdapter.ViewHolder(view);     }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(titleArray.get(holder.getAdapterPosition()));
        holder.tvMbti.setText(mbtiArray.get(holder.getAdapterPosition()));
        holder.tvNickname.setText(nicknameArray.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return userIdArray.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMbti, tvNickname, tvTitle,tvThumbUPNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMbti = itemView.findViewById(R.id.tvMbtiLongReviewInDetail);
            tvNickname = itemView.findViewById(R.id.tvNicknameLongReviewInDetail);
            tvTitle = itemView.findViewById(R.id.tvLongReviewTitleInDetail);
        }
    }
}
