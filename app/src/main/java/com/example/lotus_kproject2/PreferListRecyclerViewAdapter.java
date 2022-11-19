package com.example.lotus_kproject2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
        if (!dataLists.get(holder.getAdapterPosition()).getReleaseDate().isEmpty())
            holder.tvReleaseDate.setText(dataLists.get(holder.getAdapterPosition()).getReleaseDate().substring(0, 4));
        holder.imgViewMov.setClipToOutline(true);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(activity, MovieDetailActivity.class);
                intent.putExtra("movCode", dataLists.get(holder.getAdapterPosition()).getMovCode());
                Log.d("TAG", "prefer movie code"+dataLists.get(holder.getAdapterPosition()).getMovCode());
                activity.startActivity(intent);
                activity.overridePendingTransition(0,0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgViewMov;
        TextView tvMovName, tvReleaseDate;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgViewMov = itemView.findViewById(R.id.imgMovieList);
            tvMovName = itemView.findViewById(R.id.tvMovieNameInList);
            tvReleaseDate = itemView.findViewById(R.id.tvYearInList);
            layout = itemView.findViewById(R.id.layoutMovieList);
        }
    }
}
