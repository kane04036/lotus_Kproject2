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
import android.widget.Toast;

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
import com.bumptech.glide.load.engine.GlideException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MovieRecommandFragment extends Fragment {
    ArrayList<String> directorRecmdMovCodeArray = new ArrayList();
    ArrayList<String> actorRecmdMovCodeArray = new ArrayList();
    ArrayList<MovieList> director_movieLists = new ArrayList<>();
    ArrayList<MovieList> actor_movieLists = new ArrayList<>();
    String token;
    int random;

    ImageView imgMovMain1, imgMovMain2, imgMovMain3, imgNext, imgPre, imgMovSub1, imgMovSub2, imgMovSub3;
    TextView tvMovTitleMain, tvMovGenreMain, tvMovNameSub1, tvMovNameSub2, tvMovNameSub3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                recommendMovRequest();
//            }
//        };
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//        int min = 1;
//        int max = 8;
//        Log.d(TAG, "movielist.size" + director_movieLists.size());
//        int random = new Random().nextInt(director_movieLists.size() - 3);
//        Glide.with(getActivity()).load(director_movieLists.get(random).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain1);
//        Glide.with(getActivity()).load(director_movieLists.get(random + 1).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain2);
//        Glide.with(getActivity()).load(director_movieLists.get(random + 2).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain3);
//        tvMovTitleMain.setText(director_movieLists.get(random + 1).getMovName());
//        tvMovGenreMain.setText(director_movieLists.get(random + 1).getMovGenre());
//
//        random = new Random().nextInt(actor_movieLists.size() - 3);
//        Glide.with(getActivity()).load(actor_movieLists.get(random).getMovImg()).error(R.drawable.gray_profile).into(imgMovSub1);
//        Glide.with(getActivity()).load(actor_movieLists.get(random + 1).getMovImg()).error(R.drawable.gray_profile).into(imgMovSub2);
//        Glide.with(getActivity()).load(actor_movieLists.get(random + 2).getMovImg()).error(R.drawable.gray_profile).into(imgMovSub3);
//        tvMovNameSub1.setText(actor_movieLists.get(random).getMovName());
//        tvMovNameSub2.setText(actor_movieLists.get(random + 1).getMovName());
//        tvMovNameSub3.setText(actor_movieLists.get(random + 2).getMovName());


        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recommendMovRequest();
    }

    private void recommendMovRequest() {
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            token = sharedPreferences.getString("token", "");
            jsonObject.put("token", token);
            jsonObject.put("userNum", sharedPreferences.getString("memNum", ""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.recommendMov);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: res: recency" + response.getString("res"));
                    Log.d(TAG, "onResponse: data" + response.getString("data"));
                    JSONArray dataJsonArray = response.getJSONArray("data");
                    JSONArray directorRecmdJsonArray = dataJsonArray.getJSONArray(0);
                    JSONArray actorRecmdJsonArray = dataJsonArray.getJSONArray(1);
                    for (int i = 0; i < directorRecmdJsonArray.length(); i++) {
//                        directorRecmdMovCodeArray.add(directorRecmdJsonArray.getString(i));
//                        actorRecmdMovCodeArray.add(actorRecmdJsonArray.getString(i));
                        director_movieLists.add(new MovieList(directorRecmdJsonArray.getString(i)));
                        actor_movieLists.add(new MovieList(actorRecmdJsonArray.getString(i)));
                        movieDetailRequest(director_movieLists);
                        movieDetailRequest(actor_movieLists);

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

    private void movieDetailRequest(ArrayList<MovieList> movieList) {
        final String movName, movCode, movImg, movDate;
        for (int i = 0; i < movieList.size(); i++) {
            RequestQueue Queue = Volley.newRequestQueue(getActivity());

            JSONObject jsonObject = new JSONObject();
            try {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
                jsonObject.put("id", movieList.get(i).getMovCode());
                jsonObject.put("token", sharedPreferences.getString("token", ""));

            } catch (Exception e) {
                e.printStackTrace();
            }

            String URL = getString(R.string.server) + getString(R.string.movieDetail);


            int finalI = i;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d(TAG, "request in movieList: " + response.getString("res"));
                        if (response.getString("res").equals("200")) {
                            JSONArray dataJsonArrayAll = response.getJSONArray("data");
                            JSONArray dataJsonArray = dataJsonArrayAll.getJSONArray(1);
                            String movName = dataJsonArray.getString(0);
                            String runningTime = (String) dataJsonArray.get(2);
                            String movImg = dataJsonArray.getString(3);
                            String country = (String) dataJsonArray.get(6);
                            String releaseData = (String) dataJsonArray.get(7);
                            String genres = dataJsonArray.getString(8);
                            genres = genres.replace(",", " | ");
                            String movReleaseDate = dataJsonArray.getString(7);
                            Log.d(TAG, "onResponse: Movie List Class: movName:" + movName);

                            movieList.get(finalI).setData(dataJsonArray.getString(0), dataJsonArray.getString(3), dataJsonArray.getString(7), genres);

                            int min = 1;
                            int max = 8;
                            Log.d(TAG, "movielist.size" + director_movieLists.size());
                            int random = new Random().nextInt(director_movieLists.size() - 3);
                            Glide.with(getActivity()).load(director_movieLists.get(random).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain1);
                            Glide.with(getActivity()).load(director_movieLists.get(random + 1).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain2);
                            Glide.with(getActivity()).load(director_movieLists.get(random + 2).getMovImg()).error(R.drawable.gray_profile).into(imgMovMain3);
                            tvMovTitleMain.setText(director_movieLists.get(random + 1).getMovName());
                            tvMovGenreMain.setText(director_movieLists.get(random + 1).getMovGenre());

                            random = new Random().nextInt(actor_movieLists.size() - 3);
                            Glide.with(getActivity()).load(actor_movieLists.get(random).getMovImg()).error(R.drawable.gray_profile).into(imgMovSub1);
                            Glide.with(getActivity()).load(actor_movieLists.get(random + 1).getMovImg()).error(R.drawable.gray_profile).into(imgMovSub2);
                            Glide.with(getActivity()).load(actor_movieLists.get(random + 2).getMovImg()).error(R.drawable.gray_profile).into(imgMovSub3);
                            tvMovNameSub1.setText(actor_movieLists.get(random).getMovName());
                            tvMovNameSub2.setText(actor_movieLists.get(random + 1).getMovName());
                            tvMovNameSub3.setText(actor_movieLists.get(random + 2).getMovName());

                            imgNext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d(TAG, "onClick: imgNext");
                                }
                            });

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
}
