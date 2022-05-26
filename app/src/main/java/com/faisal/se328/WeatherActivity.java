package com.faisal.se328;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {
    EditText city;
    TextView tempTV;
    TextView humidTV;
    TextView feelsTV;
    TextView description;

    ImageView iconImg;

    Button weatherBtn;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        city = (EditText) findViewById(R.id.cityET);
        tempTV = (TextView) findViewById(R.id.temperature);
        humidTV = (TextView) findViewById(R.id.humid);
        feelsTV = (TextView) findViewById(R.id.clouds);
        description = (TextView) findViewById(R.id.desc);

        iconImg = (ImageView) findViewById(R.id.iconImg);


        weatherBtn = (Button) findViewById(R.id.weatherBtn);
        backBtn = (Button) findViewById(R.id.backBtn);

        String weatherWebserviceURL = "https://api.openweathermap.org/data/2.5/weather?q=Berlin&appid=9daa7e7592c4079e1c32d6c9bbb8a64d&units=metric";
        weather(weatherWebserviceURL);
        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weatherWebserviceURL = "https://api.openweathermap.org/data/2.5/weather?q=" + city.getText().toString() + "&appid=9daa7e7592c4079e1c32d6c9bbb8a64d&units=metric";
                weather(weatherWebserviceURL);

                editor.putString("City", city.getText().toString());
                editor.commit();

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WeatherActivity.this, MainActivity.class));

            }
        });


    }

    public void weather(String url) {

        JsonObjectRequest jsonObj =
                new JsonObjectRequest(Request.Method.GET,
                        url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Faisal", "Response received");
                        Log.d("Faisal", response.toString());

                        try {
                            String town = response.getString("name");
                            Log.d("Faisal-town", town);
                            description.setText(town);

                            JSONObject jsonMain = response.getJSONObject("main");
                            JSONObject jsonClouds = response.getJSONObject("clouds");

                            double temp = jsonMain.getDouble("temp");
                            Log.d("Faisal-temp", String.valueOf(temp));
                            tempTV.setText("temp: " + String.valueOf(temp));

                            double feelstemp = jsonClouds.getDouble("all");
                            Log.d("Faisal-feelsLike", String.valueOf(feelstemp));
                            feelsTV.setText("Clouds: " + String.valueOf(feelstemp));

                            double humidNum = jsonMain.getDouble("humidity");
                            Log.d("Faisal-Humidity", String.valueOf(humidNum));
                            humidTV.setText("Humidity: " + String.valueOf(humidNum));


                            JSONArray jArray = response.getJSONArray("weather");
                            for (int i = 0; i < jArray.length(); i++) {
                                try {
                                    Log.d("Faisal-array", jArray.getString(i));
                                    JSONObject oneObject = jArray.getJSONObject(i);
                                    String icon = oneObject.getString("icon");

                                    String link = "https://openweathermap.org/img/w/" + icon + ".png";
                                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("link", link);
                                    editor.commit();
                                    Glide.with(WeatherActivity.this).load(link).into(iconImg);

                                } catch (JSONException e) {
                                    // Oops
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Receive Error", e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Faisal", "ERROR RETRIEVING URL");
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObj);
    }
}