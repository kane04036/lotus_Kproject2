package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

public class LongReviewInMyBlogFragment extends Fragment {
    private String userId;
    private ArrayList<ReviewDataList> dataLists = new ArrayList<>();
    private RecyclerView recyclerView;
    private LongReviewInMyBlogRecyclerViewAdapter adapter;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("userData_long", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                userId = result.getString("userId");
            }
        });
        adapter = new LongReviewInMyBlogRecyclerViewAdapter(getActivity(), dataLists);
    }
    @Override
    public void onResume() {
        super.onResume();
        longReviewRequest(userId);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_longreview_board, container, false);

        recyclerView = view.findViewById(R.id.recyViewLongReviewBoard);
        progressBar = view.findViewById(R.id.progressInLongBoard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        return view;
    }

    void longReviewRequest(String userId) {
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("user_id", userId);
            jsonObject.put("token",sharedPreferences.getString("token",""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.viewLongReviewUserId);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "long review in blog|res:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        progressBar.setVisibility(View.INVISIBLE);
                        JSONArray dataJsonArray = response.getJSONArray("data");
                        JSONArray likeArray = response.getJSONArray("like");
                        JSONArray isLikeArray = response.getJSONArray("isLike");
                        JSONArray movieInfoArray = response.getJSONArray("movie_info");

                        dataLists.clear();
                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject dataObj = dataJsonArray.getJSONObject(i);
                            JSONArray movieInfoObject = movieInfoArray.getJSONArray(i);

                            if(getActivity() == null){
                                return;
                            }
                            Resources res = getResources();
                            String[] mbtiList = res.getStringArray(R.array.mbti_array);

                            MovieDataList movieData = new MovieDataList(movieInfoObject.getString(2),movieInfoObject.getString(1),
                                    movieInfoObject.getString(3),movieInfoObject.getString(4));


                            dataLists.add(new ReviewDataList(dataObj.getString("_id"), dataObj.getString("movie_id"),
                                    dataObj.getString("movie_name"), dataObj.getString("title"), dataObj.getString("user_id"),
                                    mbtiList[dataObj.getInt("user_mbti")],dataObj.getString("user_nickname"),
                                    dataObj.getString("writing"), likeArray.getInt(i), movieData, isLikeArray.getString(i)));
                        }
                        adapter.notifyDataSetChanged();
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
