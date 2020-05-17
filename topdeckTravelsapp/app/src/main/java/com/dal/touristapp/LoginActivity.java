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

public class LoginActivity extends AppCompatActivity {
    private final static String emailPattern = "[a-zA-Z0-9._-]+@dal.ca";
    private Button loginButton;
    private EditText emailInput,passwordInput;
    private TextView signUp;
    private String apiUrl = "http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3001/users/authenticate";
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.usrusr);
        passwordInput = findViewById(R.id.pswrdd);
        loginButton = findViewById(R.id.lin);
        signUp = findViewById(R.id.sign_up_direct);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, this.MODE_PRIVATE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(emailInput.getText())){
                    emailInput.setError("Email is required");
                    emailInput.requestFocus();
                }
                else if(TextUtils.isEmpty(passwordInput.getText())){
                    passwordInput.setError("Password is required");
                    passwordInput.requestFocus();
                }
                else if(!emailInput.getText().toString().matches(emailPattern)){
                    emailInput.setError("Please enter a valid email");
                    emailInput.requestFocus();
                }
                else{
                    final String email = emailInput.getText().toString();
                    String password = passwordInput.getText().toString();
                    JSONObject params = new JSONObject();
                    try {
                        params.put("email", email);
                        params.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Request request = new JsonObjectRequest(Request.Method.POST, apiUrl,params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response);
                            try {
                                String securityQuestion = response.getString("securityQuestion");
                                System.out.println(response);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("name", response.getString("name"));
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, SecurityActivity.class);
                                intent.putExtra("securityQuestion",securityQuestion);
                                intent.putExtra("email", email);
                                startActivity(intent);

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"Incorrect Email or password" , Toast.LENGTH_LONG).show();
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

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
