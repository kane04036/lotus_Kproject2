package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {
//    ArrayList<String> movieImageArray;
//    ArrayList<String> movieNameArray;
//    ArrayList<String> movieCodeArray;

    ArrayList<MovieDataList> dataLists = new ArrayList<>();
    Fragment movieDetailFragment = new MovieDetailFragment();
    Context context;
    public MovieRecyclerViewAdapter(Context context, ArrayList dataLists){
        this.dataLists = dataLists;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_movie_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMovieNameResult.setText(dataLists.get(holder.getAdapterPosition()).getMovName());
        holder.imageMovieResult.setClipToOutline(true);

        Glide.with(context).
                load(dataLists.get(holder.getAdapterPosition()).getMovImg()).error(R.drawable.gray_profile)
                .fallback(R.drawable.profile)
                .into(holder.imageMovieResult);


        holder.imageMovieResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(activity, MovieDetailActivity.class);
                intent.putExtra("movName", dataLists.get(holder.getAdapterPosition()).getMovName());
                intent.putExtra("movImage",dataLists.get(holder.getAdapterPosition()).getMovImg());
                intent.putExtra("movCode",dataLists.get(holder.getAdapterPosition()).getMovCode());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageMovieResult;
        TextView tvMovieNameResult;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMovieResult = itemView.findViewById(R.id.imageMovieResult);
            tvMovieNameResult = itemView.findViewById(R.id.tvMovieNameResult);

        }
    }
}
