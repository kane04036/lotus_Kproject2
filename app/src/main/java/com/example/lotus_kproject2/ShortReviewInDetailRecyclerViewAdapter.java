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
import java.util.concurrent.BlockingDeque;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ShortReviewInDetailRecyclerViewAdapter extends RecyclerView.Adapter<ShortReviewInDetailRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ReviewDataList> dataList = new ArrayList<>();


    public ShortReviewInDetailRecyclerViewAdapter(Context context, ArrayList dataList) {
        Log.d(TAG, "ShortReviewInDetailRecyclerViewAdapter: creator");
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.short_review_in_detail_item, parent, false);
        return new ShortReviewInDetailRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMbti.setText(dataList.get(holder.getAdapterPosition()).getMbti());
        holder.tvNickname.setText(dataList.get(holder.getAdapterPosition()).getNickname());
        holder.ratingBar.setRating(dataList.get(holder.getAdapterPosition()).getStar());
        holder.ratingBar.setIsIndicator(true);
        holder.tvWriting.setText(dataList.get(holder.getAdapterPosition()).getWriting());
        holder.tvThumbupNum.setText(dataList.get(holder.getAdapterPosition()).getLikeNum());
        if(dataList.get(holder.getAdapterPosition()).getIsLike().equals("1")){
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
                    jsonObject.put("board_id", dataList.get(holder.getAdapterPosition()).getWritingId());
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
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNickname, tvMbti, tvWriting, tvThumbupNum;
        private MaterialRatingBar ratingBar;
        private ImageView imgThumbUp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.tvNicknameShortReviewInDetail);
            tvMbti = itemView.findViewById(R.id.tvMbtiShortReviewInDetail);
            tvWriting = itemView.findViewById(R.id.tvShortReviewWritingInDetail);
            tvThumbupNum = itemView.findViewById(R.id.tvThumbUpNumShortReviewInDetail);
            ratingBar = itemView.findViewById(R.id.ratingbarShortReviewInDetail);
            imgThumbUp = itemView.findViewById(R.id.imgThumbUpShortReviewInDetail);
        }
    }
}
