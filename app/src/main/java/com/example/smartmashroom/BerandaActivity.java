                                                                    package com.example.smartmashroom;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BerandaActivity extends AppCompatActivity {

    DonutProgress humidityProgress, tempProgress;
    TextView tvHumidityText, tvTempText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        humidityProgress = findViewById(R.id.tvHumidityCircle);
        tempProgress = findViewById(R.id.tvTempCircle);

        tvHumidityText = findViewById(R.id.tvHumidityText);
        tvTempText = findViewById(R.id.tvTempText);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_profile) {
                    startActivity(new Intent(BerandaActivity.this, ProfilActivity.class));
                    return true;
                } else if (id == R.id.menu_fogging) {
                    startActivity(new Intent(BerandaActivity.this, PengembunanActivity.class));
                    return true;
                } else if (id == R.id.menu_notif) {
                    startActivity(new Intent(BerandaActivity.this, NotifikasiActivity.class));
                    return true;
                }

                return false;
            }
        });

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("sensor");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Float humidity = snapshot.child("kelembaban").getValue(Float.class);
                    Float temperature = snapshot.child("suhu").getValue(Float.class);

                    if (humidity != null) {
                        animateDonutProgress(humidityProgress, humidityProgress.getProgress(), humidity, "%");
                        tvHumidityText.setText("💧 Humidity\n  " + humidity + " %");
                    }

                    if (temperature != null) {
                        animateDonutProgress(tempProgress, tempProgress.getProgress(), temperature, "°C");
                        tvTempText.setText("🌡 Temp. Control\n  " + temperature + " °C");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error handling
            }
        });

    }

    private void animateDonutProgress(DonutProgress donut, float from, float to, String suffix) {
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(1500);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            donut.setProgress(value);
            donut.setText(String.format("%.1f %s", value, suffix));
        });
        animator.start();
    }
}
