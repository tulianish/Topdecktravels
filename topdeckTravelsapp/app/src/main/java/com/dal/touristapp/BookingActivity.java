package com.dal.touristapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    private EditText dateOftravel;
    private Spinner travelFrom, travelTime;
    private Button continueBtn, goToPay;
    private TextView price;
    private LinearLayout timeLayout;
    final Calendar myCalendar = Calendar.getInstance();
    String apiUrl = "http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3004/places/coordinate/";
    String priceUrl = "http://topdeckTravelsLoadBalancer-49374090.us-east-1.elb.amazonaws.com:3003/booking/getPrices";
    String travelFromPlace = null;
    SharedPreferences myPref;
    String bookTravelTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        dateOftravel = findViewById(R.id.travelDate);
        travelFrom = findViewById(R.id.spinner1);
        continueBtn = findViewById(R.id.btnGetPrice);
        price = findViewById(R.id.getPrice);
        goToPay = findViewById(R.id.btnContinue);
        timeLayout = findViewById(R.id.timelayout);
        travelTime = findViewById(R.id.spinner2);

        List<String> timeList = new ArrayList<>();
        timeList.add("9 am");
        timeList.add("12 pm");
        timeList.add("3 pm");
        timeList.add("6 pm");
        timeList.add("9 pm");

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,timeList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        travelTime.setAdapter(cityAdapter);
        travelTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookTravelTime = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Request request = new JsonArrayRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    List<String> cityList = new ArrayList<String>();
                    for(int i=0; i<response.length();i++){
                        JSONObject obj = response.getJSONObject(i);
                        String city = obj.getString("city");
                        System.out.println(city);
                        cityList.add(city);
                    }
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(BookingActivity.this,android.R.layout.simple_spinner_item,cityList);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    travelFrom.setAdapter(cityAdapter);
                    travelFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            travelFromPlace = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

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



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        dateOftravel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(BookingActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(dateOftravel.getText().toString())) {
                    dateOftravel.setError("Please select Travel Date");
                    dateOftravel.requestFocus();
                }
                else {
                    timeLayout.setVisibility(View.VISIBLE);
                    System.out.println(travelFromPlace);
                    JSONObject params = new JSONObject();
                    try {
                        params.put("travel_date", dateOftravel.getText().toString());
                        params.put("from_place", travelFromPlace);
                        params.put("to_place", getIntent().getExtras().getString("Travel_To"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Request request = new JsonObjectRequest(Request.Method.POST, priceUrl, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println(response);
                                String priceAmt = response.getString("price");
                                price.setText("Price is:" + priceAmt);
                                goToPay.setVisibility(View.VISIBLE);
                                goToPay.setText("Continue To Payment");

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
                }
            }
        });

        goToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                if(myPref.getString("email",null)!=null){
                    Intent intent = new Intent(BookingActivity.this,PaymentActivity.class);
                    intent.putExtra("from_place", travelFromPlace);
                    intent.putExtra("to_place", getIntent().getExtras().getString("Travel_To"));
                    intent.putExtra("travel_date", dateOftravel.getText().toString());
                    System.out.println(price.getText().subSequence(9,price.getText().length()));
                    intent.putExtra("name", myPref.getString("name",null));
                    intent.putExtra("fare", price.getText().subSequence(9,price.getText().length()));
                    intent.putExtra("email", myPref.getString("email",null));
                    intent.putExtra("travel_time", bookTravelTime );
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(BookingActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    private void updateLabel() {
        Calendar calendar = Calendar.getInstance();
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(calendar.getTime().compareTo(myCalendar.getTime())>0){
            dateOftravel.setError("Please select a date later than today");
        }
        else {
            dateOftravel.setText(sdf.format(myCalendar.getTime()));
        }
    }
}
