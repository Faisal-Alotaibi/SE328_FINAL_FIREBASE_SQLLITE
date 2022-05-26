package com.faisal.se328;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    TextView description;
    TextView humidTV;
    TextView tempTV;
    TextView cloudsTV;

    ImageView iconImg;
    private long stdCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText id = (EditText) findViewById(R.id.idTV);
        EditText name = (EditText) findViewById(R.id.nameTV);
        EditText surname = (EditText) findViewById(R.id.snameTV);
        EditText fathername = (EditText) findViewById(R.id.fathnameTV);
        EditText nid = (EditText) findViewById(R.id.nidTV);
        EditText dob = (EditText) findViewById(R.id.dateTV);
        EditText gender = (EditText) findViewById(R.id.genderET);

        iconImg = (ImageView) findViewById(R.id.imgIcon);

        description = (TextView) findViewById(R.id.desc);
        humidTV = (TextView) findViewById(R.id.humid);
        tempTV = (TextView) findViewById(R.id.temperature);
        cloudsTV = (TextView) findViewById(R.id.clouds);


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String city = sp.getString("City","");

        String weatherWebserviceURL = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=9daa7e7592c4079e1c32d6c9bbb8a64d&units=metric";
        weather(weatherWebserviceURL);


        //id.setFocusable(false);
        id.setEnabled(false);

        Button insertBtn = (Button) findViewById(R.id.button);
        Button viewBtn = (Button) findViewById(R.id.button2);
        Button sqlBtn = (Button) findViewById(R.id.sqlBtn);
        Button weatherBtn = (Button) findViewById(R.id.wBtn);


        sqlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , SQLActivity.class));
            }
        });

        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , WeatherActivity.class));
            }
        });



        // Write a message to the database https://se328-7d1b7-default-rtdb.asia-southeast1.firebasedatabase.app/
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://se328-7d1b7-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Students");

        //myRef.child("1").removeValue();

        // Read from the database
       myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                if(dataSnapshot.exists()){
                    stdCount=(dataSnapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Faisal", "Failed to read value.", error.toException());
            }
        });

       insertBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (name.getText().toString().equals("") || surname.getText().toString().equals("") || fathername.getText().toString().equals("") || nid.getText().toString().equals("") || dob.getText().toString().equals("") || gender.getText().toString().equals("")){
                   //Toast.makeText(MainActivity.this, "Please fill all fields except id", Toast.LENGTH_SHORT).show();
                   Toasty.error(getBaseContext(), "Please fill all fields except id",
                           Toast.LENGTH_SHORT, true).show();
               }else {

                   Student student = new Student(name.getText().toString(), surname.getText().toString(), fathername.getText().toString(), nid.getText().toString(), dob.getText().toString(), gender.getText().toString());
                   myRef.child("" + (stdCount + 1)).setValue(student);
                   //Toast.makeText(MainActivity.this, "INSERTED", Toast.LENGTH_SHORT).show();
                   Toasty.success(getBaseContext(), "INSERTED",
                           Toast.LENGTH_SHORT, true).show();
                   name.setText("");
                   surname.setText("");
                   fathername.setText("");
                   nid.setText("");
                   dob.setText("");
                   gender.setText("");
               }

           }
       });

       viewBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(MainActivity.this , List.class));
           }
       });

    }


    private void deleteRecord(String id){
        DatabaseReference myRef = database.getReference("Students");
        myRef.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Delete Toast

                }else{
                    //failed toast
                }
            }
        });
    }

    public void weather(String url){

        JsonObjectRequest jsonObj =
                new JsonObjectRequest(Request.Method.GET,
                        url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Faisal", "Response received");
                        Log.d("Faisal" , response.toString());

                        try {
                            String town = response.getString("name");
                            Log.d("Faisal-town", town);
                            description.setText(town);

                            JSONObject jsonMain = response.getJSONObject("main");
                            JSONObject jsonClouds = response.getJSONObject("clouds");

                            double temp = jsonMain.getDouble("temp");
                            Log.d("Faisal-temp", String.valueOf(temp));
                            tempTV.setText("temp: "+String.valueOf(temp));

                            double feelstemp = jsonClouds.getDouble("all");
                            Log.d("Faisal-clouds",String.valueOf(feelstemp));
                            cloudsTV.setText("Clouds: "+String.valueOf(feelstemp));

                            double humidNum = jsonMain.getDouble("humidity");
                            Log.d("Faisal-Humidity",String.valueOf(humidNum));
                            humidTV.setText("Humidity: "+String.valueOf(humidNum));

                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                            String link = sp.getString("link","");

                            Glide.with(MainActivity.this).load(link).into(iconImg);


                        }catch (JSONException e){
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