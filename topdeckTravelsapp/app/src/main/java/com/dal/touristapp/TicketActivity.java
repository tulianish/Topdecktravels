package com.dal.touristapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TicketActivity extends AppCompatActivity {

    private TextView from,to,at,on;
    String apiurl = "http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3005/ticket/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        from = findViewById(R.id.from);
        to = findViewById(R.id.toPlaceName);
        on = findViewById(R.id.traveldate);
        at = findViewById(R.id.traveltime);

        String httpurl1 = apiurl+ getIntent().getExtras().getString("id");
        Request request = new JsonObjectRequest(Request.Method.GET, httpurl1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        System.out.println(getIntent().getExtras().getString("id"));
        from.setText(getIntent().getExtras().getString("from_place"));
        to.setText(getIntent().getExtras().getString("to_place"));
        on.setText(getIntent().getExtras().getString("travel_date"));
        at.setText(getIntent().getExtras().getString("travel_time"));
        Toast.makeText(getApplicationContext(), "Ticket sent to : " +getIntent().getExtras().getString("email"),Toast.LENGTH_SHORT).show();

    }
}
