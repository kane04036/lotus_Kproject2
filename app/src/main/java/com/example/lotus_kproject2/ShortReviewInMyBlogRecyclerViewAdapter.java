package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ShortReviewInMyBlogRecyclerViewAdapter extends RecyclerView.Adapter<ShortReviewInMyBlogRecyclerViewAdapter.ViewHolder> {
    ArrayList<ReviewDataList> dataLists = new ArrayList<>();
    Context context;

    public ShortReviewInMyBlogRecyclerViewAdapter(Context context, ArrayList dataLists) {
        this.dataLists = dataLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.short_review_myblog_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ratingBar.setRating(dataLists.get(holder.getAdapterPosition()).getStar());
        holder.tvWriting.setText(dataLists.get(holder.getAdapterPosition()).getWriting());
        holder.tvMovName.setText("<" + dataLists.get(holder.getAdapterPosition()).getMovName() + ">");
        holder.tvThumbupNum.setText(dataLists.get(holder.getAdapterPosition()).getLikeNum());
        Glide.with(context).load(dataLists.get(holder.getAdapterPosition()).getMovieData()
                .getMovImg()).error(R.drawable.gray_profile).into(holder.imgMov);

        if(dataLists.get(holder.getAdapterPosition()).getIsLike().equals("1")){
            holder.imgThumbUp.setImageResource(R.drawable.thumbs_up_filled_small);
        }

        holder.imgThumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue Queue = Volley.newRequestQueue(context);

                JSONObject jsonObject = new JSONObject();
                try {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
                    jsonObject.put("token", sharedPreferences.getString("token", ""));
                    jsonObject.put("board_id", dataLists.get(holder.getAdapterPosition()).getWritingId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String URL = context.getString(R.string.server) + context.getString(R.string.shortLikeAdd);


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse: board like: res" + response.getString("res"));
                            if (response.getString("res").equals("200")) {
                                holder.imgThumbUp.setImageResource(R.drawable.thumbs_up_filled_small);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Queue.add(jsonObjectRequest);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialRatingBar ratingBar;
        TextView tvWriting, tvThumbupNum, tvMovName;
        ImageView imgThumbUp, imgMov;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.ratingbarShortReviewInMyBlog);
            tvWriting = itemView.findViewById(R.id.tvShortReviewWritingInMyBlog);
            tvThumbupNum = itemView.findViewById(R.id.tvThumbUpNumShortReviewInMyBlog);
            tvMovName = itemView.findViewById(R.id.tvMovNameInShortReviewMyBlog);
            imgThumbUp = itemView.findViewById(R.id.imgThumbUpShortReviewInMyBlog);
            imgMov = itemView.findViewById(R.id.imgMovInShortReviewMyBlog);
        }
    }

}
