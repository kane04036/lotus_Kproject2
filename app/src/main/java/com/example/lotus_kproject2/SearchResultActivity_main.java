package com.example.lotus_kproject2;

import android.content.Intent;
import android.content.pm.ApkChecksum;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

public class SearchResultActivity_main extends AppCompatActivity {
    ArrayList<Integer> imageArrayList = new ArrayList<>();
    ArrayList<String> nameArrayList = new ArrayList<>();
    RecyclerView movieRecyclerView;
    MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    AHBottomNavigation bottomNavigationBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        overridePendingTransition(R.anim.none, R.anim.none);

        movieRecyclerView = findViewById(R.id.movieRecyclerView);
//        bottomNavigationBar = findViewById(R.id.bottom_navigationBar);

        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter();


        imageArrayList.add(R.drawable.movie_poster1);
        imageArrayList.add(R.drawable.movie_poster2);
        imageArrayList.add(R.drawable.movie_poster3);
        imageArrayList.add(R.drawable.movie_poster4);
        nameArrayList.add("콜 미 바이 유어 네임");
        nameArrayList.add("공조 : 인터내셔널");
        nameArrayList.add("더 헌터");
        nameArrayList.add("살인의 추억");


        movieRecyclerViewAdapter.setImage(imageArrayList);
        movieRecyclerViewAdapter.setMovieName(nameArrayList);

        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        movieRecyclerView.setAdapter(movieRecyclerViewAdapter);


//        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_home, R.drawable.home, R.color.tab_home);
//        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_write, R.drawable.plus, R.color.tab_search);
//        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_notify, R.drawable.notification, R.color.tab_profile);
//
//        bottomNavigationBar.addItem(item1);
//        bottomNavigationBar.addItem(item2);
//        bottomNavigationBar.addItem(item3);
//
//        bottomNavigationBar.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
//
//        bottomNavigationBar.setBehaviorTranslationEnabled(false);
//
//
//
//        bottomNavigationBar.setAccentColor(Color.parseColor("#3688a3"));
//        bottomNavigationBar.setInactiveColor(Color.parseColor("#747474"));
//
//        bottomNavigationBar.setForceTint(true);
//
//        bottomNavigationBar.setTranslucentNavigationEnabled(true);
//
//// Manage titles
//        bottomNavigationBar.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
//
//
//// Use colored navigation with circle reveal effect
////        bottomNavigation.setColored(true);
//
//        bottomNavigationBar.setCurrentItem(0);
//
//        bottomNavigationBar.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));
//
//// Add or remove notification for each item
////        bottomNavigation.setNotification("1", 3);
//// OR
////        AHNotification notification = new AHNotification.Builder()
////                .setText("1")
////                .setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.white))
////                .setTextColor(ContextCompat.getColor(HomeActivity.this, R.color.white))
////                .build();
////        bottomNavigation.setNotification(notification, 1);
//
//        bottomNavigationBar.enableItemAtPosition(0);
//        bottomNavigationBar.setItemDisableColor(Color.parseColor("#3A000000"));
//        bottomNavigationBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
//            @Override
//            public boolean onTabSelected(int position, boolean wasSelected) {
//                // Do something cool here...
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                switch (position){
//                    case 0:
//                        topAppbar.setTitle("Fillow");
//                        transaction.replace(R.id.frameLayout, movieRecommandFragment).commitAllowingStateLoss();
//                        break;
//                    case 1:
//                        topAppbar.setTitle("글쓰기");
////                        transaction.replace(R.id.frameLayout, searcchActivity).commitAllowingStateLoss();
//                        Intent intent = new Intent(getApplicationContext(),MovieDetail2.class );
//                        startActivity(intent);
//                        break;
//                    case 2:
//                        topAppbar.setTitle("알림");
//                        transaction.replace(R.id.frameLayout, mypageFragment).commitAllowingStateLoss();
//                        break;
//
//                }
//                return true;
//            }
//        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.none, R.anim.none);

    }
}
