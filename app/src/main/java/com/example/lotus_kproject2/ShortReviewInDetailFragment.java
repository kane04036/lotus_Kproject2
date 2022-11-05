package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private TextView tvWritingCount;
    private Button btnUploadShortReview;
    private String movCode;
    private RecyclerView recyViewShortReviewInDetail;
    private ShortReviewInDetailRecyclerViewAdapter shortReviewInDetailRecyclerViewAdapter;

    private ArrayList<ReviewDataList> dataLists = new ArrayList<>();

//    private ArrayList<String> nicknameArray = new ArrayList<>();
//    private ArrayList<String> mbtiArray = new ArrayList<>();
//    private ArrayList<String> writingArray = new ArrayList<>();
//    private ArrayList<Double> starArray = new ArrayList<>();
//    private ArrayList<String> thumbUpArray = new ArrayList<>();
//    private ArrayList<String> movCodeArray = new ArrayList<>();
//    private ArrayList<String> writingIdArray = new ArrayList<>();
//    private ArrayList<String> userIdArray = new ArrayList<>();

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

        recyViewShortReviewInDetail.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyViewShortReviewInDetail.setAdapter(shortReviewInDetailRecyclerViewAdapter);

        edtShortReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                tvWritingCount.setText(String.valueOf(edtShortReview.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnUploadShortReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadShortReview(movCode, edtShortReview.getText().toString(), ratingBarInShortReview.getRating());
            }
        });
        return view;
    }

    private void uploadShortReview(String movCode, String writing, float stars) {
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
                    edtShortReview.setText("");
                    ratingBarInShortReview.setRating(0);
                    edtShortReview.clearFocus();
                    shortReviewListRequest(movCode);
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
            jsonObject.put("movie_id", movCode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.shortReviewViewInMovie);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "short Review View with movie_id: res:" + response.getString("res"));
                    JSONArray dataJsonArray = response.getJSONArray("data");
                    JSONArray likeArray = response.getJSONArray("like");
                    if (response.getString("res").equals("200")) {
                        Resources res = getResources();
                        String[] mbtiList = res.getStringArray(R.array.mbti_array);
                        dataLists.clear();
                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject dataObj = dataJsonArray.getJSONObject(i);
                            dataLists.add(new ReviewDataList(dataObj.getString("_id"), dataObj.getString("movie_id"), dataObj.getString("movie_name"),
                                    dataObj.getString("user_id"), mbtiList[dataObj.getInt("user_mbti")], dataObj.getString("user_nickname"),
                                    dataObj.getString("writing"), Float.valueOf(dataObj.getInt("star")), likeArray.getString(i)));
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
}
