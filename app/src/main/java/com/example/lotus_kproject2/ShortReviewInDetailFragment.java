package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ShortReviewInDetailFragment extends Fragment {
    MaterialRatingBar ratingBarInShortReview;
    EditText edtShortReview;
    TextView tvWritingCount;
    Button btnUploadShortReview;
    String movCode;
    RecyclerView recyViewShortReviewInDetail;
    ShortReviewInDetailRecyclerViewAdapter shortReviewInDetailRecyclerViewAdapter;

    private ArrayList<String> nicknameArray = new ArrayList<>();
    private ArrayList<String> mbtiArray = new ArrayList<>();
    private ArrayList<String> writingArray = new ArrayList<>();
    private ArrayList<Double> starArray = new ArrayList<>();
    private ArrayList<String> thumbUpArray = new ArrayList<>();
    private ArrayList<String> movCodeArray = new ArrayList<>();
    private ArrayList<String> writingIdArray = new ArrayList<>();
    private ArrayList<String> userIdArray = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("movData_short", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                movCode = result.getString("movCode");
                Log.d(TAG, "ChildFragment : movCode:"+movCode);
            }
        });

        shortReviewListRequest(movCode);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_layout_short_review, container, false);


        shortReviewInDetailRecyclerViewAdapter = new ShortReviewInDetailRecyclerViewAdapter(getActivity(),mbtiArray, nicknameArray, starArray,writingArray,
                thumbUpArray,movCodeArray,writingIdArray,userIdArray);

        ratingBarInShortReview = view.findViewById(R.id.ratingbarInShortReview);
        edtShortReview = view.findViewById(R.id.edtShortReview);
        tvWritingCount = view.findViewById(R.id.tvWritingCount);
        btnUploadShortReview = view.findViewById(R.id.btnUploadShortReview);
        recyViewShortReviewInDetail = view.findViewById(R.id.recyViewShortReviewInDetail);

        recyViewShortReviewInDetail.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recyViewShortReviewInDetail.setAdapter(shortReviewInDetailRecyclerViewAdapter);

        edtShortReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                tvWritingCount.setText(String.valueOf(edtShortReview.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnUploadShortReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadShortReview(movCode, edtShortReview.getText().toString(), ratingBarInShortReview.getRating());
            }
        });
        return view;
    }
    private void uploadShortReview(String movCode, String writing, float stars){
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);

            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("writing", writing);
            jsonObject.put("movie_id", movCode);
            jsonObject.put("star", stars);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.writeShortReview);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "short Review: res:" + response.getString("res"));

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

    private void shortReviewListRequest(String movCode){
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("movie_id", movCode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.shortReviewViewInMovie);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "short Review View with movie_id: res:" + response.getString("res"));
                    JSONArray dataJsonArray = response.getJSONArray("data");
                    Log.d(TAG, "onResponse: short review list:"+dataJsonArray);
                    if(response.getString("res").equals("200")) {
                        Resources res = getResources();
                        String[] mbtiList = res.getStringArray(R.array.mbti_array);

                        writingArray.clear(); movCodeArray.clear(); starArray.clear();
                        writingIdArray.clear(); userIdArray.clear(); nicknameArray.clear(); mbtiArray.clear();
                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject dataObj = dataJsonArray.getJSONObject(i);

                            writingIdArray.add(dataObj.getString("_id"));
                            movCodeArray.add(dataObj.getString("movie_id"));
                            starArray.add(dataObj.getDouble("star"));
                            writingArray.add(dataObj.getString("writing"));
                            userIdArray.add(dataObj.getString("user_id"));
                            nicknameArray.add(dataObj.getString("user_nickname"));
                            mbtiArray.add(mbtiList[dataObj.getInt("user_mbti")]);
                        }
                        shortReviewInDetailRecyclerViewAdapter.notifyDataSetChanged();
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
