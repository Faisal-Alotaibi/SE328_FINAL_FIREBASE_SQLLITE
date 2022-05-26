package com.faisal.se328;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class List extends AppCompatActivity {
DatabaseReference myRef;
ListView listview;
ArrayList<String> arrayList = new ArrayList<>();
ArrayAdapter<String> arrayAdapter;
int indexCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //arrayList.add("TEST");
        myRef = FirebaseDatabase.getInstance("https://se328-7d1b7-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Students");
        listview = (ListView) findViewById(R.id.listviewtxt);
        arrayAdapter = new ArrayAdapter<String>(List.this, android.R.layout.simple_list_item_1,arrayList);
        //arrayList.add("TEST");
        listview.setAdapter(arrayAdapter);

        Button delBtn = (Button) findViewById(R.id.btnDelete);
        Button updateBtn = (Button) findViewById(R.id.btnUpdate);
        Button refreshBtn = (Button) findViewById(R.id.refreshBtn);
        Button backBtn = (Button) findViewById(R.id.backButn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(List.this , MainActivity.class));

            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(List.this , List.class));

            }
        });



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                indexCount=i+1;
                Log.w("Faisal-delete", indexCount+" ");

                //myRef.child((i+1)+"").removeValue();
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRecord(indexCount);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value= indexCount;
                Intent i = new Intent(List.this, updateActivity.class);
                i.putExtra("index",value);
                startActivity(i);            }
        });
        
        final Handler handler = new Handler();
        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                // data request
                arrayList.clear();
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.w("Faisal", previousChildName+" ");
                        if (previousChildName!=null) {

                            String value =(Integer.parseInt(previousChildName)+1)+ snapshot.getValue(Student.class).toString();
                            arrayList.add(value);
                            arrayAdapter.notifyDataSetChanged();
                        }else{
                            String value = 1 + snapshot.getValue(Student.class).toString();
                            arrayList.add(value);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        arrayAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        arrayAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        arrayAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        arrayAdapter.notifyDataSetChanged();

                    }
                });
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(refresh, 5000);




    }

    private void deleteRecord(int id){
        String idTXT =String.valueOf(id);
        myRef.child(idTXT).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Delete Toast
                   // Toast.makeText(List.this, "Record Deleted", Toast.LENGTH_SHORT).show();
                    Toasty.success(getBaseContext(), "Record Deleted",
                            Toast.LENGTH_SHORT, true).show();
                    arrayList.remove(id-1);
                    arrayAdapter.notifyDataSetChanged();
                }else{
                    //failed toast
                    Toasty.error(getBaseContext(), "Failed",
                            Toast.LENGTH_SHORT, true).show();
                    //Toast.makeText(List.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}