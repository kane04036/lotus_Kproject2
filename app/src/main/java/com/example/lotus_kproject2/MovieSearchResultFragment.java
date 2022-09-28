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

    EditText edtSearchInMovieResult;
    RecyclerView movieRecyclerView;
    FrameLayout frameLayoutInMovieResult2;

    ArrayList<String> imgArray = new ArrayList<>();
    ArrayList<String> nameArray = new ArrayList<>();
    ArrayList<String> codeArray = new ArrayList<>();

    String searchWord;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("searchWord", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                searchWord = result.getString("searchWord");
                edtSearchInMovieResult.setText(searchWord);
                movieThumbnailRequest(searchWord);
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

        recyclerViewAdapter = new MovieRecyclerViewAdapter(getActivity(), nameArray, imgArray, codeArray);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        movieRecyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.notifyDataSetChanged();

        return view;


    }


    public void movieThumbnailRequest(String textSearch) {
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", textSearch);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.url) + getString(R.string.thumbnail);
        Log.d(TAG, "movieSearch: url: " + URL);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("res").equals("200")) {
                        nameArray.clear();
                        imgArray.clear();
                        codeArray.clear();
                        Log.d(TAG, "onResponse: res:" + response.getString("res"));
                        Log.d(TAG, "onResponse: data:" + response.getString("data"));
                        JSONArray dataJsonArray = response.getJSONArray("data");
                        int numberOfMovie = (int)dataJsonArray.get(0);
                        Log.d(TAG, "onResponse: numberOfMovie: "+ numberOfMovie);
                        JSONArray nameJsonArray = (JSONArray) dataJsonArray.get(1);
                        JSONArray codeJsonArray = (JSONArray) dataJsonArray.get(2);
                        JSONArray imgJsonArray = (JSONArray) dataJsonArray.get(3);
                        for(int i = 0; i < numberOfMovie; i++){
                            nameArray.add(String.valueOf(nameJsonArray.get(i)));
                            imgArray.add(String.valueOf(imgJsonArray.get(i)));
                            codeArray.add(String.valueOf(codeJsonArray.get(i)));
                        }

                        recyclerViewAdapter.notifyDataSetChanged();
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
