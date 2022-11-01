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

import java.util.ArrayList;

public class LongReviewInMyBlogFragment extends Fragment {
    String userId;
    ArrayList<ReviewDataList> dataLists = new ArrayList<>();
    private RecyclerView recyclerView;
    private LongReviewInMyBlogRecyclerViewAdapter adapter = new LongReviewInMyBlogRecyclerViewAdapter(dataLists);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("userData_long", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                userId = result.getString("userId");
            }
        });
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        return view;
    }

    void longReviewRequest(String userId) {
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.viewLongReviewUserId);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "long review in blog|res:" + response.getString("res"));
                    JSONArray dataJsonArray = response.getJSONArray("data");
                    if (response.getString("res").equals("200")) {
                        dataLists.clear();
                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject dataObj = dataJsonArray.getJSONObject(i);

                            Resources res = getResources();
                            String[] mbtiList = res.getStringArray(R.array.mbti_array);

                            dataLists.add(new ReviewDataList(dataObj.getString("_id"), dataObj.getString("movie_id"),
                                    dataObj.getString("movie_name"), dataObj.getString("title"), dataObj.getString("user_id"),
                                    mbtiList[dataObj.getInt("user_mbti")],dataObj.getString("user_nickname"),dataObj.getString("writing")));
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
