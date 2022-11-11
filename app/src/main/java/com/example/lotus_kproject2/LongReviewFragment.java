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

public class LongReviewFragment extends Fragment {
    private RecyclerView recyclerView;
    ArrayList<ReviewDataList> dataLists = new ArrayList<>();
    private LongReviewBoardRecyclerViewAdapter adapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_longreview_board, container, false);

        recyclerView = view.findViewById(R.id.recyViewLongReviewBoard);
        adapter = new LongReviewBoardRecyclerViewAdapter(getActivity(), dataLists);
        progressBar = view.findViewById(R.id.progressInLongBoard);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        longReviewDataRequest();

        return view;
    }

    private void longReviewDataRequest(){
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.loginData),Context.MODE_PRIVATE);
            jsonObject.put("token",sharedPreferences.getString("token","") );
            Log.d(TAG, "longReviewDataRequest: token: "+sharedPreferences.getString("token",""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.viewLongReviewRecency);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if(response.getString("res").equals("200")){
                        progressBar.setVisibility(View.INVISIBLE);
                        JSONArray dataJsonArray = response.getJSONArray("data");
                        JSONArray likeArray = response.getJSONArray("like");
                        JSONArray isLikeArray = response.getJSONArray("isLike");
                        JSONArray movieInfoArray = response.getJSONArray("movie_info");
                        Log.d(TAG, "onResponse: isLike"+isLikeArray);

                        dataLists.clear();
                        for(int i = 0; i<dataJsonArray.length(); i++){
                            JSONObject object = dataJsonArray.getJSONObject(i);
                            JSONArray movieInfoObject = movieInfoArray.getJSONArray(i);

                            int mbtiNum = object.getInt("user_mbti");

                            if(getActivity() == null){
                                return;
                            }
                            Resources res = getResources();
                            String[] mbtiArray = res.getStringArray(R.array.mbti_array);

                            MovieDataList movieData = new MovieDataList(movieInfoObject.getString(2),movieInfoObject.getString(1),
                                    movieInfoObject.getString(3),movieInfoObject.getString(4));

                            dataLists.add(new ReviewDataList(object.getString("_id"), object.getString("movie_id"), object.getString("movie_name"),
                                    object.getString("title"), object.getString("user_id"), mbtiArray[mbtiNum],
                                    object.getString("user_nickname"), object.getString("writing"), likeArray.getString(i), movieData,isLikeArray.getString(i)));
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
