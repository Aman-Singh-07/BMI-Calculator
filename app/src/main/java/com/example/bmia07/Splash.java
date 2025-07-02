package com.example.bmia07;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar() != null) getSupportActionBar().hide();

        ImageView logo = findViewById(R.id.bmi_logo);
        @SuppressLint("ResourceType") Animation anim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        logo.startAnimation(anim);

        new Handler().postDelayed(() -> {
            Intent in = new Intent(Splash.this, MainActivity.class);
            startActivity(in);
            finish();
        }, 1500);
    }
}