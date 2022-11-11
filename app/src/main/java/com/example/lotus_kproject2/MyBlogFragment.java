package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyBlogFragment extends Fragment {
    FragmentManager fragmentManager;
    LongReviewInMyBlogFragment longReviewInMyBlogFragment = new LongReviewInMyBlogFragment();
    ShortReviewInMyBlogFragment shortReviewInMyBlogFragment = new ShortReviewInMyBlogFragment();

    private TextView tvTabBarLongReviewInMyBlog, tvTabBarShortReviewInMyBlog, tvNickname, tvMbti, tvFollowingNum, tvFollowerNum;
    FrameLayout frameLayoutInMyBlog;

    Bundle result = new Bundle();

    String follower, following;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myblog, container, false);

        fragmentManager = getChildFragmentManager();

        SharedPreferences sharedPreferences_loginData = getActivity().getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
        result.putString("userId", sharedPreferences_loginData.getString("memNum", ""));
        Log.d(TAG, "user id:" + sharedPreferences_loginData.getString("memNum", ""));
        fragmentManager.setFragmentResult("userData_long", result);
        fragmentManager.setFragmentResult("userData_short", result);

        requestMyBlogData(sharedPreferences_loginData.getString("memNum", ""));

        tvTabBarLongReviewInMyBlog = view.findViewById(R.id.tvTabBarLongReviewInMyBlog);
        tvTabBarShortReviewInMyBlog = view.findViewById(R.id.tvTabBarShortReviewInMyBlog);
        frameLayoutInMyBlog = view.findViewById(R.id.frameLayoutInMyBlog);
        tvNickname = view.findViewById(R.id.tvNickNameInMyBlog);
        tvMbti = view.findViewById(R.id.tvMbtiInMyBlog);
        tvFollowerNum = view.findViewById(R.id.tvFollowerNumberInMyBlog);
        tvFollowingNum = view.findViewById(R.id.tvFollowingNumberInMyBlog);

        SharedPreferences sharedPreferences_userData = getActivity().getSharedPreferences(getString(R.string.userData), Context.MODE_PRIVATE);
        tvNickname.setText(sharedPreferences_userData.getString("nickname",""));
        tvMbti.setText(sharedPreferences_userData.getString("mbti",""));

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayoutInMyBlog, longReviewInMyBlogFragment).commitAllowingStateLoss();

        tvTabBarLongReviewInMyBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabBarLongReviewInMyBlog.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.line_rectangle));
                tvTabBarLongReviewInMyBlog.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                tvTabBarShortReviewInMyBlog.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.filled_rectangle));
                tvTabBarShortReviewInMyBlog.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayoutInMyBlog, longReviewInMyBlogFragment).commitAllowingStateLoss();
            }
        });

        tvTabBarShortReviewInMyBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabBarShortReviewInMyBlog.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.line_rectangle));
                tvTabBarShortReviewInMyBlog.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                tvTabBarLongReviewInMyBlog.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.filled_rectangle));
                tvTabBarLongReviewInMyBlog.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayoutInMyBlog, shortReviewInMyBlogFragment).commitAllowingStateLoss();
            }
        });

        return view;
    }

    private void requestMyBlogData(String memNum){
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.loginData),Context.MODE_PRIVATE);
            jsonObject.put("userNm", memNum);
            jsonObject.put("token", sharedPreferences.getString("token",""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.mypage);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: my page: res:"+response.getString("res"));
                    if(response.getString("res").equals("200")){
                        JSONArray dataArray = response.getJSONArray("data");
                        tvFollowerNum.setText(dataArray.getString(3));
                        tvFollowingNum.setText(dataArray.getString(4));
                        Log.d(TAG, "onResponse: following:"+dataArray.getString(4)+"follower:"+dataArray.getString(3));
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
