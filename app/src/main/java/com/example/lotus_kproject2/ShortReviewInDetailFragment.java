package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ShortReviewInDetailFragment extends Fragment {
    private MaterialRatingBar ratingBarInShortReview;
    private EditText edtShortReview;
    private TextView tvWritingCount, tvMyNickname, tvMyMbti, tvMyWriting, tvMyThumbUpNum;
    private ImageView imgMyThumbUp, imgMyMore;
    private MaterialRatingBar myRatingBar;
    private Button btnUploadShortReview;
    private String movCode;
    private RecyclerView recyViewShortReviewInDetail;
    private ShortReviewInDetailRecyclerViewAdapter shortReviewInDetailRecyclerViewAdapter;
    private ArrayList<ReviewDataList> dataLists = new ArrayList<>();
    private RelativeLayout layoutWriteShortReview, layoutMyShortReview;
    private ReviewDataList myReview;
    private Boolean isModify = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("movData_short", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                movCode = result.getString("movCode");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        shortReviewListRequest(movCode);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_layout_short_review, container, false);

        shortReviewInDetailRecyclerViewAdapter = new ShortReviewInDetailRecyclerViewAdapter(getActivity(), dataLists);

        ratingBarInShortReview = view.findViewById(R.id.ratingbarInShortReview);
        edtShortReview = view.findViewById(R.id.edtShortReview);
        tvWritingCount = view.findViewById(R.id.tvWritingCount);
        btnUploadShortReview = view.findViewById(R.id.btnUploadShortReview);
        recyViewShortReviewInDetail = view.findViewById(R.id.recyViewShortReviewInDetail);

        layoutMyShortReview = view.findViewById(R.id.layoutMyShortReview);
        layoutWriteShortReview = view.findViewById(R.id.layoutWriteShortReview);
        layoutMyShortReview.setVisibility(View.INVISIBLE);
        layoutWriteShortReview.setVisibility(View.INVISIBLE);

        tvMyMbti = view.findViewById(R.id.tvMbtiShortReviewInDetailMine);
        tvMyNickname = view.findViewById(R.id.tvNicknameShortReviewInDetailMine);
        tvMyWriting = view.findViewById(R.id.tvShortReviewWritingInDetailMine);
        myRatingBar = view.findViewById(R.id.ratingbarShortReviewInDetailMine);
        imgMyThumbUp = view.findViewById(R.id.imgThumbUpShortReviewInDetailMine);
        tvMyThumbUpNum = view.findViewById(R.id.tvThumbUpNumShortReviewInDetailMine);
        imgMyMore = view.findViewById(R.id.imgMoreShortReviewInDetailMine);

        recyViewShortReviewInDetail.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyViewShortReviewInDetail.setAdapter(shortReviewInDetailRecyclerViewAdapter);

        imgMyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(getActivity(), imgMyMore);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.popup_menu_my_review, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_modify:
                                layoutMyShortReview.setVisibility(View.INVISIBLE);
                                layoutWriteShortReview.setVisibility(View.VISIBLE);
                                edtShortReview.setText(myReview.getWriting());
                                Log.d(TAG, "onMenuItemClick: writing:"+myReview.getWriting());
                                ratingBarInShortReview.setRating(myReview.getStar());
                                Log.d(TAG, "onMenuItemClick: star:"+myReview.getStar());
                                isModify = true;
                                break;
                            case R.id.menu_delete:
                                deleteReview();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        edtShortReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                tvWritingCount.setText(String.valueOf(edtShortReview.getText().length()));
                if (edtShortReview.getText().length() > 0) {
                    btnUploadShortReview.setEnabled(true);
                    btnUploadShortReview.setBackgroundTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.signature_color)));
                }
                else{
                    btnUploadShortReview.setEnabled(false);
                    btnUploadShortReview.setBackgroundTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.lightGray)));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnUploadShortReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isModify) {
                    modifyReview(edtShortReview.getText().toString(), ratingBarInShortReview.getRating());
                } else {
                    uploadShortReview(movCode, edtShortReview.getText().toString(), ratingBarInShortReview.getRating());
                }

            }
        });
        return view;
    }

    private void uploadShortReview(String movCode, String writing, float stars) {
        Log.d(TAG, "uploadShortReview");
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("writing", writing);
            jsonObject.put("movie_id", movCode);
            jsonObject.put("star", stars);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.writeShortReview);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "short Review write: res:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        edtShortReview.setText("");
                        ratingBarInShortReview.setRating(0);
                        edtShortReview.clearFocus();
                        shortReviewListRequest(movCode);
                    } else if (response.getString("res").equals("201")) {
                        if(getActivity() == null){return;}
                        Toast.makeText(getActivity(), "이미 작성된 리뷰가 있습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        if(getActivity() == null){return;}
                        Toast.makeText(getActivity(), "리뷰 작성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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

    private void shortReviewListRequest(String movCode) {
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("movie_id", movCode);
            jsonObject.put("token", sharedPreferences.getString("token", ""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.shortReviewViewInMovie);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "short Review View with movie_id: res:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        JSONArray likeArray = response.getJSONArray("like");
                        JSONArray dataJsonArray = response.getJSONArray("data");
                        JSONArray mineArray = response.getJSONArray("mine");
                        JSONArray isLikeArray = response.getJSONArray("isLike");

                        if(getActivity() == null){
                            return;
                        }
                        Resources res = getResources();
                        String[] mbtiList = res.getStringArray(R.array.mbti_array);

                        if (mineArray.length() > 0) {
                            layoutWriteShortReview.setVisibility(View.INVISIBLE);
                            layoutMyShortReview.setVisibility(View.VISIBLE);
                            JSONObject myReviewObject = mineArray.getJSONObject(0);
                            myReview = new ReviewDataList(myReviewObject.getString("_id"), myReviewObject.getString("movie_id"), myReviewObject.getString("movie_name"),
                                    myReviewObject.getString("user_id"), mbtiList[myReviewObject.getInt("user_mbti")], myReviewObject.getString("user_nickname"),
                                    myReviewObject.getString("writing"), Float.valueOf(myReviewObject.getString("star")), mineArray.getString(1), mineArray.getString(3));

                            tvMyNickname.setText(myReview.getNickname());
                            tvMyWriting.setText(myReview.getWriting());
                            tvMyMbti.setText(myReview.getMbti());
                            tvMyThumbUpNum.setText(myReview.getLikeNum());
                            myRatingBar.setRating(myReview.getStar());
                            if (myReview.getIsLike().equals("1")) {
                                imgMyThumbUp.setImageResource(R.drawable.thumbs_up_filled_small);
                            }
                        } else {
                            layoutWriteShortReview.setVisibility(View.VISIBLE);
                            layoutMyShortReview.setVisibility(View.INVISIBLE);
                        }


                        dataLists.clear();
                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject dataObj = dataJsonArray.getJSONObject(i);
                            dataLists.add(new ReviewDataList(dataObj.getString("_id"), dataObj.getString("movie_id"), dataObj.getString("movie_name"),
                                    dataObj.getString("user_id"), mbtiList[dataObj.getInt("user_mbti")], dataObj.getString("user_nickname"),
                                    dataObj.getString("writing"), Float.valueOf(dataObj.getInt("star")), likeArray.getString(i), isLikeArray.getString(i)));
                        }
                        shortReviewInDetailRecyclerViewAdapter.notifyDataSetChanged();
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
    private void modifyReview(String writing, float star){
        Log.d(TAG, "modifyReview:");
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginData", Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("boardID", myReview.getWritingId());
            jsonObject.put("writing", writing);
            jsonObject.put("star",ratingBarInShortReview.getRating());
            Log.d(TAG, "modifyReview: token:"+sharedPreferences.getString("token", "")+" id:"+myReview.getWritingId()+"  writing" + writing+"  star:"+ratingBarInShortReview.getRating());

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.modifyShortReview);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: modify : res" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        shortReviewListRequest(movCode);
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
    private void deleteReview(){
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginData", Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("boardID", myReview.getWritingId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.deleteShortReview);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: delete: res" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        shortReviewListRequest(movCode);
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
