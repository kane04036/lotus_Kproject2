package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
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
        Log.d(TAG, "MovieRecyclerViewAdapter: 생성자 ");

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
        for(int i = 0; i< movieImageArray.size(); i++) {
            Log.d(TAG, "MovieRecyclerViewAdapter: imgArray: " + movieImageArray.get(i));
        }
        holder.tvMovieNameResult.setText(movieNameArray.get(position));
        holder.imageMovieResult.setClipToOutline(true);


        Glide.with(context).
                load(movieImageArray.get(position)).error(R.drawable.gray_profile)
                .fallback(R.drawable.profile)
                .into(holder.imageMovieResult);

//        if(movieImageArray.get(position).isEmpty()){
//            holder.imageMovieResult.setImageResource(R.drawable.gray_profile);
//        }
//        else {
//            Picasso.get().load(movieImageArray.get(position)).into(holder.imageMovieResult);
//        }

        holder.imageMovieResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutInMovieResult, movieDetailFragment).commit();
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