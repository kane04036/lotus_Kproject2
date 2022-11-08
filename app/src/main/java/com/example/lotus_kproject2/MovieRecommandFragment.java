package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MovieRecommandFragment extends Fragment {
    ArrayList<String> directorRecmdMovCodeArray = new ArrayList();
    ArrayList<String> actorRecmdMovCodeArray = new ArrayList();
    //    ArrayList<RecommendMovieList> director_Recommend_movieLists = new ArrayList<>();
//    ArrayList<RecommendMovieList> actor_Recommend_movieLists = new ArrayList<>();
    ArrayList<RecommendMovieList> recmdMovLists = new ArrayList<>();

    String token;
    int position = 1;

    ImageView imgMovMain1, imgMovMain2, imgMovMain3, imgNext, imgPre, imgMovSub1, imgMovSub2, imgMovSub3;
    private TextView tvMovTitleMain, tvMovGenreMain, tvMovNameSub1, tvMovNameSub2, tvMovNameSub3,tvMbti;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recommendMovRequest();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movierecommand, container, false);

        imgMovMain1 = view.findViewById(R.id.imgMovMain1);
        imgMovMain2 = view.findViewById(R.id.imgMovMain2);
        imgMovMain3 = view.findViewById(R.id.imgMovMain3);
        imgNext = view.findViewById(R.id.imgNext);
        imgPre = view.findViewById(R.id.imgPre);
        tvMovTitleMain = view.findViewById(R.id.tvMovTitleMain);
        tvMovGenreMain = view.findViewById(R.id.tvMovGenreMain);
        imgMovSub1 = view.findViewById(R.id.imgMovSub1);
        imgMovSub2 = view.findViewById(R.id.imgMovSub2);
        imgMovSub3 = view.findViewById(R.id.imgMovSub3);
        tvMovNameSub1 = view.findViewById(R.id.tvMovNameSub1);
        tvMovNameSub2 = view.findViewById(R.id.tvMovNameSub2);
        tvMovNameSub3 = view.findViewById(R.id.tvMovNameSub3);
        tvMbti = view.findViewById(R.id.tvMbtiInHome);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.userData), Context.MODE_PRIVATE);
        tvMbti.setText(sharedPreferences.getString("mbti",""));

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int len = recmdMovLists.size()-1;
                position++;

                Glide.with(getActivity()).load(recmdMovLists.get(position%len).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain1);
                Glide.with(getActivity()).load(recmdMovLists.get((position + 1)%len).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain2);
                Glide.with(getActivity()).load(recmdMovLists.get((position + 2)%len).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain3);
                tvMovTitleMain.setText(recmdMovLists.get((position + 1)%len).getMovName());
                tvMovGenreMain.setText(recmdMovLists.get((position + 1)%len).getMovGenre());

            }
        });
        imgPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int len = recmdMovLists.size()-1;
                position--;
                if(position < 0)
                    position = len;

                Glide.with(getActivity()).load(recmdMovLists.get(position%len).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain1);
                Glide.with(getActivity()).load(recmdMovLists.get((position + 1)%len).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain2);
                Glide.with(getActivity()).load(recmdMovLists.get((position + 2)%len).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain3);
                tvMovTitleMain.setText(recmdMovLists.get((position + 1)%len).getMovName());
                tvMovGenreMain.setText(recmdMovLists.get((position + 1)%len).getMovGenre());

            }
        });

        return view;
    }

    private void recommendMovRequest() {
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("userNum", sharedPreferences.getString("memNum", ""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.recommendMov);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: res: recommandMov: " + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        JSONArray dataJsonArray = response.getJSONArray("data");
                        recmdMovLists.clear();
                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONArray tmpJsonArray = dataJsonArray.getJSONArray(i);
                            recmdMovLists.add(new RecommendMovieList( tmpJsonArray.getString(0),  tmpJsonArray.getString(1),  tmpJsonArray.getString(2),  tmpJsonArray.getString(3)));
                        }

                        Glide.with(getActivity()).load(recmdMovLists.get(0).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain1);
                        Glide.with(getActivity()).load(recmdMovLists.get(1).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain2);
                        Glide.with(getActivity()).load(recmdMovLists.get(2).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain3);
                        tvMovTitleMain.setText(recmdMovLists.get(1).getMovName());
                        tvMovGenreMain.setText(recmdMovLists.get(1).getMovGenre());

                        Glide.with(getActivity()).load(recmdMovLists.get(3).getMovImg()).error(R.drawable.gray_profile).into(imgMovSub1);
                        Glide.with(getActivity()).load(recmdMovLists.get(4).getMovImg()).error(R.drawable.gray_profile).into(imgMovSub2);
                        Glide.with(getActivity()).load(recmdMovLists.get(5).getMovImg()).error(R.drawable.gray_profile).into(imgMovSub3);
                        tvMovNameSub1.setText(recmdMovLists.get(3).getMovName());
                        tvMovNameSub2.setText(recmdMovLists.get(4).getMovName());
                        tvMovNameSub3.setText(recmdMovLists.get(5).getMovName());
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
