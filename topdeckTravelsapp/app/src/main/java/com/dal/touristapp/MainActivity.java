package com.dal.touristapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton userProfileButton;
    SharedPreferences myPref;
    private TextView errorView;
    String apiUrl = "http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3004/places/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycleView);
        errorView = findViewById(R.id.errorView);
        userProfileButton=findViewById(R.id.userProfileButton);

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                if(myPref.getString("email",null)!=null){
                    Intent intent = new Intent(MainActivity.this,LogoutActivity.class);
                    startActivity(intent);
                }
               else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
               }
            }
        });

        final ArrayList<Card> cardsList = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Request req = new JsonArrayRequest(Request.Method.GET,apiUrl, null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response);
                for(int i = 0; i<response.length();i++){
                    try{
                        JSONObject obj = response.getJSONObject(i);
                        String placeText = obj.getString("name");
                        String imageUrl = obj.getString("image_url");
                        cardsList.add(new Card(imageUrl, placeText));
                    } catch (Exception e){
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Something Went wrong" , Toast.LENGTH_LONG).show();
                    }
                }
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getApplicationContext());
                adapter = new ViewAdaptor(cardsList);

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        recyclerView.setVisibility(View.GONE);
                        System.out.println(error);
                        Toast.makeText(getApplicationContext(),"Something Went wrong" , Toast.LENGTH_LONG).show();
                    }
                });
        RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }
}
