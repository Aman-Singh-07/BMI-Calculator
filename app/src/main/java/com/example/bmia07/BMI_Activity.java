package com.example.bmia07;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.text.DecimalFormat;
import com.google.android.material.button.MaterialButton;

public class BMI_Activity extends AppCompatActivity {
    MaterialButton recalculate, shareBtn;
    TextView bmi_display, bmi_category, gender, bmiDesc, bmiAdvice;
    ImageView imageView;
    CardView background;

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        if(getSupportActionBar() != null) getSupportActionBar().hide();

        bmi_display = findViewById(R.id.bmi_display);
        bmi_category = findViewById(R.id.bmi_category);
        gender = findViewById(R.id.gender_display);
        background = findViewById(R.id.content_layout);
        imageView = findViewById(R.id.imageView);
        recalculate = findViewById(R.id.recalculate_bmi);
        bmiDesc = findViewById(R.id.bmi_difference_desc);
        bmiAdvice = findViewById(R.id.bmi_advice);
        shareBtn = findViewById(R.id.share_bmi);

        Intent in = getIntent();
        String height = in.getStringExtra("height");
        String weight = in.getStringExtra("weight");
        String genderStr = in.getStringExtra("gender");

        if (height == null || weight == null || genderStr == null) {
            Toast.makeText(this, "Missing data!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        float intBmi;
        try {
            float intHeight = Float.parseFloat(height) / 100;
            float intWeight = Float.parseFloat(weight);
            intBmi = intWeight / (intHeight * intHeight);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input format for height or weight!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DecimalFormat df = new DecimalFormat("#.##");
        String bmi = df.format(intBmi);
        bmi_display.setText(bmi);
        gender.setText(genderStr);

        // Set BMI category and visuals
        if (intBmi <= 16) {
            bmi_category.setText("Severely Underweight");
            background.setCardBackgroundColor(0xFFFF00FF); // Magenta
            imageView.setImageResource(R.drawable.crisis);
        } else if (intBmi <= 18.4) {
            bmi_category.setText("Underweight");
            background.setCardBackgroundColor(0xFF2196F3); // Blue
            imageView.setImageResource(R.drawable.underweight);
        } else if (intBmi <= 24.9) {
            bmi_category.setText("Normal (Healthy)");
            background.setCardBackgroundColor(0xFF00C853); // Green
            imageView.setImageResource(R.drawable.normal);
        } else if (intBmi <= 29.9) {
            bmi_category.setText("Overweight");
            background.setCardBackgroundColor(0xFFFFC107); // Amber
            imageView.setImageResource(R.drawable.overweight);
        } else if (intBmi <= 34.9) {
            bmi_category.setText("Obese Class I");
            background.setCardBackgroundColor(0xFFF44336); // Red
            imageView.setImageResource(R.drawable.obesity);
        } else if (intBmi <= 39.9) {
            bmi_category.setText("Obese Class II");
            background.setCardBackgroundColor(0xFF9E9E9E); // Gray
            imageView.setImageResource(R.drawable.obesity2);
        } else {
            bmi_category.setText("Obese Class III");
            background.setCardBackgroundColor(0xFF8D6E63); // Brown
            imageView.setImageResource(R.drawable.scales);
        }

        // Gender-specific BMI range
        float normalLow, normalHigh;
        String rangeStr;
        if ("Male".equalsIgnoreCase(genderStr)) {
            normalLow = 20.0f;
            normalHigh = 25.0f;
            rangeStr = "Normal BMI Range for Male: 20.0 - 25.0";
        } else if ("Female".equalsIgnoreCase(genderStr)) {
            normalLow = 18.5f;
            normalHigh = 24.9f;
            rangeStr = "Normal BMI Range for Female: 18.5 - 24.9";
        } else {
            normalLow = 18.5f;
            normalHigh = 24.9f;
            rangeStr = "Normal BMI Range: 18.5 - 24.9";
        }

        String diffText;
        if (intBmi < normalLow) {
            float diff = normalLow - intBmi;
            diffText = String.format("You are %.2f below the normal BMI.", diff);
        } else if (intBmi > normalHigh) {
            float diff = intBmi - normalHigh;
            diffText = String.format("You are %.2f above the normal BMI.", diff);
        } else {
            diffText = "You are within the normal BMI range.";
        }

        bmiDesc.setText(diffText + "\n" + rangeStr);

        // Health advice
        String advice = getBmiAdvice(intBmi);
        bmiAdvice.setText(advice);

        // Share BMI result
        shareBtn.setOnClickListener(view -> {
            String shareText = "My BMI is " + bmi + " (" + bmi_category.getText().toString() + "). " + bmiDesc.getText().toString();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(intent, "Share your BMI result"));
        });

        recalculate.setOnClickListener(v -> {
            Intent i = new Intent(BMI_Activity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    @NonNull
    private static String getBmiAdvice(float intBmi) {
        String advice;
        if (intBmi <= 16) {
            advice = "Your BMI is very low. Please consult a healthcare professional for guidance on healthy weight gain.";
        } else if (intBmi <= 18.4) {
            advice = "You are underweight. Try to eat a balanced diet with sufficient calories.";
        } else if (intBmi <= 24.9) {
            advice = "Great! Maintain your healthy lifestyle.";
        } else if (intBmi <= 29.9) {
            advice = "You are slightly overweight. Consider regular exercise and a balanced diet.";
        } else if (intBmi <= 34.9) {
            advice = "You are in Obese Class I. It's important to consult a doctor for a personalized plan.";
        } else if (intBmi <= 39.9) {
            advice = "You are in Obese Class II. Seek medical advice for a safe weight loss plan.";
        } else {
            advice = "You are in Obese Class III. Immediate medical attention is recommended.";
        }
        return advice;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Exits the whole app
    }
}