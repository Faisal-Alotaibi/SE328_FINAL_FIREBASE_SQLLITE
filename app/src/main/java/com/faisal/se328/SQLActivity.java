package com.faisal.se328;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class SQLActivity extends AppCompatActivity {
    DatabaseReference myrefer;

    EditText id ;
    EditText name;
    EditText sname;
    EditText fathername ;
    EditText nid ;
    EditText dob;
    EditText gender ;

    TextView cityTV;
    TextView tempTV;

    ImageView iconImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlactivity);

        DatabaseHelper db = new DatabaseHelper(this);
        myrefer = FirebaseDatabase.getInstance("https://se328-7d1b7-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Students");
        id = (EditText) findViewById(R.id.idET);
        name = (EditText) findViewById(R.id.nameField);
        sname = (EditText) findViewById(R.id.snameField);
        fathername = (EditText) findViewById(R.id.fathnameField);
        nid = (EditText) findViewById(R.id.nidField);
        dob = (EditText) findViewById(R.id.dateField);
        gender = (EditText) findViewById(R.id.genderField);

        cityTV = (TextView) findViewById(R.id.cityTV2);
        tempTV = (TextView) findViewById(R.id.tempTV);

        iconImg = (ImageView) findViewById(R.id.imageIcon);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String city = sp.getString("City","");
        String weatherWebserviceURL = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=9daa7e7592c4079e1c32d6c9bbb8a64d&units=metric";
        weather(weatherWebserviceURL);


        Button insert = (Button) findViewById(R.id.insertBtn);
        Button fetchBtn = (Button) findViewById(R.id.fetchBtn);
        Button viewBtn = (Button) findViewById(R.id.viewDbBtn);
        Button delBtn = (Button) findViewById(R.id.deleteBtn);
        Button updateBtn = (Button) findViewById(R.id.updateBtn);
        Button sqlFetchBtn = (Button) findViewById(R.id.sqlFetchBtn);

        Button back = (Button) findViewById(R.id.back2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SQLActivity.this , MainActivity.class));

            }
        });

        sqlFetchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor data = db.getSpecificResult(id.getText().toString());

                if (data.getCount() != 0){
                    while(data.moveToNext()) {
                        id.setText(data.getString(0));
                        name.setText(data.getString(1));
                        sname.setText(data.getString(2));
                        fathername.setText(data.getString(3));
                        nid.setText(data.getString(4));
                        dob.setText(data.getString(5));
                        gender.setText(data.getString(6));
                    }
                }else{
                    Toasty.info(getBaseContext(), "No results",
                            Toast.LENGTH_SHORT, true).show();
                }


            }
        });



        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteData(id.getText().toString());

                Toasty.success(getBaseContext(), "Record deleted from SQLLite",
                        Toast.LENGTH_SHORT, true).show();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.updateData(id.getText().toString(),
                        name.getText().toString(),
                        sname.getText().toString(),
                        fathername.getText().toString(),
                        nid.getText().toString(),
                        dob.getText().toString(),
                        gender.getText().toString())) {
                    Toasty.success(getBaseContext(), "Data updated",
                            Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.error(getBaseContext(), "Data failed to update",
                            Toast.LENGTH_SHORT, true).show();
                }
            }
        });


        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SQLActivity.this , SQLList.class));

            }
        });

        fetchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.getText().toString().equals("")){
                   // Toast.makeText(SQLActivity.this, "Please specify an ID", Toast.LENGTH_SHORT).show();
                    Toasty.error(getBaseContext(), "Please specify an ID",
                            Toast.LENGTH_SHORT, true).show();
                }else{
                    fetchStudent(id.getText().toString());
                }
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cursor data = db.getSpecificResult(id.getText().toString());
                //if(data != null && data.getCount() > 0 ){
                //    Toast.makeText(SQLActivity.this, "ID Already exists", Toast.LENGTH_SHORT).show();

               // }else {
                    if (db.addData(id.getText().toString(),
                            name.getText().toString(),
                            sname.getText().toString(),
                            fathername.getText().toString(),
                            nid.getText().toString(),
                            dob.getText().toString(),
                            gender.getText().toString())) {
                       // Toast.makeText(SQLActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                        Toasty.success(getBaseContext(), "Data Inserted",
                                Toast.LENGTH_SHORT, true).show();
                    } else {
                        //Toast.makeText(SQLActivity.this, "Data failed to insert", Toast.LENGTH_SHORT).show();
                        Toasty.error(getBaseContext(), "Data failed to insert",
                                Toast.LENGTH_SHORT, true).show();
                    }
               // }
            }
        });
    }

    public void fetchStudent(String SID){
        myrefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id.setText(""+SID);
                name.setText(snapshot.child(SID).child("name").getValue().toString());
                sname.setText(snapshot.child(SID).child("surname").getValue().toString());
                fathername.setText(snapshot.child(SID).child("fathname").getValue().toString());
                nid.setText(snapshot.child(SID).child("nid").getValue().toString());
                dob.setText(snapshot.child(SID).child("dob").getValue().toString());
                gender.setText(snapshot.child(SID).child("gender").getValue().toString());

                Toasty.success(getBaseContext(), "Student "+SID+" fetched",
                        Toast.LENGTH_SHORT, true).show();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                            cityTV.setText(town);

                            JSONObject jsonMain = response.getJSONObject("main");
                            JSONObject jsonClouds = response.getJSONObject("clouds");

                            double temp = jsonMain.getDouble("temp");
                            Log.d("Faisal-temp", String.valueOf(temp));
                            tempTV.setText("temp: "+String.valueOf(temp));

                            double feelstemp = jsonClouds.getDouble("all");
                            Log.d("Faisal-clouds",String.valueOf(feelstemp));
                           // cloudsTV.setText("Clouds: "+String.valueOf(feelstemp));

                            double humidNum = jsonMain.getDouble("humidity");
                            Log.d("Faisal-Humidity",String.valueOf(humidNum));
                           // humidTV.setText("Humidity: "+String.valueOf(humidNum));

                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SQLActivity.this);

                            String link = sp.getString("link","");

                            Glide.with(SQLActivity.this).load(link).into(iconImg);


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