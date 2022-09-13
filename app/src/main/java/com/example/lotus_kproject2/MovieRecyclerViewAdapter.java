package com.example.lotus_kproject2;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {
    ArrayList<Integer> movieImageArray;
    ArrayList<String> movieNameArray;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_movie_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMovieNameResult.setText(movieNameArray.get(position));
        holder.imageMovieResult.setClipToOutline(true);
        holder.imageMovieResult.setImageResource(movieImageArray.get(position));
    }

    @Override
    public int getItemCount() {
        return movieImageArray.size();
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
    void setImage(ArrayList imageArray){
        movieImageArray = imageArray;
    }
    void setMovieName(ArrayList nameArray){
        movieNameArray = nameArray;
    }
}
