package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;

public class LongReviewInDetailFragment extends Fragment {
    RecyclerView recyclerView;
    LongReviewInDetailRecyclerViewAdapter longReviewInDetailRecyclerViewAdapter;
    String movCode;

    private ArrayList<String> nicknameArray = new ArrayList<>();
    private ArrayList<String> mbtiArray = new ArrayList<>();
    private ArrayList<String> writingArray = new ArrayList<>();
    private ArrayList<String> thumbUpArray = new ArrayList<>();
    private ArrayList<String> movCodeArray = new ArrayList<>();
    private ArrayList<String> writingIdArray = new ArrayList<>();
    private ArrayList<String> userIdArray = new ArrayList<>();
    private ArrayList<String> titleArray = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("movData_long", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                movCode = result.getString("movCode");
                Log.d(TAG, "ChildFragment longReview : movCode:"+movCode);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        longReviewListRequest(movCode);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_layout_long_review, container, false);



        recyclerView = view.findViewById(R.id.recyViewLongReviewInDetail);
        longReviewInDetailRecyclerViewAdapter = new LongReviewInDetailRecyclerViewAdapter(getActivity(), mbtiArray, nicknameArray,
                writingArray, titleArray, thumbUpArray, movCodeArray, writingIdArray, userIdArray);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(longReviewInDetailRecyclerViewAdapter);


        return view;
    }

    private void longReviewListRequest(String movCode) {
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("movie_id", movCode);
            Log.d(TAG, "longReviewListRequest: movieId"+movCode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.longReviewViewInMovie);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "long Review View with movie_id: res:" + response.getString("res"));
                    JSONArray dataJsonArray = response.getJSONArray("data");
                    Log.d(TAG, "onResponse: long review list:" + dataJsonArray);
                    if (response.getString("res").equals("200")) {
                        Resources res = getResources();
                        String[] mbtiList = res.getStringArray(R.array.mbti_array);

                        writingArray.clear();
                        movCodeArray.clear();
                        writingIdArray.clear();
                        userIdArray.clear();
                        nicknameArray.clear();
                        mbtiArray.clear();
                        titleArray.clear();
                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject dataObj = dataJsonArray.getJSONObject(i);

                            writingIdArray.add(dataObj.getString("_id"));
                            movCodeArray.add(dataObj.getString("movie_id"));
                            writingArray.add(dataObj.getString("writing"));
                            userIdArray.add(dataObj.getString("user_id"));
                            nicknameArray.add(dataObj.getString("user_nickname"));
                            titleArray.add(dataObj.getString("title"));
                            mbtiArray.add(mbtiList[dataObj.getInt("user_mbti")]);

                        }
                        Log.d(TAG, "onResponse: longreview title.size():" + titleArray.size());
//                        longReviewInDetailRecyclerViewAdapter.notifyDataSetChanged();
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
