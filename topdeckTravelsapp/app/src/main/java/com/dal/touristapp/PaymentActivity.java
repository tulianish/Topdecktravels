package com.dal.touristapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.braintreepayments.cardform.view.CardForm;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private final static String zipPattern = "([A-Za-z]\\d[A-Za-z][-]?\\d[A-Za-z]\\d)";
    private final static String expiryPattern = "\\d{2}\\/\\d{2}";
    private EditText cardNumber, expiration,cvv,zipCode,fullName;
    private Button getTicketBtn;
    private String apiUrl ="http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3002/payment/validate";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cardNumber = findViewById(R.id.cardNumber);
        expiration = findViewById(R.id.expiry);
        cvv = findViewById(R.id.cvv);
        zipCode = findViewById(R.id.zip);
        fullName = findViewById(R.id.fullName);
        getTicketBtn = findViewById(R.id.getTicket);
        cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 4 || s.toString().length() == 9 || s.toString().length() == 14){
                    cardNumber.append("-");
                }
            }
        });

        expiration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 2){
                    expiration.append("/");
                }
            }
        });
        zipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==3){
                    zipCode.append("-");
                }
            }
        });
        getTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(cardNumber.getText())) {
                    cardNumber.setError("Card Number is required");
                    cardNumber.requestFocus();
                } else if (!TextUtils.isDigitsOnly(cardNumber.getText().subSequence(0, 3)) || !TextUtils.isDigitsOnly(cardNumber.getText().subSequence(5, 8)) ||
                        !TextUtils.isDigitsOnly(cardNumber.getText().subSequence(10, 13)) || !TextUtils.isDigitsOnly(cardNumber.getText().subSequence(15, 18))) {
                    cardNumber.setError("Please Enter a valid card Number");
                    cardNumber.requestFocus();
                } else if (TextUtils.isEmpty(expiration.getText())) {
                    expiration.setError("Expiration Date is required");
                    expiration.requestFocus();
                } else if (expiration.getText().length() > 5) {
                    expiration.setError("Required format is mm/yy");
                    expiration.requestFocus();
                } else if (!TextUtils.isDigitsOnly(expiration.getText().subSequence(0, 1)) || !TextUtils.isDigitsOnly(expiration.getText().subSequence(3, 4))) {
                    expiration.setError("Please enter a valid expiration date");
                    expiration.requestFocus();
                } else if (TextUtils.isEmpty(cvv.getText())) {
                    cvv.setError("CVV is Required");
                } else if (!TextUtils.isDigitsOnly(cvv.getText()) || cvv.getText().length() > 3) {
                    cvv.setError("Please Enter a valid cvv");
                    cvv.requestFocus();
                } else if (TextUtils.isEmpty(zipCode.getText())) {
                    zipCode.setError("Postal Code is Required");
                    zipCode.requestFocus();
                } else if (zipCode.getText().length() > 7) {
                    zipCode.setError("Please enter a valid Postal Code ");
                } else if (!zipCode.getText().toString().matches(zipPattern)) {
                    zipCode.setError("Please Enter a valid Postal Code");
                }
                else if (TextUtils.isEmpty(fullName.getText())) {
                    fullName.setError("Name is Required");
                    fullName.requestFocus();
                }
                else {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("cardNumber", cardNumber.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(cardNumber.getText().toString());
                    Request request = new JsonObjectRequest(Request.Method.POST, apiUrl, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String res;
                            res = response.optString("message");
                            if (res.compareTo("success") == 0) {
                                JSONObject params = new JSONObject();
                                try {
                                    params.put("from_place", getIntent().getExtras().getString("from_place"));
                                    params.put("to_place", getIntent().getExtras().getString("to_place"));
                                    params.put("travel_date", getIntent().getExtras().getString("travel_date"));
                                    params.put("travel_time", getIntent().getExtras().getString("travel_time"));
                                    params.put("fare", getIntent().getExtras().getString("fare"));
                                    params.put("email", getIntent().getExtras().getString("email"));
                                    params.put("name", getIntent().getExtras().getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Request request1 = new JsonObjectRequest(Request.Method.POST, "http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3002/booking/createBooking", params, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println(response);
                                        if (response.optString("name") != null) {
                                            Intent intent = new Intent(PaymentActivity.this, TicketActivity.class);
                                            intent.putExtra("id", response.optString("_id"));
                                            intent.putExtra("travel_date", response.optString("travel_date"));
                                            intent.putExtra("travel_time", response.optString("travel_time"));
                                            intent.putExtra("from_place", response.optString("from_place"));
                                            intent.putExtra("to_place", response.optString("to_place"));
                                            intent.putExtra("email", response.optString("email"));
                                            startActivity(intent);

                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(request1);
                            } else {
                                cardNumber.setError("Please Enter a Valid card Number");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                }
            }
        });

    }

}
