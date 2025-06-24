package com.example.smartmashroom;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BerandaActivity extends AppCompatActivity {

    DonutProgress humidityProgress, tempProgress;
    TextView tvHumidityText, tvTempText, tvStatus, tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        humidityProgress = findViewById(R.id.tvHumidityCircle);
        tempProgress = findViewById(R.id.tvTempCircle);

        tvHumidityText = findViewById(R.id.tvHumidityText);
        tvTempText = findViewById(R.id.tvTempText);
        tvStatus = findViewById(R.id.tvStatus);
        tvDate = findViewById(R.id.tvDate); // Realtime tanggal & waktu

        updateDateTime(); // Menjalankan waktu realtime

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
                        isHumidityStable = humidity >= 80.0f && humidity <= 90.0f;
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

                    // Logika status lengkap
                    if (isHumidityStable && isTempStable) {
                        tvStatus.setText("Kelembaban Udara dan Suhu Udara Stabil");
                        tvStatus.setBackgroundColor(Color.parseColor("#457BAA")); // Biru stabil
                    } else if (!isHumidityStable && isTempStable) {
                        tvStatus.setText("Kelembaban Udara Tidak Stabil");
                        tvStatus.setBackgroundColor(Color.RED);
                    } else if (isHumidityStable && !isTempStable) {
                        tvStatus.setText("Suhu Udara Tidak Stabil");
                        tvStatus.setBackgroundColor(Color.RED);
                    } else {
                        tvStatus.setText("Kelembaban dan Suhu Udara Tidak Stabil");
                        tvStatus.setBackgroundColor(Color.RED);
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

    private void updateDateTime() {
        Handler handler = new Handler(Looper.getMainLooper());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
                String currentDateTime = sdf.format(new Date());
                tvDate.setText(currentDateTime);

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);
    }
}
