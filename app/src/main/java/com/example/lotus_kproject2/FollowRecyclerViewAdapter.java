package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FollowRecyclerViewAdapter extends RecyclerView.Adapter<FollowRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "follow adapter";
    ArrayList<UserDataList> dataLists = new ArrayList<>();
    Context context;

    public FollowRecyclerViewAdapter(Context context, ArrayList dataLists) {
        this.dataLists = dataLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNickname.setText(dataLists.get(holder.getAdapterPosition()).getNickname());
        holder.tvMbti.setText(dataLists.get(holder.getAdapterPosition()).getMbti());
        if (dataLists.get(holder.getAdapterPosition()).getIsFollow().equals("1")) {
            holder.btnFollow.setBackgroundResource(R.drawable.round_rectangle_gray);
            holder.btnFollow.setTextColor(Color.BLACK);
        } else {
            holder.btnFollow.setBackgroundResource(R.drawable.round_rectangle_signaturecolor);
            holder.btnFollow.setTextColor(Color.WHITE);
        }

        holder.tvNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(activity, OtherUserBlogActivity.class);
                intent.putExtra("nickname", dataLists.get(holder.getAdapterPosition()).getNickname());
                intent.putExtra("mbti", dataLists.get(holder.getAdapterPosition()).getMbti());
                intent.putExtra("userId", dataLists.get(holder.getAdapterPosition()).getUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataLists.get(holder.getAdapterPosition()).getIsFollow().equals("1")) {
                    requestFollowCancel(dataLists.get(holder.getAdapterPosition()).getUserId(), holder.getAdapterPosition());
                } else {
                    requestFollow(dataLists.get(holder.getAdapterPosition()).getUserId(), holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNickname, tvMbti, btnFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.tvNicknameInFollowList);
            tvMbti = itemView.findViewById(R.id.tvMbtiInFollowList);
            btnFollow = itemView.findViewById(R.id.btnFollowInList);
        }
    }

    private void requestFollow(String userId,Integer index) {
        RequestQueue Queue = Volley.newRequestQueue(context);

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("user_id", sharedPreferences.getString("memNum", ""));
            jsonObject.put("target_id", userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = context.getString(R.string.server) + context.getString(R.string.follow);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: follow request res:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        dataLists.get(index).setIsFollow("1");
                        notifyDataSetChanged();
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

    private void requestFollowCancel(String userId,Integer index) {
        RequestQueue Queue = Volley.newRequestQueue(context);

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("user_id", sharedPreferences.getString("memNum", ""));
            jsonObject.put("target_id", userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = context.getString(R.string.server) + context.getString(R.string.cancelFollow);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: follow cancel request res:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        dataLists.get(index).setIsFollow("0");
                        notifyDataSetChanged();
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
