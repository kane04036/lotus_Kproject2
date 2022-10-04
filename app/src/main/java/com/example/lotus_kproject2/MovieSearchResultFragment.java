package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

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

public class MovieSearchResultFragment extends Fragment {
    MovieRecyclerViewAdapter recyclerViewAdapter;
    LongReviewRecyclerViewAdapter longReviewAdapter;

    EditText edtSearchInMovieResult;
    RecyclerView movieRecyclerView, longReivewRecyView;
    FrameLayout frameLayoutInMovieResult2;

    ArrayList<String> imgArray = new ArrayList<>();
    ArrayList<String> nameArray = new ArrayList<>();
    ArrayList<String> codeArray = new ArrayList<>();

    ArrayList<String> reviewIdArray = new ArrayList<>();
    ArrayList<String> movieCodeArray = new ArrayList<>();
    ArrayList<Integer> starArray = new ArrayList<>();
    ArrayList<String> userIdArray = new ArrayList<>();
    ArrayList<String> titleArray = new ArrayList<>();
    ArrayList<String> writingArray = new ArrayList<>();

    String searchWord;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("searchWord", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                searchWord = result.getString("searchWord");
                edtSearchInMovieResult.setText(searchWord);
                movieSearchRequest(searchWord);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_search_result, container, false);

        edtSearchInMovieResult = view.findViewById(R.id.edtSearchInMovieResult);;
        movieRecyclerView = view.findViewById(R.id.movieRecyclerView);
        frameLayoutInMovieResult2 = view.findViewById(R.id.frameLayoutInMovieResult2);
        longReivewRecyView = view.findViewById(R.id.recyViewLongReviewInResult);
//        shortReviewRecyView = view.findViewById(R.id.recyViewShortReviewInResult);

        recyclerViewAdapter = new MovieRecyclerViewAdapter(getActivity(), nameArray, imgArray, codeArray);
        longReviewAdapter = new LongReviewRecyclerViewAdapter(getActivity(), reviewIdArray, movieCodeArray, starArray, userIdArray, titleArray, writingArray );
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        movieRecyclerView.setAdapter(recyclerViewAdapter);
        longReivewRecyView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        longReivewRecyView.setAdapter(longReviewAdapter);



        return view;


    }


    void movieSearchRequest(String textSearch) {
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", textSearch);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.movieSearch);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: res:" + response.getString("res"));
                    Log.d(TAG, "onResponse: data"+response.getString("data"));

                    if(response.getString("res").equals("200")) {
                        nameArray.clear();
                        imgArray.clear();
                        codeArray.clear();
                        JSONArray dataJsonArray = response.getJSONArray("data");
                        JSONArray movieJsonArray = (JSONArray) dataJsonArray.get(0);
                        int movNum = (int) movieJsonArray.get(0);
                        JSONArray movNameJsonArray = movieJsonArray.getJSONArray(1);
                        JSONArray movCodeJsonArray = movieJsonArray.getJSONArray(2);
                        JSONArray movImgJsonArray = movieJsonArray.getJSONArray(3);
                        for(int i = 0; i < movNum; i++){
                            nameArray.add(String.valueOf(movNameJsonArray.get(i)));
                            imgArray.add(String.valueOf(movImgJsonArray.get(i)));
                            codeArray.add(String.valueOf(movCodeJsonArray.get(i)));
                            Log.d(TAG, "onResponse: nameArray"+nameArray.get(i));
                        }
                        recyclerViewAdapter.notifyDataSetChanged();

                        JSONArray movDateJsonArray = movieJsonArray.getJSONArray(4);
                        JSONArray longReviewJsonArray = dataJsonArray.getJSONArray(1);
                        JSONObject longReviewJsonObj1 = longReviewJsonArray.getJSONObject(0);
                        Log.d(TAG, "onResponse: longreview_ttile:"+longReviewJsonObj1.getString("title"));
                        JSONArray shortReviewJsonArray = dataJsonArray.getJSONArray(2);
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
