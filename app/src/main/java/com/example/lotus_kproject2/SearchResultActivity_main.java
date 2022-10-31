package com.example.lotus_kproject2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class SearchResultActivity_main extends AppCompatActivity {
    private MypageFragment mypageFragment = new MypageFragment();
    private LongReviewFragment longReviewFragment = new LongReviewFragment();
    private ShortReviewFragment shortReviewFragment = new ShortReviewFragment();
    private MovieFragment movieFragment = new MovieFragment();
    private MovieRecommandFragment movieRecommandFragment = new MovieRecommandFragment();
    private MovieSearchResultFragment movieSearchResultFragment = new MovieSearchResultFragment();

    MaterialToolbar topAppbarInMovieResult;
    AHBottomNavigation bottomNavigationInMovieResult;
    FrameLayout frameLayoutInMovieResult;
    DrawerLayout drawerLayoutInMovieResult;
    NavigationView navigationViewInMovieResult;
    String searchWord;

    private FragmentManager fragmentManager = getSupportFragmentManager();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movieresult_for_fragment);
        overridePendingTransition(R.anim.none, R.anim.none);

        topAppbarInMovieResult = findViewById(R.id.topAppBarInMovieResult);
        bottomNavigationInMovieResult = (AHBottomNavigation) findViewById(R.id.bottomNavigationInMovieResult);
        frameLayoutInMovieResult = findViewById(R.id.frameLayoutInMovieResult);
        drawerLayoutInMovieResult = findViewById(R.id.drawerLayoutInMovieResult);
        navigationViewInMovieResult = findViewById(R.id.navigationViewInMovieResult);

        setBottomNavigationInMovieResult();
        topAppbarInMovieResult.setTitle("검색");



        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayoutInMovieResult, movieSearchResultFragment).commitAllowingStateLoss();

        searchWord = getIntent().getStringExtra("searchWord");

        Bundle bundle = new Bundle();
        bundle.putString("searchWord", searchWord);
        fragmentManager.setFragmentResult("searchWord", bundle);

        topAppbarInMovieResult.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayoutInMovieResult.open();
            }
        });








    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.none, R.anim.none);

    }

    @SuppressLint("ResourceType")
    private void setBottomNavigationInMovieResult(){
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_home, R.drawable.home, R.color.tab_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.tab_write), R.drawable.plus);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_profile, R.drawable.profile, R.color.tab_profile);

        bottomNavigationInMovieResult.addItem(item1);
        bottomNavigationInMovieResult.addItem(item2);
        bottomNavigationInMovieResult.addItem(item3);

        bottomNavigationInMovieResult.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        bottomNavigationInMovieResult.setBehaviorTranslationEnabled(true);
        bottomNavigationInMovieResult.setAccentColor(Color.parseColor(getString(R.color.signature_color)));
        bottomNavigationInMovieResult.setInactiveColor(Color.parseColor("#747474"));
        bottomNavigationInMovieResult.setForceTint(true);
        bottomNavigationInMovieResult.setTranslucentNavigationEnabled(true);
        bottomNavigationInMovieResult.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigationInMovieResult.setCurrentItem(0);
        bottomNavigationInMovieResult.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));
        bottomNavigationInMovieResult.enableItemAtPosition(0);
        bottomNavigationInMovieResult.setItemDisableColor(Color.parseColor("#3A000000"));

        bottomNavigationInMovieResult.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (position) {
                    case 0:
                        topAppbarInMovieResult.setTitle("Fillow");
                        transaction.replace(R.id.frameLayoutInMovieResult, movieRecommandFragment).commitAllowingStateLoss();
                        break;
                    case 1:
                        topAppbarInMovieResult.setTitle("글쓰기");
//                        transaction.replace(R.id.frameLayout, searcchActivity).commitAllowingStateLoss();
                        Intent intent = new Intent(getApplicationContext(), ReviewEditorActivity.class);
                        intent.putExtra("existMovie", 0);
                        startActivity(intent);
                        break;
                    case 2:
                        topAppbarInMovieResult.setTitle("알림");
                        transaction.replace(R.id.frameLayoutInMovieResult, mypageFragment).commitAllowingStateLoss();
                        break;

                }
                return true;
            }
        });


    }
    private void setDrawer(){
        navigationViewInMovieResult.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigationViewInMovieResult.setCheckedItem(item);
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (item.getItemId()) {
                    case R.id.drawer_long:
                        topAppbarInMovieResult.setTitle("긴 글 리뷰 게시판");
                        transaction.replace(R.id.frameLayoutInMovieResult, longReviewFragment).commitAllowingStateLoss();
                        break;
                    case R.id.drawer_short:
                        topAppbarInMovieResult.setTitle("짧은 글 리뷰 게시판");
                        transaction.replace(R.id.frameLayoutInMovieResult, shortReviewFragment).commitAllowingStateLoss();
                        break;

                }
                drawerLayoutInMovieResult.close();
                return false;
            }
        });


    }
}
