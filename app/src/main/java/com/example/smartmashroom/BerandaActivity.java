package com.example.smartmashroom;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.animation.ValueAnimator;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BerandaActivity extends AppCompatActivity {

    DonutProgress humidityProgress, tempProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        // Inisialisasi DonutProgress
        humidityProgress = findViewById(R.id.tvHumidityCircle);
        tempProgress = findViewById(R.id.tvTempCircle);

        // Animasi DonutProgress
        animateDonutProgress(humidityProgress, 0f, 90f, "%");
        animateDonutProgress(tempProgress, 0f, 25.1f, "°C");

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_profile) {
                    Intent intent = new Intent(BerandaActivity.this, ProfilActivity.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });
    }

    private void animateDonutProgress(DonutProgress donut, float from, float to, String suffix) {
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(1500); // durasi animasi 1.5 detik
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            donut.setProgress(value);
            donut.setText(String.format("%.1f %s", value, suffix));
        });
        animator.start();
    }
}
