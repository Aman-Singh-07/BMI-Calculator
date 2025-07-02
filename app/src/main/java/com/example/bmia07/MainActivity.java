package com.example.bmia07;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.material.snackbar.Snackbar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView currentHeight, currentAge, currentWeight, heightUnit, weightUnit;
    ImageView incrementAge, incrementWeight, decrementAge, decrementWeight, infoIcon;
    ImageView maleLogo, femaleLogo;
    SeekBar seekBarForHeight;
    CardView maleCard, femaleCard;
    android.widget.Button calculateBmi;
    SwitchCompat unitSwitch;

    int intWeight = 55;
    int intAge = 20;
    int currentProgress = 170;

    boolean isMetric = true; // true: kg/cm, false: lbs/in
    String selectedGender = "0"; // "0" means not selected

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null) getSupportActionBar().hide();

        initViews();
        setDefaultValues();
        setupListeners();
    }

    @SuppressLint("WrongViewCast")
    private void initViews() {
        calculateBmi = findViewById(R.id.calculate_bmi);
        currentAge = findViewById(R.id.current_age);
        currentWeight = findViewById(R.id.current_weight);
        currentHeight = findViewById(R.id.current_height);

        incrementAge = findViewById(R.id.increment_age);
        incrementWeight = findViewById(R.id.increment_weight);
        decrementAge = findViewById(R.id.decrement_age);
        decrementWeight = findViewById(R.id.decrement_weight);

        seekBarForHeight = findViewById(R.id.seekbar_for_height);

        maleCard = findViewById(R.id.male_card);
        femaleCard = findViewById(R.id.female_card);

        infoIcon = findViewById(R.id.info_icon);
        unitSwitch = findViewById(R.id.unit_switch);
        heightUnit = findViewById(R.id.height_unit);
        weightUnit = findViewById(R.id.weight_unit);

        maleLogo = findViewById(R.id.male_logo);
        femaleLogo = findViewById(R.id.female_logo);

        // Custom thumb and track color for SwitchCompat
        unitSwitch.setThumbTintList(ContextCompat.getColorStateList(this, R.color.switch_thumb_selector));
        unitSwitch.setTrackTintList(ContextCompat.getColorStateList(this, R.color.switch_track_selector));
    }

    @SuppressLint("SetTextI18n")
    private void setDefaultValues() {
        currentAge.setText(String.format(Locale.getDefault(), "%d", intAge));
        currentWeight.setText(String.format(Locale.getDefault(), "%d", intWeight));
        currentHeight.setText(String.format(Locale.getDefault(), "%d", intProgressMetric()));
        seekBarForHeight.setMax(275 - 50); // min height = 50, so range = 225
        seekBarForHeight.setProgress(intProgressMetric() - 50); // show 170 as default
        heightUnit.setText("cm");
        weightUnit.setText("kg");
        unitSwitch.setChecked(false); // metric by default

        maleLogo.setSelected(false);
        femaleLogo.setSelected(false);
    }

    @SuppressLint("SetTextI18n")
    private void setupListeners() {
        maleCard.setOnClickListener(v -> {
            maleCard.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.purplePrimary));
            femaleCard.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            ((TextView) maleCard.findViewById(R.id.text_male)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            ((TextView) femaleCard.findViewById(R.id.text_female)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.purplePrimary));
            maleLogo.setSelected(true);
            femaleLogo.setSelected(false);
            selectedGender = "Male";
        });

        femaleCard.setOnClickListener(v -> {
            femaleCard.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.purplePrimary));
            maleCard.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            ((TextView) femaleCard.findViewById(R.id.text_female)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            ((TextView) maleCard.findViewById(R.id.text_male)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.purplePrimary));
            femaleLogo.setSelected(true);
            maleLogo.setSelected(false);
            selectedGender = "Female";
        });

        seekBarForHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isMetric) {
                    currentProgress = progress + 50;
                } else {
                    currentProgress = progress + 20;
                }
                currentHeight.setText(String.format(Locale.getDefault(), "%d", currentProgress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        incrementAge.setOnClickListener(v -> {
            intAge++;
            currentAge.setText(String.format(Locale.getDefault(), "%d", intAge));
        });

        decrementAge.setOnClickListener(v -> {
            if (intAge > 1) {
                intAge--;
                currentAge.setText(String.format(Locale.getDefault(), "%d", intAge));
            }
        });

        incrementWeight.setOnClickListener(v -> {
            intWeight++;
            currentWeight.setText(String.format(Locale.getDefault(), "%d", intWeight));
        });

        decrementWeight.setOnClickListener(v -> {
            if (intWeight > 1) {
                intWeight--;
                currentWeight.setText(String.format(Locale.getDefault(), "%d", intWeight));
            }
        });

        infoIcon.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("What is BMI?")
                .setMessage("BMI (Body Mass Index) is a measure of body fat based on height and weight. " +
                        "It helps assess if you are in a healthy weight range.\n" +
                        "Note: BMI does not account for muscle mass and other factors.\n\n" +
                        "Version: 1.0 \n" +
                        "Developer: Aman Singh")
                .setPositiveButton("OK", null)
                .show());

        unitSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Switch to lbs/in
                isMetric = false;
                heightUnit.setText("in");
                weightUnit.setText("lbs");
                intWeight = Math.round(intWeight * 2.20462f);
                currentWeight.setText(String.format(Locale.getDefault(), "%d", intWeight));
                currentProgress = Math.round(intProgressMetric() / 2.54f);
                currentHeight.setText(String.format(Locale.getDefault(), "%d", currentProgress));
                seekBarForHeight.setMax(108 - 20); // 20in to 108in
                seekBarForHeight.setProgress(currentProgress - 20);
            } else {
                // Switch to kg/cm
                isMetric = true;
                heightUnit.setText("cm");
                weightUnit.setText("kg");
                intWeight = Math.round(intWeight / 2.20462f);
                currentWeight.setText(String.format(Locale.getDefault(), "%d", intWeight));
                currentProgress = Math.round(intProgressImperial() * 2.54f);
                currentHeight.setText(String.format(Locale.getDefault(), "%d", currentProgress));
                seekBarForHeight.setMax(275 - 50); // 50cm to 275cm
                seekBarForHeight.setProgress(currentProgress - 50);
            }
        });

        calculateBmi.setOnClickListener(v -> {
            if (selectedGender.equals("0")) {
                Snackbar.make(findViewById(android.R.id.content), "Select Your Gender First", Snackbar.LENGTH_SHORT).show();
            } else if ((isMetric && (currentProgress < 50 || currentProgress > 275)) ||
                    (!isMetric && (currentProgress < 20 || currentProgress > 108))) {
                Snackbar.make(findViewById(android.R.id.content),
                        isMetric ? "Please enter a valid height (50-275cm)" : "Please enter a valid height (20-108in)",
                        Snackbar.LENGTH_SHORT).show();
            } else if (intAge < 5 || intAge > 120) {
                Snackbar.make(findViewById(android.R.id.content), "Please enter a valid age (5-120)", Snackbar.LENGTH_SHORT).show();
            } else if ((isMetric && (intWeight < 10 || intWeight > 300)) ||
                    (!isMetric && (intWeight < 22 || intWeight > 661))) {
                Snackbar.make(findViewById(android.R.id.content),
                        isMetric ? "Please enter a valid weight (10-300kg)" : "Please enter a valid weight (22-661lbs)",
                        Snackbar.LENGTH_SHORT).show();
            } else {
                int sendHeight = currentProgress;
                int sendWeight = intWeight;
                if (!isMetric) {
                    sendHeight = Math.round(currentProgress * 2.54f); // in to cm
                    sendWeight = Math.round(intWeight / 2.20462f); // lbs to kg
                }
                Intent in = new Intent(MainActivity.this, BMI_Activity.class);
                in.putExtra("gender", selectedGender);
                in.putExtra("height", String.valueOf(sendHeight));
                in.putExtra("weight", String.valueOf(sendWeight));
                in.putExtra("age", String.valueOf(intAge));
                startActivity(in);
            }
        });
    }

    // Helper for metric height (cm)
    private int intProgressMetric() {
        if (isMetric) return currentProgress;
        else return Math.round(currentProgress * 2.54f);
    }

    // Helper for imperial height (in)
    private int intProgressImperial() {
        if (!isMetric) return currentProgress;
        else return Math.round(currentProgress / 2.54f);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Exits the whole app
    }
}