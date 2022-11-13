package com.example.lotus_kproject2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SearchResultActivity_main extends AppCompatActivity {
    private MypageFragment mypageFragment = new MypageFragment();
    private LongReviewFragment longReviewFragment = new LongReviewFragment();
    private ShortReviewFragment shortReviewFragment = new ShortReviewFragment();
    private MovieFragment movieFragment = new MovieFragment();
    private MovieRecommandFragment movieRecommandFragment = new MovieRecommandFragment();
    private MovieSearchResultFragment movieSearchResultFragment = new MovieSearchResultFragment();
    private PreferFragment preferFragment = new PreferFragment();
    private MyBlogFragment myBlogFragment = new MyBlogFragment();
    private HomeActivity homeActivity = new HomeActivity();

    private MaterialToolbar topAppbar;
    private AHBottomNavigation bottomNavigationInMovieResult;
    private FrameLayout frameLayoutInMovieResult;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String searchWord;

    private FragmentManager fragmentManager = getSupportFragmentManager();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movieresult_for_fragment);
        overridePendingTransition(R.anim.none, R.anim.none);

        topAppbar = findViewById(R.id.topAppBarInMovieResult);
        bottomNavigationInMovieResult = (AHBottomNavigation) findViewById(R.id.bottomNavigationInMovieResult);
        frameLayoutInMovieResult = findViewById(R.id.frameLayoutInMovieResult);
        drawerLayout = findViewById(R.id.drawerLayoutInMovieResult);
        navigationView = findViewById(R.id.navigationViewInMovieResult);

        setBottomNavigationInMovieResult();
        topAppbar.setTitle("검색");
        setDrawer();



        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayoutInMovieResult, movieSearchResultFragment).commitAllowingStateLoss();

        searchWord = getIntent().getStringExtra("searchWord");

        Bundle bundle = new Bundle();
        bundle.putString("searchWord", searchWord);
        fragmentManager.setFragmentResult("searchWord", bundle);

        topAppbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
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
                Intent intent;
                switch (position) {
                    case 0:
//                        topAppbar.setTitle("Fillow");
//                        transaction.replace(R.id.frameLayout, movieRecommandFragment).commitAllowingStateLoss();
                        intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case 1:
                        topAppbar.setTitle("글쓰기");
                        intent = new Intent(getApplicationContext(), ReviewEditorActivity.class);
                        intent.putExtra("existMovie", 0);
                        startActivity(intent);
                        break;
                    case 2:
                        topAppbar.setTitle("내 블로그");
                        transaction.replace(R.id.frameLayoutInMovieResult, myBlogFragment).commitAllowingStateLoss();
                        break;

                }
                return true;
            }
        });

    }
    private void setDrawer(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigationView.setCheckedItem(item);
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (item.getItemId()) {
                    case R.id.drawer_long:
                        topAppbar.setTitle("감상평 게시판");
                        transaction.replace(R.id.frameLayoutInMovieResult, longReviewFragment).commitAllowingStateLoss();
                        break;
                    case R.id.drawer_short:
                        topAppbar.setTitle("한 줄 느낌 게시판");
                        transaction.replace(R.id.frameLayoutInMovieResult, shortReviewFragment).commitAllowingStateLoss();
                        break;
                    case R.id.drawer_preferMovie:
                        topAppbar.setTitle("찜한 영화");
                        transaction.replace(R.id.frameLayoutInMovieResult, preferFragment).commitAllowingStateLoss();
                        break;
                    case R.id.drawer_logout:
                        logout();
                        break;

                }
                drawerLayout.close();
                return false;
            }
        });
    }
    private void logout(){
        KakaoSdk.init(getApplicationContext(), getString(R.string.nativeAppKey));
        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {
                return null;
            }
        });
        SharedPreferences pref = getSharedPreferences(getString(R.string.loginData), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("token");
        editor.remove("memNum");
        editor.commit();

        SharedPreferences pref2 = getSharedPreferences(getString(R.string.userData), MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pref2.edit();
        editor2.remove("mbti");
        editor2.remove("nickname");
        editor2.commit();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}
