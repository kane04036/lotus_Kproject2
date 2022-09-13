package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private MypageFragment mypageFragment = new MypageFragment();
    private LongReviewFragment longReviewFragment = new LongReviewFragment();
    private ShortReviewFragment shortReviewFragment = new ShortReviewFragment();
    private MovieFragment movieFragment = new MovieFragment();
    private MovieRecommandFragment movieRecommandFragment = new MovieRecommandFragment();
    private SearchActivity searchActivity = new SearchActivity();

    DrawerLayout drawrLayout;
    MaterialToolbar topAppbar;
    NavigationView navigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentTransaction transaction  = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, movieRecommandFragment).commitAllowingStateLoss();

        drawrLayout = findViewById(R.id.drawerLayout);
        topAppbar = findViewById(R.id.topAppBar);
        navigationView = findViewById(R.id.navigationView);

        topAppbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigation");
                drawrLayout.open();
            }
        });

        topAppbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_search:
                        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigationView.setCheckedItem(item);
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (item.getItemId()){
                    case R.id.drawer_mypage:
                        topAppbar.setTitle("마이페이지");
                        transaction.replace(R.id.frameLayout, mypageFragment).commitAllowingStateLoss();
                        break;
                    case R.id.drawer_long:
                        topAppbar.setTitle("긴 글 리뷰 게시판");
                        transaction.replace(R.id.frameLayout, longReviewFragment).commitAllowingStateLoss();
                        break;
                    case R.id.drawer_short:
                        topAppbar.setTitle("짧은 글 리뷰 게시판");
                        transaction.replace(R.id.frameLayout, shortReviewFragment).commitAllowingStateLoss();
                        break;
                    case R.id.drawer_movie:
                        topAppbar.setTitle("영화");
                        transaction.replace(R.id.frameLayout, movieFragment).commitAllowingStateLoss();
                        break;
                }
                drawrLayout.close();
                return false;
            }
        });


        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_home, R.drawable.home, R.color.tab_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_write, R.drawable.plus, R.color.tab_search);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_notify, R.drawable.notification, R.color.tab_profile);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        bottomNavigation.setBehaviorTranslationEnabled(false);



        bottomNavigation.setAccentColor(Color.parseColor("#3688a3"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        bottomNavigation.setForceTint(true);

        bottomNavigation.setTranslucentNavigationEnabled(true);

// Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);


// Use colored navigation with circle reveal effect
//        bottomNavigation.setColored(true);

        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

// Add or remove notification for each item
//        bottomNavigation.setNotification("1", 3);
// OR
//        AHNotification notification = new AHNotification.Builder()
//                .setText("1")
//                .setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.white))
//                .setTextColor(ContextCompat.getColor(HomeActivity.this, R.color.white))
//                .build();
//        bottomNavigation.setNotification(notification, 1);

        bottomNavigation.enableItemAtPosition(0);
        bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (position){
                    case 0:
                        topAppbar.setTitle("Fillow");
                        transaction.replace(R.id.frameLayout, movieRecommandFragment).commitAllowingStateLoss();
                        break;
                    case 1:
                        topAppbar.setTitle("글쓰기");
//                        transaction.replace(R.id.frameLayout, searcchActivity).commitAllowingStateLoss();
                        Intent intent = new Intent(getApplicationContext(),MovieDetail2.class );
                        startActivity(intent);
                        break;
                    case 2:
                        topAppbar.setTitle("알림");
                        transaction.replace(R.id.frameLayout, mypageFragment).commitAllowingStateLoss();
                        break;

                }
                return true;
            }
        });




    }
}
