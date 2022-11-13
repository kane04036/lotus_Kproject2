package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;

public class MovieListRecyclerViewAdapter extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder> {
    ArrayList<MovieDataList> dataLists = new ArrayList<>();
    private boolean isSelected = false;
    private int selectedPosition = -1;

    Context context;

    public MovieListRecyclerViewAdapter(Context context, ArrayList dataLists) {
        this.dataLists = dataLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


            holder.tvMovieNmaeInList.setText(dataLists.get(holder.getAdapterPosition()).getMovName());
            if (!dataLists.get(holder.getAdapterPosition()).getReleaseDate().isEmpty())
                holder.tvYearInList.setText(dataLists.get(holder.getAdapterPosition()).getReleaseDate().substring(0, 4));
            else
                holder.tvYearInList.setText("개봉일자 불명");
            holder.imgMovieList.setClipToOutline(true);
            Glide.with(context).load(dataLists.get(holder.getAdapterPosition()).getMovImg()).error(R.drawable.gray_profile).into(holder.imgMovieList);

        holder.layoutMovieList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(activity, ReviewEditorActivity.class);
                intent.putExtra("movName",dataLists.get(holder.getAdapterPosition()).getMovName());
                intent.putExtra("movCode",dataLists.get(holder.getAdapterPosition()).getMovCode());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
                activity.overridePendingTransition(0,0);
                activity.finish();
            }
        });

        if (selectedPosition == position) {
            holder.btnGoReview.setVisibility(View.VISIBLE);
        } else {
            holder.btnGoReview.setVisibility(View.INVISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMovieList;
        TextView tvMovieNmaeInList, tvYearInList;
        Button btnGoReview;
        RelativeLayout layoutMovieList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovieList = itemView.findViewById(R.id.imgMovieList);
            tvMovieNmaeInList = itemView.findViewById(R.id.tvMovieNameInList);
            btnGoReview = itemView.findViewById(R.id.btnGoReview);
            layoutMovieList = itemView.findViewById(R.id.layoutMovieList);
            tvYearInList = itemView.findViewById(R.id.tvYearInList);
        }
    }



}
