package com.dal.touristapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SecurityActivity extends AppCompatActivity {
    private TextView question;
    private EditText answer;
    private Button validate;
    private String apiUrl = "http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3001/users/validateQuestion";
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        question = findViewById(R.id.securityValidate);
        answer = findViewById(R.id.securityAnswer);
        validate = findViewById(R.id.validateQuestion);

        final String securityQuestion = getIntent().getExtras().getString("securityQuestion");
        question.setText(securityQuestion);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, this.MODE_PRIVATE);

        final String email = getIntent().getExtras().getString("email");
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(answer.getText())){
                    answer.setError("Answer is required");
                    answer.requestFocus();
                }
                else{
                    JSONObject params = new JSONObject();
                    try {
                        params.put("email", email);
                        params.put("securityQuestion", securityQuestion);
                        params.put("answer", answer.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Request request = new JsonObjectRequest(Request.Method.POST, apiUrl,params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println(securityQuestion);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.commit();
                                Intent intent = new Intent(SecurityActivity.this, MainActivity.class);
                                startActivity(intent);


                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"Incorrect Answer.Please Try Again", Toast.LENGTH_LONG).show();
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
                }

            }
        });

    }
}
