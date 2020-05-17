package com.dal.touristapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class PlaceActivity extends AppCompatActivity {
    private ImageView image;
    private TextView placeName, placeAddress, openStatus;
    private Button bookBtn;
    String apiUrl = "http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3004/places/byName/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        image = findViewById(R.id.cardPlaceImg);
        placeName = findViewById(R.id.placeName);
        placeAddress = findViewById(R.id.placeAddress);
        openStatus = findViewById(R.id.openStatus);
        bookBtn = findViewById(R.id.bookBtn);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String name = getIntent().getStringExtra("placeName");
        String httpurl1 = apiUrl+ name;
        String httpurl = httpurl1.replaceAll(" ","%20");

        Request request = new JsonArrayRequest(Request.Method.GET, httpurl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    System.out.println(response);
                    boolean statusOfPlace = false;
                    String addressOfPlace = response.getJSONObject(0).getString("formatted_address");
                    try {
                        statusOfPlace = response.getJSONObject(0).getBoolean("open_status");
                    }
                    catch (JSONException e){
                        openStatus.setText("NA");
                    }
                    Glide.with(getApplicationContext()).load(response.getJSONObject(0).getString("image_url")).into(image);
                    placeName.setText(getIntent().getStringExtra("placeName"));
                    placeAddress.setText(addressOfPlace);
                    bookBtn.setText("Book Tickets ");
                    if(statusOfPlace) {
                        openStatus.setText("Open Now");
                    }
                    else {
                        openStatus.setText("Closed Now");
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Something Went wrong" , Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),BookingActivity.class);
                intent.putExtra("Travel_To", placeName.getText());
                v.getContext().startActivity(intent);
            }
        });

        RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}

