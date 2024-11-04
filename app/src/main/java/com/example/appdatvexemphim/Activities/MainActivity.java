package com.example.appdatvexemphim.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.appdatvexemphim.Adapters.FilmListAdapter;
import com.example.appdatvexemphim.Adapters.FilmListAdapter2;
import com.example.appdatvexemphim.Adapters.SliderAdapter;
import com.example.appdatvexemphim.BroadcastReceiver.NetworkChangeReceiver;
import com.example.appdatvexemphim.Domains.Film;
import com.example.appdatvexemphim.Domains.SliderItems;
import com.example.appdatvexemphim.R;
import com.example.appdatvexemphim.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ChipNavigationBar chipNavigationBar;
    private Button logoutBtn;
    ActivityMainBinding binding;
    private FirebaseDatabase database;
    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem()+1);
        }
    };


    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();
    private boolean isReceiverRegistered = false;
    private boolean isNetworkAvailable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        // Write a message to the database
        database = FirebaseDatabase.getInstance();

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        chipNavigationBar = findViewById(R.id.chipNavigationBar);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {

                if (i == R.id.explorer) {
                    // Handle explorer click
                } else if (i == R.id.favorite) {
                    // Handle favorite click
                } else if (i == R.id.cart) {
                    // Handle cart click
                } else if (i == R.id.profile) {
                    // Chuyển đến giao diện Profile
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else {
                    throw new IllegalArgumentException("Unexpected value: " + i);
                }
            }
        });

        initBanner();
        initNowShowingMovies();
        initUpComingMovies();
    }




    public void logout() {
        // Xóa trạng thái đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        // Chuyển đến LoginActivity sau khi đăng xuất
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//        finish();
    }
    private void initNowShowingMovies(){
        DatabaseReference myRef= database.getReference("Items");
        binding.progressBartTop.setVisibility(View.VISIBLE);
        ArrayList<Film> items=new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        items.add(issue.getValue(Film.class));
                    }
                    if (!items.isEmpty()){
                        binding.recyclerViewTop.setLayoutManager(new LinearLayoutManager(MainActivity.this
                                ,LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerViewTop.setAdapter(new FilmListAdapter2(items));
                    }
                    binding.progressBartTop.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initUpComingMovies(){
        DatabaseReference myRef= database.getReference("Upcomming");
        binding.progressBarUp.setVisibility(View.VISIBLE);
        ArrayList<Film> items=new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        items.add(issue.getValue(Film.class));
                    }
                    if (!items.isEmpty()){
                        binding.recyclerViewUp.setLayoutManager(new LinearLayoutManager(MainActivity.this
                                ,LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerViewUp.setAdapter(new FilmListAdapter(items));
                    }
                    binding.progressBarUp.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initBanner(){
        DatabaseReference myRef= database.getReference("Banners");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items=new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private  void banners(ArrayList<SliderItems> items){
        binding.viewPager2.setAdapter(new SliderAdapter(items,binding.viewPager2));
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer= new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1 - Math.abs(position);
                page.setScaleY(0.85f + r*0.15f);

            }
        });

        binding.viewPager2.setPageTransformer(compositePageTransformer);
        binding.viewPager2.setCurrentItem(1);
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable    );
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
        if (!isReceiverRegistered) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(networkChangeReceiver, filter);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable , 1000);
        if (isReceiverRegistered) {
            unregisterReceiver(networkChangeReceiver);
            isReceiverRegistered = false;
        }
    }


}