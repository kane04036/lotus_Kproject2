package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class LongReviewInMyBlogRecyclerViewAdapter extends RecyclerView.Adapter<LongReviewInMyBlogRecyclerViewAdapter.ViewHolder> {
    ArrayList<ReviewDataList> dataLists = new ArrayList<>();
    Context context;

    public LongReviewInMyBlogRecyclerViewAdapter(Context context, ArrayList dataLists){
        this.dataLists = dataLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.long_review_myblog_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMovName.setText(dataLists.get(holder.getAdapterPosition()).getMovName());
        holder.tvTitle.setText(dataLists.get(holder.getAdapterPosition()).getTitle());
        holder.tvWriting.setText(dataLists.get(holder.getAdapterPosition()).getWriting());
        holder.tvThumbUpNum.setText(dataLists.get(holder.getAdapterPosition()).getLikeNum());
        Glide.with(context).load(dataLists.get(holder.getAdapterPosition()).getMovieData()
                .getMovImg()).error(R.drawable.gray_profile).into(holder.imgMov);
        if(dataLists.get(holder.getAdapterPosition()).getIsLike().equals("1")){
            holder.imgThumbUp.setImageResource(R.drawable.thumbs_up_filled_small);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailOfBoardActivity.class);
                intent.putExtra("mbti",dataLists.get(holder.getAdapterPosition()).getMbti());
                intent.putExtra("likeNum",dataLists.get(holder.getAdapterPosition()).getLikeNum());
                intent.putExtra("nickname",dataLists.get(holder.getAdapterPosition()).getNickname());
                intent.putExtra("title",dataLists.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("writing",dataLists.get(holder.getAdapterPosition()).getWriting());
                intent.putExtra("boardId", dataLists.get(holder.getAdapterPosition()).getWritingId());
                intent.putExtra("userId",dataLists.get(holder.getAdapterPosition()).getUserId());
                intent.putExtra("movCode",dataLists.get(holder.getAdapterPosition()).getMovieData().getMovCode());

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

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

                String URL = context.getString(R.string.server) + context.getString(R.string.longLikeAdd);


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse: board like: res" + response.getString("res"));
                            if (response.getString("res").equals("200")) {
                                holder.imgThumbUp.setImageResource(R.drawable.thumbs_up_filled_small);
                                holder.tvThumbUpNum.setText(String.valueOf(Integer.parseInt(dataLists.get(holder.getAdapterPosition()).getLikeNum())+1));
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
        ImageView imgThumbUp, imgMov;
        TextView tvMovName, tvTitle, tvWriting, tvThumbUpNum;
        RelativeLayout layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovName = itemView.findViewById(R.id.tvMovNameInLongReviewMyblog);
            tvTitle = itemView.findViewById(R.id.tvLongReviewTitleInMyBlog);
            tvWriting = itemView.findViewById(R.id.tvLongReviewWritingInMyBlog);
            tvThumbUpNum = itemView.findViewById(R.id.tvThumbUpNumInMyBlog);
            imgThumbUp = itemView.findViewById(R.id.imgThumbUpLongReviewMyBlog);
            imgMov = itemView.findViewById(R.id.imgMovInLongReviewMyBlog);
            layout = itemView.findViewById(R.id.layoutLongReviewItemInBlog);
        }
    }
}
