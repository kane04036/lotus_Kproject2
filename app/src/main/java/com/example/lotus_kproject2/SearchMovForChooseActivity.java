package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchMovForChooseActivity extends AppCompatActivity {
    RecyclerView recyclerViewInMovSearch;
    RelativeLayout layoutSearchMov;
    EditText edtSearchMov;
    MovieListRecyclerViewAdapter recyclerViewAdapter;
    MaterialToolbar reviewAppBar;

    ArrayList<String> imgArray = new ArrayList<>();
    ArrayList<String> nameArray = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mov);

        recyclerViewInMovSearch = findViewById(R.id.recyclerViewInMovSearch);
        layoutSearchMov = findViewById(R.id.layoutSearchMov);
        edtSearchMov = findViewById(R.id.edtSearchMov);
        reviewAppBar = findViewById(R.id.topAppBarInEditor);

        recyclerViewAdapter = new MovieListRecyclerViewAdapter(getApplicationContext());
        recyclerViewInMovSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerViewInMovSearch.setAdapter(recyclerViewAdapter);

        imgArray.add("https://www.themoviedb.org/t/p/w220_and_h330_face/aFVjf2zcFzoNgbDKDEHiIw2g1DR.jpg");
        imgArray.add("https://www.themoviedb.org/t/p/w220_and_h330_face/5F5MYJgVrCBLMNHuM1PVC4OYFoe.jpg");
        imgArray.add("https://www.themoviedb.org/t/p/w220_and_h330_face/bZLrpWM065h5bu1msUcPmLFsHBe.jpg");
        imgArray.add("https://www.themoviedb.org/t/p/w220_and_h330_face/lAP4sWFCch4Ed3ylOdhprCge5Li.jpg");
        imgArray.add("https://www.themoviedb.org/t/p/w220_and_h330_face/5F5MYJgVrCBLMNHuM1PVC4OYFoe.jpg");

        nameArray.add("그루트의 첫 발자국");
        nameArray.add("강철의 연금술사 완결편: 복수자 스카");
        nameArray.add("토르: 러브 앤 썬더");
        nameArray.add("노 리미트");
        nameArray.add("사마리탄");

        edtSearchMov.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == keyEvent.ACTION_DOWN) && (keyCode == keyEvent.KEYCODE_ENTER)) {
                    String searchWord = edtSearchMov.getText().toString();
                    recyclerViewAdapter.setArray(imgArray, nameArray);
                    recyclerViewAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

//        reviewAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.menu_upload:
//                        break;
//
//                }
//                return false;
//            }
//        });

    }


    public void movieSearch(String searchWord){
        RequestQueue Queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", searchWord);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.url) + getString(R.string.thumbnail);
        Log.d(TAG, "movieSearch: url: " + URL);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<String> imageArray = new ArrayList<>();
                    ArrayList<String> nameArray = new ArrayList<>();
                    Log.d(TAG, "onResponse: res:"+response.getString("res"));
                    Log.d(TAG, "onResponse: data:"+response.getString("data"));
                    JSONArray dataJsonArray = response.getJSONArray("data");
                    JSONArray nameJSonArray = dataJsonArray.getJSONArray(0);
                    JSONArray imageJsonArray = dataJsonArray.getJSONArray(1);
                    for(int i = 0; i< nameJSonArray.length(); i++){
//                        String.valueOf(nameJSonArray.get(i)).replace("<b>"," ");
//                        String.valueOf(nameJSonArray.get(i)).replace("</b>"," ");
                        nameArray.add(String.valueOf(nameJSonArray.get(i)));
                        imageArray.add(String.valueOf(imageJsonArray.get(i)));
                    }
                    recyclerViewAdapter.setArray(imageArray, nameArray);


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
