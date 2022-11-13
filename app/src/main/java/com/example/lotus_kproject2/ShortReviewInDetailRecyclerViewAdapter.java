package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
        holder.tvThumbupNum.setText(String.valueOf(dataList.get(holder.getAdapterPosition()).getLikeNum()));
        if (dataList.get(holder.getAdapterPosition()).getIsLike().equals("1")) {
            holder.imgThumbUp.setImageResource(R.drawable.thumbs_up_filled_small);
        }else {
            holder.imgThumbUp.setImageResource(R.drawable.thumb_up_small);
        }

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(context, holder.imgMore);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.popup_menu_in_writing, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_report:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("해당 한 줄 느낌을 신고하시겠습니까?");
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        reportReview(dataList.get(holder.getAdapterPosition()).getWritingId());
                                    }
                                });
                                builder.setNegativeButton("취소", null);
                                builder.show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        holder.tvNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(activity, OtherUserBlogActivity.class);
                intent.putExtra("nickname", dataList.get(holder.getAdapterPosition()).getNickname());
                intent.putExtra("mbti", dataList.get(holder.getAdapterPosition()).getMbti());
                intent.putExtra("userId", dataList.get(holder.getAdapterPosition()).getUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.overridePendingTransition(0,0);
            }
        });
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

                    String URL = context.getString(R.string.server) + context.getString(R.string.shortLikeCancel);


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d(TAG, "onResponse: board like cancel: res" + response.getString("res"));
                                if (response.getString("res").equals("200")) {
                                    holder.imgThumbUp.setImageResource(R.drawable.thumb_up_small);
                                    Integer likeNum = dataList.get(holder.getAdapterPosition()).getLikeNum() - 1;
                                    holder.tvThumbupNum.setText(String.valueOf(likeNum));
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

                    String URL = context.getString(R.string.server) + context.getString(R.string.shortLikeAdd);


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d(TAG, "onResponse: board like: res" + response.getString("res"));
                                if (response.getString("res").equals("200")) {
                                    holder.imgThumbUp.setImageResource(R.drawable.thumbs_up_filled_small);
                                    Integer likeNum = dataList.get(holder.getAdapterPosition()).getLikeNum() + 1;
                                    holder.tvThumbupNum.setText(String.valueOf(likeNum));
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNickname, tvMbti, tvWriting, tvThumbupNum;
        private MaterialRatingBar ratingBar;
        private ImageView imgThumbUp, imgMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.tvNicknameShortReviewInDetail);
            tvMbti = itemView.findViewById(R.id.tvMbtiShortReviewInDetail);
            tvWriting = itemView.findViewById(R.id.tvShortReviewWritingInDetail);
            tvThumbupNum = itemView.findViewById(R.id.tvThumbUpNumShortReviewInDetail);
            ratingBar = itemView.findViewById(R.id.ratingbarShortReviewInDetail);
            imgThumbUp = itemView.findViewById(R.id.imgThumbUpShortReviewInDetail);
            imgMore = itemView.findViewById(R.id.imgMoreShortReviewInDetail);
        }
    }
    private void reportReview(String boardId) {
        RequestQueue Queue = Volley.newRequestQueue(context);

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("boardID", boardId);
            jsonObject.put("tp", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = context.getString(R.string.server) + context.getString(R.string.report);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("res").equals("200")){
                        Toast.makeText(context,"신고가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"오류로 인해 신고가 되지 않았습니다.",Toast.LENGTH_SHORT).show();
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
