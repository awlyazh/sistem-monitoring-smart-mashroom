package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InformasiActivity extends AppCompatActivity {

    DonutProgress humidityCircle, tempCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi);

        // Inisialisasi indikator lingkaran
        humidityCircle = findViewById(R.id.humidityCircle);
        tempCircle = findViewById(R.id.tempCircle);

        // Data statis contoh
        setHumidity(90.0);
        setTemperature(25.1);

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_info); // Item yang aktif di halaman ini

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                startActivity(new Intent(this, BerandaActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.menu_info) {
                return true; // Tetap di halaman ini
            } else if (itemId == R.id.menu_fogging) {
                startActivity(new Intent(this, PengembunanActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.menu_notif) {
                startActivity(new Intent(this, NotifikasiActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.menu_profile) {
                startActivity(new Intent(this, ProfilActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void setHumidity(double humidity) {
        humidityCircle.setProgress((float) humidity);
        humidityCircle.setText(String.format("%.1f %%", humidity));
    }

    private void setTemperature(double temperature) {
        tempCircle.setProgress((float) temperature);
        tempCircle.setText(String.format("%.1f °C", temperature));
    }
}
