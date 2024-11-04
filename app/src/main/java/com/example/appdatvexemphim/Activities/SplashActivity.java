package com.example.appdatvexemphim.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        AppPreferences appPreferences = new AppPreferences(this);
//       /// kiểm tra đangư nhâpj
//        if (isLoggedIn) {
//            startActivity(new Intent(this, MainActivity.class));
//        } else {
//            startActivity(new Intent(this, IntroActivity.class));
//        }
//        finish();
        //
        if (appPreferences.isFirstTime()) {
            appPreferences.setFirstTime(false);
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Điều hướng đến MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}