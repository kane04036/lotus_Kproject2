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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LongReviewInDetailRecyclerViewAdapter extends RecyclerView.Adapter<LongReviewInDetailRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ReviewDataList> dataList = new ArrayList<>();

    public LongReviewInDetailRecyclerViewAdapter(Context context, ArrayList dataList){
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.long_review_in_detail_item, parent, false);
        return new LongReviewInDetailRecyclerViewAdapter.ViewHolder(view);     }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(dataList.get(holder.getAdapterPosition()).getTitle());
        holder.tvMbti.setText(dataList.get(holder.getAdapterPosition()).getMbti());
        holder.tvNickname.setText(dataList.get(holder.getAdapterPosition()).getNickname());
        holder.tvThumbUPNum.setText(String.valueOf(dataList.get(holder.getAdapterPosition()).getLikeNum()));
        holder.tvWriting.setText(dataList.get(holder.getAdapterPosition()).getWriting());
        if (dataList.get(holder.getAdapterPosition()).getIsLike().equals("1")) {
            holder.imgThumbUp.setImageResource(R.drawable.thumbs_up_filled_small);
        }else {
            holder.imgThumbUp.setImageResource(R.drawable.thumb_up_small);
        }

        holder.imgThumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataList.get(holder.getAdapterPosition()).getIsLike().equals("1")) {
                    RequestQueue Queue = Volley.newRequestQueue(context);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
                        jsonObject.put("token", sharedPreferences.getString("token", ""));
                        jsonObject.put("board_id", dataList.get(holder.getAdapterPosition()).getWritingId());
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
                                    Integer likeNum = dataList.get(holder.getAdapterPosition()).getLikeNum() - 1;
                                    holder.tvThumbUPNum.setText(String.valueOf(likeNum));
                                    dataList.get(holder.getAdapterPosition()).setLikeNum(likeNum);
                                    dataList.get(holder.getAdapterPosition()).setIsLike("0");
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
                        jsonObject.put("board_id", dataList.get(holder.getAdapterPosition()).getWritingId());
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
                                    Integer likeNum = dataList.get(holder.getAdapterPosition()).getLikeNum() + 1;
                                    holder.tvThumbUPNum.setText(String.valueOf(likeNum));
                                    dataList.get(holder.getAdapterPosition()).setLikeNum(likeNum);
                                    dataList.get(holder.getAdapterPosition()).setIsLike("1");
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
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMbti, tvNickname, tvTitle,tvThumbUPNum,tvWriting;
        ImageView imgThumbUp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMbti = itemView.findViewById(R.id.tvMbtiLongReviewInDetail);
            tvNickname = itemView.findViewById(R.id.tvNicknameLongReviewInDetail);
            tvTitle = itemView.findViewById(R.id.tvLongReviewTitleInDetail);
            tvThumbUPNum = itemView.findViewById(R.id.tvThumbUpNumLongReviewInDetail);
            imgThumbUp = itemView.findViewById(R.id.imgThumbUpLongReviewInDetail);
            tvWriting = itemView.findViewById(R.id.tvLongReviewWritingInDetail);
        }
    }
}
