package com.example.smartmashroom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PengembunanActivity extends AppCompatActivity {

    private boolean isManualOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengembunan);

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_fogging);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                startActivity(new Intent(this, BerandaActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.menu_fogging) {
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

        // Inisialisasi View
        Switch switchMode = findViewById(R.id.switchAuto);
        Button btnManual = findViewById(R.id.btnManual);
        TextView txtStatus = findViewById(R.id.textStatus);
        TextView txtSuhu = findViewById(R.id.textSuhu);
        TextView txtFeels = findViewById(R.id.textFeels);
        LinearLayout cardHumidity = findViewById(R.id.cardHumidity);
        LinearLayout cardTemp = findViewById(R.id.cardTemp);

        // Mode Manual OFF Awal
        isManualOn = false;
        updateManualUI(btnManual);

        // Switch Otomatis/Manual
        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            double suhu = getSuhu();
            double kelembaban = getKelembaban();

            if (isChecked) {
                // Mode Otomatis
                btnManual.setEnabled(false);
                txtStatus.setText("Mode Otomatis");

                if (suhu < 24 || kelembaban < 80) {
                    txtStatus.setText("Pengembunan Aktif (Otomatis)");
                    cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.green)); // Hijau
                    cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.blue)); // Biru
                } else {
                    txtStatus.setText("Kondisi Stabil, Pengembunan OFF");
                    cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.red_light)); // Merah Muda
                    cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.red_light)); // Merah Muda
                }
            } else {
                // Mode Manual
                btnManual.setEnabled(true);
                txtStatus.setText("Mode Manual: Atur Sendiri");
                // Warna tetap hijau dan biru untuk kartu, meskipun mode manual
                cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.green)); // Hijau
                cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.blue)); // Biru
                updateManualUI(btnManual);
            }
        });

        // Tombol Manual
        btnManual.setOnClickListener(v -> {
            isManualOn = !isManualOn;
            updateManualUI(btnManual);
            if (isManualOn) {
                txtStatus.setText("Manual: Pengembunan Dinyalakan");
            } else {
                txtStatus.setText("Manual: Pengembunan Dimatikan");
            }
        });
    }

    // Metode untuk mendapatkan suhu
    private double getSuhu() {
        // Ganti dengan logika pengambilan suhu
        return 25.1;  // Misalnya suhu sekarang 25.1
    }

    // Metode untuk mendapatkan kelembaban
    private double getKelembaban() {
        // Ganti dengan logika pengambilan kelembaban
        return 85.0;  // Misalnya kelembaban 85%
    }

    // Metode untuk memperbarui status tombol manual
    private void updateManualUI(Button btnManual) {
        if (isManualOn) {
            btnManual.setText("ON");
            btnManual.setBackgroundColor(ContextCompat.getColor(this, R.color.green)); // Hijau
        } else {
            btnManual.setText("OFF");
            btnManual.setBackgroundColor(ContextCompat.getColor(this, R.color.red_light)); // Merah Muda
        }
    }
}
