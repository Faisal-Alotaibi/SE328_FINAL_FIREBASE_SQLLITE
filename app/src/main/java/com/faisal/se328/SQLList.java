package com.faisal.se328;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class SQLList extends AppCompatActivity {
    ListView listview;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqllist);
        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<String> arrayList = new ArrayList<>();

        Button back = (Button) findViewById(R.id.buttonBck);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SQLList.this, SQLActivity.class));

            }
        });
        listview = (ListView) findViewById(R.id.lvtv);
        arrayAdapter = new ArrayAdapter<String>(SQLList.this, android.R.layout.simple_list_item_1, arrayList);
        //arrayList.add("TEST");

        listview.setAdapter(arrayAdapter);

        Cursor data = db.getListContents();

        if (data.getCount() != 0) {
            while (data.moveToNext()) {
                arrayList.add("ID: " + data.getString(0) + "\nName: " + data.getString(1) + "\nSurname: " + data.getString(2) + "\nFather Name: " + data.getString(3) + "\nNationalID: " + data.getString(4) + "\nDate of birth: " + data.getString(5) + "\nGender: " + data.getString(6));
                arrayAdapter.notifyDataSetChanged();
            }
        } else {
            //Toast.makeText(this, "Database is empty", Toast.LENGTH_LONG).show();
            Toasty.info(getBaseContext(), "Database is empty",
                    Toast.LENGTH_SHORT, true).show();
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] s = adapterView.getItemAtPosition(i).toString().split("\\r?\\n");
                Toasty.info(getBaseContext(), s[1] + "\n" + s[2], Toast.LENGTH_SHORT, true).show();
            }
        });

    }
}