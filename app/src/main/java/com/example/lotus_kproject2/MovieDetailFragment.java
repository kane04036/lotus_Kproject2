package com.example.lotus_kproject2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class MovieDetailFragment extends Fragment {
    TextView tvTabBarLongReview, tvTabBarShortReview;
    private MaterialRatingBar ratingBar, ratingBarInShortReview;
    EditText edtShortReview;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        tvTabBarLongReview = view.findViewById(R.id.tvTabBarLongReview);
        tvTabBarShortReview = view.findViewById(R.id.tvTabBarShortReview);
        ratingBar = view.findViewById(R.id.ratingBarInDetail);

        tvTabBarShortReview.setIncludeFontPadding(false);
        tvTabBarLongReview.setIncludeFontPadding(false);

        tvTabBarLongReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabBarLongReview.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.line_rectangle));
                tvTabBarLongReview.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                tvTabBarShortReview.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.filled_rectangle));
                tvTabBarShortReview.setTextColor(ContextCompat.getColor(getActivity(), R.color.darkGray));
            }
        });

        tvTabBarShortReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabBarShortReview.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.line_rectangle));
                tvTabBarShortReview.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                tvTabBarLongReview.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.filled_rectangle));
                tvTabBarLongReview.setTextColor(ContextCompat.getColor(getActivity(), R.color.darkGray));

            }
        });
        return view;
    }
}
