package com.dal.touristapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LogoutActivity extends AppCompatActivity {

    private TextView user;
    private Button logout, delete;
    SharedPreferences myPref;
    String apiUrl = "http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3001/users/logout";
    String deleteUrl = "http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3001/users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);


        user = findViewById(R.id.userName);
        logout = findViewById(R.id.lout);
        delete = findViewById(R.id.delete);
        myPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        JSONObject params = new JSONObject();
        try {
            params.put("email", myPref.getString("email",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(Request.Method.POST, "http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3001/users/current", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(response.optString("id")==null){
                        SharedPreferences.Editor editor = myPref.edit();
                        editor.clear();
                        editor.commit();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Something Went wrong" , Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        user.setText("Hello " + myPref.getString("name",null));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject params = new JSONObject();
                try {
                    params.put("email", myPref.getString("email",null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences.Editor editor = myPref.edit();
                editor.clear();
                editor.commit();
                Request request = new JsonObjectRequest(Request.Method.POST, apiUrl,params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response);
                            if(response.getString("message")!=null) {
                                Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                                startActivity(intent);
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
                RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = myPref.edit();
                editor.clear();
                editor.commit();
                Request request1 = new JsonObjectRequest(Request.Method.DELETE, deleteUrl + myPref.getString("email", null), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response);
                            Toast.makeText(getApplicationContext(), "Account Deleted successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LogoutActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"Something Went wrong" , Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(request1);
            }
        });


    }
}
