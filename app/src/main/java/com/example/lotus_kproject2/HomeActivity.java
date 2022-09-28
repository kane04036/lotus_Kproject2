package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MypageFragment mypageFragment = new MypageFragment();
    private LongReviewFragment longReviewFragment = new LongReviewFragment();
    private ShortReviewFragment shortReviewFragment = new ShortReviewFragment();
    private MovieFragment movieFragment = new MovieFragment();
    private MovieRecommandFragment movieRecommandFragment = new MovieRecommandFragment();

    DrawerLayout drawrLayout;
    MaterialToolbar topAppbar;
    NavigationView navigationView;
    EditText edtSearch;
    Boolean femaleCheck = false, maleCheck = false;
    ArrayList registerResult = new ArrayList();
    AlertDialog alertDialog, genderAlertDialog, ageAlertDialog, mbtiAlertDialog, nicknameAlertDialog;
    AHBottomNavigation bottomNavigation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, movieRecommandFragment).commitAllowingStateLoss();

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        drawrLayout = findViewById(R.id.drawerLayout);
        topAppbar = findViewById(R.id.topAppBar);
        navigationView = findViewById(R.id.navigationView);
        edtSearch = findViewById(R.id.edtSearch);

        setBottomNavigation();

        if (getIntent().getStringExtra("isNew").equals("1")) {
            showGenderAlertDialog();
        }

        topAppbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawrLayout.open();
            }
        });

        topAppbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
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

                switch (item.getItemId()) {
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

    }



    void showGenderAlertDialog(){
        View dialogView = getLayoutInflater().inflate(R.layout.alertdialog_gender, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setView(dialogView);
        alertDialog = builder.create();
        builder.setCancelable(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

        Button btnNextInGender = dialogView.findViewById(R.id.btnNextInGender);
        ImageView imageFemale = dialogView.findViewById(R.id.imgBtnFemale);
        ImageView imageMale = dialogView.findViewById(R.id.imgBtnMale);

        imageFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                femaleCheck = true;
                imageFemale.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
                if(maleCheck){
                    imageMale.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.DST);
                    maleCheck = false;
                }
            }
        });
        imageMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleCheck = true;
                imageMale.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
                if(femaleCheck){
                    imageFemale.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.DST);
                    femaleCheck = false;
                }
            }
        });

        btnNextInGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(femaleCheck || maleCheck){
                    if(femaleCheck){
                        registerResult.add(1);
                    }else{
                        registerResult.add(0);
                    }
                    alertDialog.dismiss();
                    showAgeAlertDialog();
                }

            }
        });
    }
    void showAgeAlertDialog(){
        View dialogView = getLayoutInflater().inflate(R.layout.alertdialog_age, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setView(dialogView);

        final Spinner spinnerAge = dialogView.findViewById(R.id.spinnerAge);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.age_range , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        spinnerAge.setAdapter(adapter);

        alertDialog = builder.create();
        builder.setCancelable(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

        Button btnNextInAge = dialogView.findViewById(R.id.btnNextInAge);
        btnNextInAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerResult.add(spinnerAge.getSelectedItemPosition());
                alertDialog.dismiss();
                showMBTIAlertDialog();
            }
        });
    }
    void showMBTIAlertDialog(){
        View dialogView = getLayoutInflater().inflate(R.layout.alertdialog_mbti, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setView(dialogView);

        final Spinner spinnerMbti = dialogView.findViewById(R.id.spinnerMbti);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(HomeActivity.this,
                R.array.mbti_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        spinnerMbti.setAdapter(adapter);

        alertDialog = builder.create();
        builder.setCancelable(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

        Button btnNextInMBTI = dialogView.findViewById(R.id.btnNextInMbti);
        btnNextInMBTI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerResult.add(spinnerMbti.getSelectedItemPosition());
                alertDialog.dismiss();
                showNicknameAlertDialog();
            }
        });
    }
    void showNicknameAlertDialog(){
        View dialogView = getLayoutInflater().inflate(R.layout.alertdialog_nickname, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        alertDialog = builder.create();
        builder.setCancelable(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

        Button btnNextInNickname = dialogView.findViewById(R.id.btnNextInNickname);
        EditText edtNickname = dialogView.findViewById(R.id.edtNickname);
        btnNextInNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtNickname.getText().toString().replace(" ","").equals("")){
                    registerResult.add(edtNickname.getText().toString());
                    for(int i = 0; i < registerResult.size(); i++){
                        Log.d(TAG, "onClick: registerResultArray:"+registerResult.get(i));
                    }
                    validNickCheck(edtNickname.getText().toString());
                }
            }
        });
    }

    public void validNickCheck(String nickname){

        RequestQueue Queue = Volley.newRequestQueue(HomeActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nickname", nickname);
            Log.d(TAG, "validNickCheck: nickname:" + nickname);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.url)+"/umanager/ck_nickname/kakao";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String res = response.getString("res");
                    Log.d(TAG, "onResponse: Nickname Valid Result:"+res);
                    if(res.equals("Success")){
                        //여기서 회원가입 처리를 어떻게 할까...
                        kakaoRegister();
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
    public void kakaoRegister(){
        RequestQueue Queue = Volley.newRequestQueue(HomeActivity.this);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.loginData),Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");
        String memNum = sharedPreferences.getString("memNum","");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("num", memNum);
            jsonObject.put("gender", registerResult.get(0));
            jsonObject.put("age", registerResult.get(1));
            jsonObject.put("mbti", registerResult.get(2));
            jsonObject.put("nickname", registerResult.get(3));

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.url)+"/umanager/register/kakao";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String res = response.getString("res");
                    Log.d(TAG, "onResponse: kakaoRegister Result:"+res);
                    if(res.equals("200")){
                        //여기서 회원가입 처리를 어떻게 할까...
                        alertDialog.dismiss();
                    }
                    else{
                        alertDialog.dismiss();
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
    @SuppressLint("ResourceType")
    private void setBottomNavigation(){

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_home, R.drawable.home, R.color.tab_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.tab_write), R.drawable.plus);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_profile, R.drawable.profile, R.color.tab_profile);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setAccentColor(Color.parseColor(getString(R.color.signature_color)));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        bottomNavigation.setForceTint(true);
//        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));
        bottomNavigation.enableItemAtPosition(0);
        bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (position) {
                    case 0:
                        topAppbar.setTitle("Fillow");
                        transaction.replace(R.id.frameLayout, movieRecommandFragment).commitAllowingStateLoss();
                        break;
                    case 1:
                        topAppbar.setTitle("글쓰기");
//                        transaction.replace(R.id.frameLayout, searcchActivity).commitAllowingStateLoss();
                        Intent intent = new Intent(getApplicationContext(), ReviewEditorActivity.class);
                        intent.putExtra("existMovie", 0);
                        startActivity(intent);
                        break;
                    case 2:
                        topAppbar.setTitle("마이페이지");
                        transaction.replace(R.id.frameLayout, mypageFragment).commitAllowingStateLoss();
                        break;

                }
                return true;
            }
        });

    }
}