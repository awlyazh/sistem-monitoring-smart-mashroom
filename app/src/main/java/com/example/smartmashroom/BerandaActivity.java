package com.example.smartmashroom;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
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
    TextView tvHumidityText, tvTempText, tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        humidityProgress = findViewById(R.id.tvHumidityCircle);
        tempProgress = findViewById(R.id.tvTempCircle);

        tvHumidityText = findViewById(R.id.tvHumidityText);
        tvTempText = findViewById(R.id.tvTempText);
        tvStatus = findViewById(R.id.tvStatus); // Tambahkan TextView status

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

                    boolean isHumidityStable = false;
                    boolean isTempStable = false;

                    if (humidity != null) {
                        isHumidityStable = humidity == 80.0f;
                        int color = isHumidityStable ? Color.parseColor("#2196F3") : Color.RED;
                        humidityProgress.setFinishedStrokeColor(color);
                        animateDonutProgress(humidityProgress, humidityProgress.getProgress(), humidity, "%");
                        tvHumidityText.setText("💧 Humidity\n  " + humidity + " %");
                    }

                    if (temperature != null) {
                        isTempStable = temperature >= 24.0f && temperature <= 27.0f;
                        int color = isTempStable ? Color.parseColor("#2196F3") : Color.RED;
                        tempProgress.setFinishedStrokeColor(color);
                        animateDonutProgress(tempProgress, tempProgress.getProgress(), temperature, "°C");
                        tvTempText.setText("🌡 Temp. Control\n  " + temperature + " °C");
                    }

                    // Atur status keseluruhan
                    if (isHumidityStable && isTempStable) {
                        tvStatus.setText("Kelembaban Udara dan Suhu Udara Stabil");
                        tvStatus.setBackgroundColor(Color.parseColor("#457BAA")); // warna biru
                    } else {
                        tvStatus.setText("Kelembaban Udara dan Suhu Udara Tidak Stabil");
                        tvStatus.setBackgroundColor(Color.RED); // warna merah
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
