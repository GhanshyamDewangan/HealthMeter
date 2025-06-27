package com.example.healthmeter;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class History extends AppCompatActivity {

    ListView listViewHistory;
    ArrayList<String> recordList;
    ArrayAdapter<String> adapter;

    DatabaseReference databaseReference;
    String userId = "HealthMeter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewHistory = findViewById(R.id.listViewHistory);
        recordList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.my_dropdown, recordList);
        listViewHistory.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("bmi_records");

        loadRecords();
    }

    private void loadRecords() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recordList.clear();
                if (!snapshot.exists()) {
                    recordList.add("No records found.");
                } else {
                    for (DataSnapshot recordSnapshot : snapshot.getChildren()) {
                        String name = recordSnapshot.child("name").getValue(String.class);
                        Integer age = recordSnapshot.child("age").getValue(Integer.class);
                        Double bmi = recordSnapshot.child("bmi").getValue(Double.class);
                        String category = recordSnapshot.child("category").getValue(String.class);

                        String item = "Name: " + name + "\nAge: " + age + "\nBMI: " + String.format("%.2f", bmi) + "\nCategory: " + category;
                        recordList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

