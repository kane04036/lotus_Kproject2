package com.example.lotus_kproject2;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class FragmentMainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private SearchFragment searchFragment = new SearchFragment();
    private HomeFragment homeFragment = new HomeFragment();
    private MypageFragment mypageFragment = new MypageFragment();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentmain);

        FragmentTransaction transaction  = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout, homeFragment).commitAllowingStateLoss();

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_home, R.drawable.home, R.color.tab_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_search, R.drawable.search, R.color.tab_search);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_mypage, R.drawable.profile, R.color.tab_profile);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

// Enable the translation of the FloatingActionButton
//        bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);

// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Display color under navigation bar (API 21+)
// Don't forget these lines in your style-v21
// <item name="android:windowTranslucentNavigation">true</item>
// <item name="android:fitsSystemWindows">true</item>
        bottomNavigation.setTranslucentNavigationEnabled(true);

// Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
//        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
//        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

// Use colored navigation with circle reveal effect
//        bottomNavigation.setColored(true);

// Set current item programmatically
        bottomNavigation.setCurrentItem(0);

// Customize notification (title, background, typeface)
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

// Enable / disable item & set disable color
        bottomNavigation.enableItemAtPosition(0);
//        bottomNavigation.disableItemAtPosition(2);
        bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

// Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (position){
                    case 0:
                        transaction.replace(R.id.framelayout, homeFragment).commitAllowingStateLoss();
                        break;
                    case 1:
                        transaction.replace(R.id.framelayout, searchFragment).commitAllowingStateLoss();
                        break;
                    case 2:
                        transaction.replace(R.id.framelayout, mypageFragment).commitAllowingStateLoss();
                        break;

                }
                return true;
            }
        });
//        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
//            @Override public void onPositionChange(int y) {
//                // Manage the new y position
//            }
//        });
    }
}
