package com.example.appdatvexemphim.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appdatvexemphim.R;

public class NoNetworkActivity extends AppCompatActivity {

    private Button retryButton;
    private Handler handler = new Handler();
    private Runnable checkNetworkRunnable = new Runnable() {
        @Override
        public void run() {
            if (isNetworkAvailable()) {
                Intent intent = new Intent(NoNetworkActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                handler.postDelayed(this, 5000); // Kiểm tra lại sau 5 giây
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);

        retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    Intent intent = new Intent(NoNetworkActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    handler.postDelayed(checkNetworkRunnable, 5000); // Kiểm tra lại sau 5 giây
                }
            }
        });

        handler.postDelayed(checkNetworkRunnable, 5000); // Kiểm tra lại sau 5 giây
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true;
                }
            }
        } else {
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkNetworkRunnable);
    }
}
