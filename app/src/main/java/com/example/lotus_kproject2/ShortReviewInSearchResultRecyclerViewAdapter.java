package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

public class ShortReviewInSearchResultRecyclerViewAdapter extends RecyclerView.Adapter<ShortReviewInSearchResultRecyclerViewAdapter.ViewHolder> {
    ArrayList<ReviewDataList> dataLists = new ArrayList<>();
    Context context;
    ArrayList<Integer> likeNumArray = new ArrayList<>();

    public ShortReviewInSearchResultRecyclerViewAdapter(Context context, ArrayList dataLists) {
        this.dataLists = dataLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.short_review_search_result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMbti.setText(dataLists.get(holder.getAdapterPosition()).getMbti());
        holder.tvNickname.setText(dataLists.get(holder.getAdapterPosition()).getNickname());
        holder.tvWriting.setText(dataLists.get(holder.getAdapterPosition()).getWriting());
        holder.ratingBar.setRating(dataLists.get(holder.getAdapterPosition()).getStar());
        holder.tvMovName.setText(dataLists.get(holder.getAdapterPosition()).getMovName());
        holder.tvThumbUpNum.setText(String.valueOf(dataLists.get(holder.getAdapterPosition()).getLikeNum()));
//        Glide.with(context).load(dataLists.get(holder.getAdapterPosition()).getMovieData()
//                .getMovImg()).error(R.drawable.gray_profile).into(holder.imgMov);
        if (dataLists.get(holder.getAdapterPosition()).getIsLike().equals("1")) {
            holder.imgThumbUp.setImageResource(R.drawable.thumbs_up_filled_small);
        }else {
            holder.imgThumbUp.setImageResource(R.drawable.thumb_up_small);
        }
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

                    String URL = context.getString(R.string.server) + context.getString(R.string.shortLikeCancel);


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

                    String URL = context.getString(R.string.server) + context.getString(R.string.shortLikeAdd);


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
                activity.overridePendingTransition(0,0);
            }
        });

//        holder.imgMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final PopupMenu popupMenu = new PopupMenu(context, holder.imgMore);
//                MenuInflater inflater = popupMenu.getMenuInflater();
//                inflater.inflate(R.menu.popup_menu_in_writing, popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        switch (menuItem.getItemId()) {
//                            case R.id.menu_report:
//                                reportReview(dataLists.get(holder.getAdapterPosition()).getWritingId());
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                popupMenu.show();
//            }
//        });
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataLists.get(holder.getAdapterPosition()).getUserId().equals(sharedPreferences.getString("memNum", ""))) {
                    final PopupMenu popupMenu = new PopupMenu(context, holder.imgMore);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.popup_menu_short_review_in_myblog, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_modify_in_myBlog:
                                    Activity activity = (Activity) context;
                                    Intent intent = new Intent(activity, ModifyShortReviewActivity.class);
                                    intent.putExtra("star", String.valueOf(dataLists.get(holder.getAdapterPosition()).getStar()));
                                    intent.putExtra("movCode", dataLists.get(holder.getAdapterPosition()).getMovId());
                                    intent.putExtra("writing", dataLists.get(holder.getAdapterPosition()).getWriting());
                                    intent.putExtra("boardId", dataLists.get(holder.getAdapterPosition()).getWritingId());
                                    activity.startActivity(intent);
                                    activity.overridePendingTransition(0,0);
                                    break;
                                case R.id.menu_delete_in_myBlog:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("한 줄 느낌을 삭제하시겠습니까?");
                                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            deleteReview(dataLists.get(holder.getAdapterPosition()).getWritingId(), holder.getAdapterPosition());

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
                }else{
                    final PopupMenu popupMenu = new PopupMenu(context, holder.imgMore);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.popup_menu_in_writing, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_report:
                                    reportReview(dataLists.get(holder.getAdapterPosition()).getWritingId());
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMbti, tvNickname, tvWriting, tvThumbUpNum, tvMovName;
        MaterialRatingBar ratingBar;
        ImageView imgMore, imgThumbUp, imgMov;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMbti = itemView.findViewById(R.id.tvMbtiShortReviewInSearchResult);
            tvNickname = itemView.findViewById(R.id.tvNicknameShortReviewInSearchResult);
            tvWriting = itemView.findViewById(R.id.tvShortReviewWritingInSearchResult);
            ratingBar = itemView.findViewById(R.id.ratingbarShortReviewInSearchResult);
            tvMovName = itemView.findViewById(R.id.tvMovNameShortReviewInSearchResult);
            imgMore = itemView.findViewById(R.id.imgMoreShortReviewInSearchResult);
            tvThumbUpNum = itemView.findViewById(R.id.tvThumbUpNumInShortReviewSearchResult);
            imgThumbUp = itemView.findViewById(R.id.imgThumbUpInShortReviewSearchResult);
//            imgMov = itemView.findViewById(R.id.imgMovInShortReviewBoard);

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
                    Log.d(TAG, "onResponse: report: res" + response.getString("res"));


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
    private void deleteReview(String boardId, Integer index) {
        RequestQueue Queue = Volley.newRequestQueue(context);

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("boardID", boardId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = context.getString(R.string.server) + context.getString(R.string.deleteShortReview);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: delete: res" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        Log.d(TAG, "onResponse: remove index:" + index);
                        dataLists.remove(index);
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
