package com.example.healthmeter;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    TextView tvBMI, tvCategory, tvTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvBMI = findViewById(R.id.tvBMI);
        tvCategory = findViewById(R.id.tvCategory);
        tvTip = findViewById(R.id.tvTip);

        float bmi = getIntent().getFloatExtra("bmi", 0f);
        String category = getIntent().getStringExtra("category");

        tvBMI.setText(String.format("Your BMI: %.2f", bmi));
        tvCategory.setText("Category: " + category);

        switch (category) {
            case "Underweight":
                tvTip.setText("Tip: Fuel up, stay strong! 💪");
                break;
            case "Normal":
                tvTip.setText("Awesome work! Stay consistent and healthy! 🎉");
                break;
            case "Overweight":
                tvTip.setText("Progress starts now — stay active, stay strong! 🏃‍♂️");
                break;
            case "Obese":
                tvTip.setText("One healthy choice at a time — your comeback starts now! 🥗");
                break;
            default:
                tvTip.setText("");
        }
    }
}
