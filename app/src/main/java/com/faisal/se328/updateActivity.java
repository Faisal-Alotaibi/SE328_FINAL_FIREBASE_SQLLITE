package com.faisal.se328;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class updateActivity extends AppCompatActivity {
    DatabaseReference myref;
    EditText id;
    EditText name;
    EditText sname;
    EditText fathername;
    EditText nid;
    EditText dob;
    EditText gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        myref = FirebaseDatabase.getInstance("https://se328-7d1b7-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Students");

        Bundle extras = getIntent().getExtras();
        int value = extras.getInt("index");

        id = (EditText) findViewById(R.id.idET);
        name = (EditText) findViewById(R.id.nameField);
        sname = (EditText) findViewById(R.id.snameField);
        fathername = (EditText) findViewById(R.id.fathnameField);
        nid = (EditText) findViewById(R.id.nidField);
        dob = (EditText) findViewById(R.id.dateField);
        gender = (EditText) findViewById(R.id.genderField);

        Button update = (Button) findViewById(R.id.updBtn);
        Button fetchBtn = (Button) findViewById(R.id.fetchBtn);


        //id.setText(myref.child(String.valueOf(value)).toString());
        //name.setText(myref.child(String.valueOf(value)).child("name"));
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id.setText("" + value);
                name.setText(snapshot.child(String.valueOf(value)).child("name").getValue().toString());
                sname.setText(snapshot.child(String.valueOf(value)).child("surname").getValue().toString());
                fathername.setText(snapshot.child(String.valueOf(value)).child("fathname").getValue().toString());
                nid.setText(snapshot.child(String.valueOf(value)).child("nid").getValue().toString());
                dob.setText(snapshot.child(String.valueOf(value)).child("dob").getValue().toString());
                gender.setText(snapshot.child(String.valueOf(value)).child("gender").getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStudent();
            }
        });


        fetchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchStudent(id.getText().toString());
            }
        });
    }


    public void updateStudent() {
        //myref.child(id.getText().toString())
        Log.w("Faisal-delete", "Reached else");
        myref.child(id.getText().toString()).child("name").setValue(name.getText().toString());
        myref.child(id.getText().toString()).child("surname").setValue(sname.getText().toString());
        myref.child(id.getText().toString()).child("fathname").setValue(fathername.getText().toString());
        myref.child(id.getText().toString()).child("nid").setValue(nid.getText().toString());
        myref.child(id.getText().toString()).child("dob").setValue(dob.getText().toString());
        myref.child(id.getText().toString()).child("gender").setValue(gender.getText().toString());
        Toasty.success(getBaseContext(), "Record Updated!",
                Toast.LENGTH_SHORT, true).show();
        //Toast.makeText(updateActivity.this, "Record Updated!", Toast.LENGTH_SHORT).show();

    }


    public void fetchStudent(String SID) {
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id.setText("" + SID);
                name.setText(snapshot.child(SID).child("name").getValue().toString());
                sname.setText(snapshot.child(SID).child("surname").getValue().toString());
                fathername.setText(snapshot.child(SID).child("fathname").getValue().toString());
                nid.setText(snapshot.child(SID).child("nid").getValue().toString());
                dob.setText(snapshot.child(SID).child("dob").getValue().toString());
                gender.setText(snapshot.child(SID).child("gender").getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}