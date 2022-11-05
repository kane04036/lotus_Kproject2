package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShortReviewInMyBlogFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<ReviewDataList> dataLists = new ArrayList<>();
    private ShortReviewInMyBlogRecyclerViewAdapter adapter;
    String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("userData_short", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                userId = result.getString("userId");
            }
        });

        adapter = new ShortReviewInMyBlogRecyclerViewAdapter(getActivity(), dataLists);
    }

    @Override
    public void onResume() {
        super.onResume();
        shortReviewRequest(userId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shortreview_board, container, false);

        recyclerView = view.findViewById(R.id.recyViewShortReviewBoard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void shortReviewRequest(String userId) {

        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.viewShortReviewUserId);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: short review myblog request:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        JSONArray dataJsonArray = response.getJSONArray("data");
                        JSONArray likeArray = response.getJSONArray("like");
                        JSONArray movieInfoArray = response.getJSONArray("movie_info");
                        Log.d(TAG, "onResponse: short review dataJsonarray size:" + dataJsonArray.length());
                        dataLists.clear();
                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject object = dataJsonArray.getJSONObject(i);
                            Log.d(TAG, "onResponse: short review object" + object);
                            JSONArray movieInfoObject = movieInfoArray.getJSONArray(i);
                            MovieDataList movieData = new MovieDataList(movieInfoObject.getString(2), movieInfoObject.getString(1),
                                    movieInfoObject.getString(3), movieInfoObject.getString(4));

                            int mbtiNum = object.getInt("user_mbti");
                            Resources res = getResources();
                            String[] mbtiArray = res.getStringArray(R.array.mbti_array);

                            float star = Float.parseFloat(object.getString("star"));


                            dataLists.add(new ReviewDataList(object.getString("_id"), object.getString("movie_id"), object.getString("movie_name"),
                                    object.getString("user_id"), mbtiArray[mbtiNum],
                                    object.getString("user_nickname"), object.getString("writing"), star, likeArray.getString(i), movieData));

                            Log.d(TAG, "onResponse: short review size in loop" + dataLists.size());

                        }
                        Log.d(TAG, "onResponse: short review count:" + dataLists.size());
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
