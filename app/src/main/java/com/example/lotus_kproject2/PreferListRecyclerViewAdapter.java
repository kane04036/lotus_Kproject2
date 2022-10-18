package com.example.lotus_kproject2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PreferListRecyclerViewAdapter extends RecyclerView.Adapter<PreferListRecyclerViewAdapter.ViewHolder> {
    ArrayList<MovieDataList> dataLists = new ArrayList<>();
    Context context;
    public PreferListRecyclerViewAdapter(Context context, ArrayList dataLists){
        this.dataLists = dataLists;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);

        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(dataLists.get(holder.getAdapterPosition()).getMovImg()).error(R.drawable.gray_profile).into(holder.imgViewMov);
        holder.tvMovName.setText(dataLists.get(holder.getAdapterPosition()).getMovName());
        holder.tvReleaseDate.setText(dataLists.get(holder.getAdapterPosition()).getReleaseDate());

    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgViewMov;
        TextView tvMovName, tvReleaseDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgViewMov = itemView.findViewById(R.id.imgMovieList);
            tvMovName = itemView.findViewById(R.id.tvMovieNameInList);
            tvReleaseDate = itemView.findViewById(R.id.tvYearInList);
        }
    }
}
