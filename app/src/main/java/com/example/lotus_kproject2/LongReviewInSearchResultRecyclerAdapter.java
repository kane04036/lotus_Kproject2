package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LongReviewInSearchResultRecyclerAdapter extends RecyclerView.Adapter<LongReviewInSearchResultRecyclerAdapter.ViewHolder> {
    private ArrayList<ReviewDataList> dataLists = new ArrayList<>();
    private Boolean isLike = false;
    private Context context;
    private Bundle bundle = new Bundle();

    public LongReviewInSearchResultRecyclerAdapter(Context context, ArrayList<ReviewDataList> dataLists) {
        this.dataLists = dataLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.long_review_search_result_item, parent, false);
        return new LongReviewInSearchResultRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(dataLists.get(holder.getAdapterPosition()).getTitle());
        holder.tvNickname.setText(dataLists.get(holder.getAdapterPosition()).getNickname());
        holder.tvMbti.setText(dataLists.get(holder.getAdapterPosition()).getMbti());
        holder.tvWriting.setText(dataLists.get(holder.getAdapterPosition()).getWriting());
        holder.tvMovName.setText(dataLists.get(holder.getAdapterPosition()).getMovName());
        holder.tvThumbUpNum.setText(String.valueOf(dataLists.get(holder.getAdapterPosition()).getLikeNum()));
//        Glide.with(context).load(dataLists.get(holder.getAdapterPosition()).getMovieData()
//                .getMovImg()).error(R.drawable.gray_profile).into(holder.imgMov);
        if (dataLists.get(holder.getAdapterPosition()).getIsLike().equals("1")) {
            holder.imgThumbUp.setImageResource(R.drawable.thumbs_up_filled_small);
        }else {
            holder.imgThumbUp.setImageResource(R.drawable.thumb_up_small);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(activity, DetailOfBoardActivity.class);
                intent.putExtra("writingId", dataLists.get(holder.getAdapterPosition()).getWritingId());
                intent.putExtra("movCode", dataLists.get(holder.getAdapterPosition()).getMovId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.overridePendingTransition(0,0);
            }
        });

        holder.tvNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherUserBlogActivity.class);
                intent.putExtra("nickname", dataLists.get(holder.getAdapterPosition()).getNickname());
                intent.putExtra("mbti", dataLists.get(holder.getAdapterPosition()).getMbti());
                intent.putExtra("userId", dataLists.get(holder.getAdapterPosition()).getUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        holder.imgThumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataLists.get(holder.getAdapterPosition()).getIsLike().equals("1")) {
                    RequestQueue Queue = Volley.newRequestQueue(context);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
                        jsonObject.put("token", sharedPreferences.getString("token", ""));
                        jsonObject.put("board_id", dataLists.get(holder.getAdapterPosition()).getWritingId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String URL = context.getString(R.string.server) + context.getString(R.string.longLikeCancel);


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d(TAG, "onResponse: board like cancel: res" + response.getString("res"));
                                if (response.getString("res").equals("200")) {
                                    holder.imgThumbUp.setImageResource(R.drawable.thumb_up_small);
                                    Integer likeNum = dataLists.get(holder.getAdapterPosition()).getLikeNum() - 1;
                                    holder.tvThumbUpNum.setText(String.valueOf(likeNum));
                                    dataLists.get(holder.getAdapterPosition()).setLikeNum(likeNum);
                                    dataLists.get(holder.getAdapterPosition()).setIsLike("0");
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
                } else {
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
                                    Integer likeNum = dataLists.get(holder.getAdapterPosition()).getLikeNum() + 1;
                                    holder.tvThumbUpNum.setText(String.valueOf(likeNum));
                                    dataLists.get(holder.getAdapterPosition()).setLikeNum(likeNum);
                                    dataLists.get(holder.getAdapterPosition()).setIsLike("1");
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovName, tvMbti, tvNickname, tvTitle, tvWriting, tvThumbUpNum;
        RelativeLayout layout;
        ImageView imgMov, imgThumbUp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovName = itemView.findViewById(R.id.tvMovNameInLongReviewSearchResult);
            tvMbti = itemView.findViewById(R.id.tvMbtiInLongReviewSearchResult);
            tvNickname = itemView.findViewById(R.id.tvNicknameInLongReviewSearchResult);
            tvTitle = itemView.findViewById(R.id.tvLongReviewTitleInSearchResult);
            tvWriting = itemView.findViewById(R.id.tvLongReviewWritingInSearchResult);
            tvThumbUpNum = itemView.findViewById(R.id.tvThumbUpNumLongReviewInSearchResult);
//            imgMov = itemView.findViewById(R.id.imgMovInLongReviewBoard);
            layout = itemView.findViewById(R.id.layoutLongReviewItemInSearchResult);
            imgThumbUp = itemView.findViewById(R.id.imgThumbUpLongReviewInSearchResult);
        }
    }
}
