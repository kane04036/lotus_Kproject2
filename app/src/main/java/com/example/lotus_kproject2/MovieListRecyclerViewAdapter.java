package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
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
    ArrayList<String> imgArray = new ArrayList();
    ArrayList<String> nameArray = new ArrayList();
    ArrayList<String> codeArray = new ArrayList();
    ArrayList<String> yearArray = new ArrayList<>();
    String title, contents;
    boolean isSelected = false;
    int selectedPosition;

    Context context;

    public MovieListRecyclerViewAdapter(Context context, ArrayList nameArray, ArrayList imgArray, ArrayList codeArray, ArrayList yearArray,String title, String contents) {
        this.imgArray = imgArray;
        this.nameArray = nameArray;
        this.codeArray = codeArray;
        this.yearArray = yearArray;
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
        if (!imgArray.isEmpty()) {
            if(yearArray.get(position).equals("")){
                holder.tvYearInList.setText("개봉일자 불명");
            }
            holder.tvMovieNmaeInList.setText(nameArray.get(position));
            holder.tvYearInList.setText(yearArray.get(position));
            holder.imgMovieList.setClipToOutline(true);
            Glide.with(context).load(imgArray.get(position)).error(R.drawable.gray_profile).into(holder.imgMovieList);

        }
        holder.layoutMovieList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!isSelected) {
                    holder.btnGoReview.setVisibility(View.VISIBLE);
                    isSelected = true;
                    selectedPosition = position;
                    notifyDataSetChanged();
//                }
            }

        });
        holder.btnGoReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReviewEditorActivity.class);
                intent.putExtra("existMovie", 1);
                intent.putExtra("movName", nameArray.get(position));
                intent.putExtra("movCode",codeArray.get(position));
                intent.putExtra("title", title);
                intent.putExtra("contents", contents);
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));

            }
        });


    }

    @Override
    public int getItemCount() {
        return imgArray.size();
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
