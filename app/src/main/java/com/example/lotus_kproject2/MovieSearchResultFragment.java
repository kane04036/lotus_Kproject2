package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.OnBackPressedDispatcher;
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
    private MovieRecyclerViewAdapter movieAdapter;
    private LongReviewInSearchResultRecyclerAdapter longReviewAdapter;
    private ShortReviewInSearchResultRecyclerViewAdapter shortReviewAdapter;

    private EditText edtSearchInMovieResult;
    private ImageView imgBtnMoreMovie, imgBtnMoreLongReview, imgBtnMoreShortReview;
    private RecyclerView movieRecyclerView, longReivewRecyView, shortReviewRecyView;

    private ArrayList<ReviewDataList> shortReviewDataList = new ArrayList<>();
    private ArrayList<ReviewDataList> longReviewDataList = new ArrayList<>();
    private ArrayList<MovieDataList> movieDataList = new ArrayList<>();

    private String searchWord;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("searchWord", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                searchWord = result.getString("searchWord");
                edtSearchInMovieResult.setText(searchWord);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        movieSearchRequest(searchWord);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_search_result, container, false);

        edtSearchInMovieResult = view.findViewById(R.id.edtSearchInMovieResult);
        movieRecyclerView = view.findViewById(R.id.movieRecyclerView);
        longReivewRecyView = view.findViewById(R.id.recyViewLongReviewInResult);
        shortReviewRecyView = view.findViewById(R.id.recyViewShortReviewInResult);
        imgBtnMoreMovie = view.findViewById(R.id.imgBtnMoreMovie);
        imgBtnMoreLongReview = view.findViewById(R.id.imgBtnMoreLongReview);
        imgBtnMoreShortReview = view.findViewById(R.id.imgBtnMoreShortReview);


        movieAdapter = new MovieRecyclerViewAdapter(getActivity(), movieDataList);
        longReviewAdapter = new LongReviewInSearchResultRecyclerAdapter(getActivity(), longReviewDataList);
        shortReviewAdapter = new ShortReviewInSearchResultRecyclerViewAdapter(getActivity(), shortReviewDataList);

        movieRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        movieRecyclerView.setAdapter(movieAdapter);

        longReivewRecyView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        longReivewRecyView.setAdapter(longReviewAdapter);

        shortReviewRecyView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        shortReviewRecyView.setAdapter(shortReviewAdapter);

        edtSearchInMovieResult.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == keyEvent.ACTION_DOWN) && (keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) {
                    movieSearchRequest(edtSearchInMovieResult.getText().toString());
                    Log.d(TAG, "onKey: enter click");
                    return true;
                }
                return false;
            }
        });

        imgBtnMoreMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoreListActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("keyword", searchWord);
                startActivity(intent);
            }
        });

        imgBtnMoreLongReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoreListActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("keyword", searchWord);
                startActivity(intent);
            }
        });

        imgBtnMoreShortReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoreListActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("keyword", searchWord);
                startActivity(intent);
            }
        });


        return view;


    }

    private void movieSearchRequest(String textSearch) {
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("name", textSearch);
            jsonObject.put("token", sharedPreferences.getString("token", ""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.movieSearch);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("res").equals("200")) {
                        longReviewDataList.clear();
                        shortReviewDataList.clear();
                        movieDataList.clear();

                        JSONArray movieDataArray = response.getJSONArray("movie_data");
                        JSONArray movNameArray = movieDataArray.getJSONArray(1);
                        JSONArray movImgArray = movieDataArray.getJSONArray(3);
                        JSONArray movDateArray = movieDataArray.getJSONArray(4);
                        JSONArray movCodeArray = movieDataArray.getJSONArray(2);
                        for (int i = 0; i < movCodeArray.length(); i++) {
                            movieDataList.add(new MovieDataList(movCodeArray.getString(i), movNameArray.getString(i), movImgArray.getString(i), movDateArray.getString(i)));
                        }
                        movieAdapter.notifyDataSetChanged();

                        if (getActivity() == null) {
                            return;
                        }
                        Resources res = getResources();
                        String[] mbtiList = res.getStringArray(R.array.mbti_array);

                        JSONArray longReviewArray = response.getJSONArray("long_board");
                        JSONArray longReviewLikeArray = response.getJSONArray("longLike");
                        JSONArray longReviewIsLikeArray = response.getJSONArray("long_isLike");
                        for (int i = 0; i < longReviewArray.length(); i++) {
                            JSONObject jsonObject = longReviewArray.getJSONObject(i);
                            longReviewDataList.add(new ReviewDataList(jsonObject.getString("_id"), jsonObject.getString("movie_id"), jsonObject.getString("movie_name"),
                                    jsonObject.getString("title"), jsonObject.getString("user_id"), mbtiList[jsonObject.getInt("user_mbti")], jsonObject.getString("user_nickname"),
                                    jsonObject.getString("writing"), longReviewLikeArray.getInt(i), longReviewIsLikeArray.getString(i)));
                        }
                        longReviewAdapter.notifyDataSetChanged();

                        JSONArray shortReviewArray = response.getJSONArray("short_board");
                        JSONArray shortReviewLikeArray = response.getJSONArray("shortLike");
                        JSONArray shortReviewIsLikeArray = response.getJSONArray("short_isLike");
                        for (int i = 0; i < shortReviewArray.length(); i++) {
                            JSONObject jsonObject = shortReviewArray.getJSONObject(i);
                            shortReviewDataList.add(new ReviewDataList(jsonObject.getString("_id"), jsonObject.getString("movie_id"), jsonObject.getString("movie_name"),
                                    jsonObject.getString("user_id"), mbtiList[jsonObject.getInt("user_mbti")], jsonObject.getString("user_nickname"),
                                    jsonObject.getString("writing"), Float.valueOf(jsonObject.getInt("star")), shortReviewLikeArray.getInt(i), shortReviewIsLikeArray.getString(i)));
                        }
                        shortReviewAdapter.notifyDataSetChanged();

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

    private void hideKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    private void requestShortReviewList() {
//        RequestQueue Queue = Volley.newRequestQueue(getActivity());
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("tp", "0");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String URL = getString(R.string.server) + getString(R.string.report);
//
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    Log.d(TAG, "onResponse: report: res" + response.getString("res"));
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        Queue.add(jsonObjectRequest);
//    }
//
//    private void requestLongReviewList() {
//        RequestQueue Queue = Volley.newRequestQueue(getActivity());
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("tp", "0");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String URL = getString(R.string.server) + getString(R.string.report);
//
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    Log.d(TAG, "onResponse: report: res" + response.getString("res"));
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        Queue.add(jsonObjectRequest);
//    }
//
//    private void requestMovieList() {
//        RequestQueue Queue = Volley.newRequestQueue(getActivity());
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("tp", "0");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String URL = getString(R.string.server) + getString(R.string.report);
//
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    Log.d(TAG, "onResponse: report: res" + response.getString("res"));
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        Queue.add(jsonObjectRequest);
//
//
//    }


}



