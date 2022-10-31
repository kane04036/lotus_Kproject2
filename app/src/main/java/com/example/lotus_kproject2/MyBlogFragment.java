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

public class MyBlogFragment extends Fragment {
    FragmentManager fragmentManager;
    LongReviewInMyBlogFragment longReviewInMyBlogFragment = new LongReviewInMyBlogFragment();
    ShortReviewInMyBlogFragment shortReviewInMyBlogFragment = new ShortReviewInMyBlogFragment();

    private TextView tvTabBarLongReviewInMyBlog, tvTabBarShortReviewInMyBlog, tvNickname, tvMbti, tvFollowingNum, tvFollowerNum;
    FrameLayout frameLayoutInMyBlog;

    Bundle result = new Bundle();

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
                tvTabBarShortReviewInMyBlog.setTextColor(ContextCompat.getColor(getActivity(), R.color.darkGray));

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
                tvTabBarLongReviewInMyBlog.setTextColor(ContextCompat.getColor(getActivity(), R.color.darkGray));

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayoutInMyBlog, shortReviewInMyBlogFragment).commitAllowingStateLoss();
            }
        });

        return view;
    }

}
