package com.dal.touristapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private final static String emailPattern = "[a-zA-Z0-9._-]+@dal.ca";
    private EditText name, email,password, securityAnswer;
    private Spinner securityQuestion;
    private Button register;
    String selectedQuestion = null;
    String apiUrl = "http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3001/users/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        securityAnswer = findViewById(R.id.security);
        securityQuestion = findViewById(R.id.securityQuestion);
        register = findViewById(R.id.register);

        List<String> questionList = new ArrayList<>();
        questionList.add("What is your mother's maiden name?");
        questionList.add("What is the name of your childhood best friend?");
        questionList.add("What is your favourite author's name?");
        questionList.add("What primary school did you attend??");
        questionList.add("In what city or town was your first job?");
        questionList.add("What was your favorite place to visit as a child");
        questionList.add("What is your grandfather's name?");
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,questionList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        securityQuestion.setAdapter(cityAdapter);
        securityQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedQuestion = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText())){
                    name.setError("Name is required");
                    name.requestFocus();
                }
                else if(TextUtils.isEmpty(email.getText())){
                    email.setError("Email is required");
                    email.requestFocus();
                }
                else if(email.getText().toString().compareTo("test@dal.ca")==0){
                    email.setError("Already registered with this email");
                    email.requestFocus();
                }
                else if(TextUtils.isEmpty(password.getText())){
                    password.setError("Password is required");
                    password.requestFocus();
                }
                else if(TextUtils.isEmpty(securityAnswer.getText())){
                    securityAnswer.setError("Name is required");
                    securityAnswer.requestFocus();
                }
                else if(!email.getText().toString().matches(emailPattern)){
                    email.setError("Please Enter a valid Email address");
                    email.requestFocus();
                }
                else if(password.getText().toString().length()<6){
                    password.setError("Password should be minimum 6 characters");
                    password.requestFocus();
                }
                else {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("name", name.getText().toString());
                        params.put("email", email.getText().toString());
                        params.put("password", password.getText().toString());
                        params.put("answer", securityAnswer.getText().toString());
                        params.put("securityQuestion", selectedQuestion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Request request = new JsonObjectRequest(Request.Method.POST, apiUrl,params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println(response);
                                if(response.getString("message").compareTo("User registered successfully")==0){
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                                if(response.getString("message").contains("is already taken")){
                                    email.setError("Already registered with this email");
                                    email.requestFocus();
                                }

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            System.out.println(error);
                        }
                    });

                    RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                }

            }
        });

    }
}
