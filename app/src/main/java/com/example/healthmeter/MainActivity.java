package com.example.healthmeter;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    EditText etName, etAge, etHeight, etWeight;
    Spinner spHeightUnit, spWeightUnit;
    RadioGroup rgGender;
    Button btnCalculate;
    TextView tvResult;

    DatabaseReference databaseReference;
    String userId = "HealthMeter";

    String lastName = "";
    int lastAge = 0;
    float lastBMI = 0f;
    String lastCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        spHeightUnit = findViewById(R.id.spHeightUnit);
        spWeightUnit = findViewById(R.id.spWeightUnit);
        rgGender = findViewById(R.id.rgGender);
        btnCalculate = findViewById(R.id.btnCalculate);

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("bmi_records");

        setSpinners();
        btnCalculate.setOnClickListener(v -> calculateBMI());
    }

    private void setSpinners() {
        ArrayList<String> heightOptions = new ArrayList<>(Arrays.asList("Select Parameter", "CM", "Inches"));
        ArrayAdapter<String> heightAdapter = new ArrayAdapter<>(this, R.layout.slect_item, heightOptions);
        heightAdapter.setDropDownViewResource(R.layout.my_dropdown);
        spHeightUnit.setAdapter(heightAdapter);
        spHeightUnit.setSelection(0);
        spHeightUnit.setPopupBackgroundResource(android.R.color.white);

        ArrayList<String> weightOptions = new ArrayList<>(Arrays.asList("Select Parameter", "KG", "LBS"));
        ArrayAdapter<String> weightAdapter = new ArrayAdapter<>(this, R.layout.slect_item, weightOptions);
        weightAdapter.setDropDownViewResource(R.layout.my_dropdown);
        spWeightUnit.setAdapter(weightAdapter);
        spWeightUnit.setSelection(0);
        spWeightUnit.setPopupBackgroundResource(android.R.color.white);
    }

    private void calculateBMI() {
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String heightUnit = spHeightUnit.getSelectedItem().toString();
        String weightUnit = spWeightUnit.getSelectedItem().toString();

        if (name.isEmpty() || ageStr.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()
                || heightUnit.equals("Select Parameter") || weightUnit.equals("Select Parameter")) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        float height = Float.parseFloat(heightStr);
        float weight = Float.parseFloat(weightStr);

        if (heightUnit.equals("CM")) height /= 100;
        else if (heightUnit.equals("Inches")) height *= 0.0254;

        if (weightUnit.equals("LBS")) weight *= 0.453592;

        float bmi = weight / (height * height);
        String category = getBMICategory(bmi);

        String recordId = databaseReference.push().getKey();
        Map<String, Object> record = new HashMap<>();
        record.put("name", name);
        record.put("age", age);
        record.put("bmi", bmi);
        record.put("category", category);
        record.put("weight", weight);
        record.put("height", height);

        databaseReference.child(recordId).setValue(record)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, Result.class);
                    intent.putExtra("bmi", bmi);
                    intent.putExtra("category", category);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save data.", Toast.LENGTH_SHORT).show());
    }

    private String getBMICategory(float bmi) {
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 24.9) return "Normal";
        else if (bmi < 29.9) return "Overweight";
        else return "Obese";
    }

    // Bottom Nav functions
    public void onHomeClick(View view) {
        Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
    }

    public void onHistoryClick(View view) {
        Intent intent = new Intent(MainActivity.this, History.class);
        startActivity(intent);
    }

}
