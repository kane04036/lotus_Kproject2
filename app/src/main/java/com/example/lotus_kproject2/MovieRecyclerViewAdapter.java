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
    ArrayList<String> movieImageArray;
    ArrayList<String> movieNameArray;
    ArrayList<String> movieCodeArray;
    Fragment movieDetailFragment = new MovieDetailFragment();
    Context context;
    public MovieRecyclerViewAdapter(Context context, ArrayList nameArray, ArrayList imageArray, ArrayList codeArray){
        movieNameArray = nameArray;
        movieImageArray = imageArray;
        movieCodeArray = codeArray;

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
        holder.tvMovieNameResult.setText(movieNameArray.get(holder.getAdapterPosition()));
        holder.imageMovieResult.setClipToOutline(true);


        Glide.with(context).
                load(movieImageArray.get(holder.getAdapterPosition())).error(R.drawable.gray_profile)
                .fallback(R.drawable.profile)
                .into(holder.imageMovieResult);


        holder.imageMovieResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "recyclerView: Mov Code"+movieCodeArray.get(holder.getAdapterPosition()));
                Log.d(TAG, "recyclerView: Mov Name"+movieNameArray.get(holder.getAdapterPosition()));
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("movName", movieNameArray.get(holder.getAdapterPosition()));
                intent.putExtra("movImage",movieImageArray.get(holder.getAdapterPosition()));
                intent.putExtra("movCode",movieCodeArray.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieCodeArray.size();
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
