package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private MovieRecyclerViewAdapter recyclerViewAdapter;
    private LongReviewRecyclerViewAdapter longReviewAdapter;
    private LongReviewRecyclerViewAdapter shortReviewAdapter;

    private EditText edtSearchInMovieResult;
    private ImageButton imgBtnMoreMovie, imgBtnMoreLongReview, imgBtnMoreShortReview;
    private RecyclerView movieRecyclerView, longReivewRecyView, shortReviewRecyView;
    private FrameLayout frameLayoutInMovieResult2;

    private ArrayList<String> imgArray = new ArrayList<>();
    private ArrayList<String> nameArray = new ArrayList<>();
    private ArrayList<String> codeArray = new ArrayList<>();

    private ArrayList<String> reviewIdArray = new ArrayList<>();
    private ArrayList<String> movieCodeArray = new ArrayList<>();
    private ArrayList<String> movieNameArray = new ArrayList<>();
    private ArrayList<String> userIdArray = new ArrayList<>();
    private ArrayList<String> titleArray = new ArrayList<>();

    private ArrayList<String> reviewIdArray_short = new ArrayList<>();
    private ArrayList<String> movieCodeArray_short = new ArrayList<>();
    private ArrayList<String> movieNameArray_short = new ArrayList<>();
    private ArrayList<String> userIdArray_short = new ArrayList<>();
    private ArrayList<String> titleArray_short = new ArrayList<>();

    private String searchWord;

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

        edtSearchInMovieResult = view.findViewById(R.id.edtSearchInMovieResult);
        ;
        movieRecyclerView = view.findViewById(R.id.movieRecyclerView);
        frameLayoutInMovieResult2 = view.findViewById(R.id.frameLayoutInMovieResult2);
        longReivewRecyView = view.findViewById(R.id.recyViewLongReviewInResult);
        shortReviewRecyView = view.findViewById(R.id.recyViewShortReviewInResult);
        imgBtnMoreMovie = view.findViewById(R.id.imgBtnMoreMovie);
        imgBtnMoreLongReview = view.findViewById(R.id.imgBtnMoreLongReview);
        imgBtnMoreShortReview = view.findViewById(R.id.imgBtnMoreShortReview);


        recyclerViewAdapter = new MovieRecyclerViewAdapter(getActivity(), nameArray, imgArray, codeArray);
        longReviewAdapter = new LongReviewRecyclerViewAdapter(getActivity(), reviewIdArray, movieCodeArray, userIdArray, titleArray, movieNameArray);
        shortReviewAdapter = new LongReviewRecyclerViewAdapter(getActivity(), reviewIdArray_short, movieCodeArray_short, userIdArray_short, titleArray_short, movieNameArray_short);

        movieRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        movieRecyclerView.setAdapter(recyclerViewAdapter);

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

        imgBtnMoreLongReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoreListActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("keyword",searchWord);
                startActivity(intent);
            }
        });

        imgBtnMoreShortReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoreListActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("keyword",searchWord);
                startActivity(intent);
            }
        });


        return view;


    }

    private void requestShortReviewList() {
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tp", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.report);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: report: res" + response.getString("res"));


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

    private void requestLongReviewList() {
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tp", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.report);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: report: res" + response.getString("res"));


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

    private void requestMovieList() {
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tp", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.report);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: report: res" + response.getString("res"));


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

    private void movieSearchRequest(String textSearch) {
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
                    if (response.getString("res").equals("200")) {
                        nameArray.clear();
                        imgArray.clear();
                        codeArray.clear();
                        JSONArray dataJsonArray = response.getJSONArray("data");
                        Log.d(TAG, "onResponse: datajson" + dataJsonArray);
                        JSONArray movieJsonArray = (JSONArray) dataJsonArray.get(0);
                        int movNum = (int) movieJsonArray.get(0);
                        JSONArray movNameJsonArray = movieJsonArray.getJSONArray(1);
                        JSONArray movCodeJsonArray = movieJsonArray.getJSONArray(2);
                        JSONArray movImgJsonArray = movieJsonArray.getJSONArray(3);
                        JSONArray movDateJsonArray = movieJsonArray.getJSONArray(4);
                        for (int i = 0; i < movNum; i++) {
                            nameArray.add(String.valueOf(movNameJsonArray.get(i)));
                            imgArray.add(String.valueOf(movImgJsonArray.get(i)));
                            codeArray.add(String.valueOf(movCodeJsonArray.get(i)));
                        }
                        Log.d(TAG, "onResponse: arraysize(name, img, code)" + nameArray.size() + "," + imgArray.size() + "+" + codeArray.size());
                        Log.d(TAG, "onResponse: firstCode:" + codeArray.get(0));
                        Log.d(TAG, "onResponse: firstName:" + nameArray.get(0));
                        Log.d(TAG, "onResponse: firstCode:" + codeArray.get(0));

                        recyclerViewAdapter.notifyDataSetChanged();

                        JSONArray longReviewJsonArray = dataJsonArray.getJSONArray(1);
                        JSONArray shortReviewJsonArray = dataJsonArray.getJSONArray(2);

                        reviewIdArray.clear();
                        titleArray.clear();
                        movieNameArray.clear();
                        movieCodeArray.clear();
                        userIdArray.clear();
                        for (int i = 0; i < longReviewJsonArray.length(); i++) {
                            JSONObject reviewJsonObj = longReviewJsonArray.getJSONObject(i);
                            reviewIdArray.add(reviewJsonObj.getString("_id"));
                            titleArray.add(reviewJsonObj.getString("title"));
                            movieNameArray.add(reviewJsonObj.getString("movie_name"));
                            movieCodeArray.add(reviewJsonObj.getString("movie_id"));
                            userIdArray.add(reviewJsonObj.getString("user_id"));
                        }
                        longReviewAdapter.notifyDataSetChanged();

                        reviewIdArray_short.clear();
                        titleArray_short.clear();
                        movieNameArray_short.clear();
                        movieCodeArray_short.clear();
                        userIdArray_short.clear();
                        for (int i = 0; i < shortReviewJsonArray.length(); i++) {
                            JSONObject reviewJsonObj = shortReviewJsonArray.getJSONObject(i);
                            reviewIdArray_short.add(reviewJsonObj.getString("_id"));
                            titleArray_short.add(reviewJsonObj.getString("writing"));
                            movieNameArray_short.add(reviewJsonObj.getString("movie_name"));
                            movieCodeArray_short.add(reviewJsonObj.getString("movie_id"));
                            userIdArray_short.add(reviewJsonObj.getString("user_id"));
                        }
                        shortReviewAdapter.notifyDataSetChanged();

                        hideKeyboard();

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
}



