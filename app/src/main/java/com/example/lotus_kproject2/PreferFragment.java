package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PreferFragment extends Fragment {
    private ArrayList<MovieDataList> dataLists = new ArrayList<>();
    private PreferListRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prefer, container, false);

        adapter = new PreferListRecyclerViewAdapter(getActivity(),dataLists);
        recyclerView = view.findViewById(R.id.recyViewPrefer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        preferListRequest();

        return view;
    }

    private void preferListRequest(){
        RequestQueue Queue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            Log.d(TAG, "preferListRequest: token:"+sharedPreferences.getString("token",""));
            jsonObject.put("token",sharedPreferences.getString("token","") );
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.viewPrefer);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: prefer request res:"+response.getString("res"));
                    JSONArray dataJsonArray = response.getJSONArray("data");
                    Log.d(TAG, "onResponse: prefer request data:"+dataJsonArray);
                    dataLists.clear();
                    if(response.getString("res").equals("200")){
                        for(int i = 0; i<dataJsonArray.length(); i++){
                            JSONArray tmpJsonArray = dataJsonArray.getJSONArray(i);
                            dataLists.add(new MovieDataList(tmpJsonArray.getString(0), tmpJsonArray.getString(1),tmpJsonArray.getString(2), tmpJsonArray.getString(3)));
                        }
                        adapter.notifyDataSetChanged();
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
