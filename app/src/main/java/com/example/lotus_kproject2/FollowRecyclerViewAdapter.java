package com.example.lotus_kproject2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FollowRecyclerViewAdapter extends RecyclerView.Adapter<FollowRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "follow adapter" ;
    ArrayList<UserDataList> dataLists = new ArrayList<>();
    public FollowRecyclerViewAdapter(ArrayList dataLists){
        this.dataLists = dataLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_list_item, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position"+position);
        holder.tvNickname.setText(dataLists.get(holder.getAdapterPosition()).getNickname());
        holder.tvMbti.setText(dataLists.get(holder.getAdapterPosition()).getMbti());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+dataLists.size());
        return dataLists.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNickname, tvMbti;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.tvNicknameInFollowList);
            tvMbti = itemView.findViewById(R.id.tvMbtiInFollowList);
        }
    }
}
