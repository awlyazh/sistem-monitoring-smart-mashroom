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

        // ====== ✅ Bottom Navigation Tetap Ada ======
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_fogging);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                // Mengarah ke BerandaActivity
                startActivity(new Intent(this, BerandaActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.menu_fogging) {
                // Tetap di PengembunanActivity
                return true;
            } else if (itemId == R.id.menu_notif) {
                // Mengarah ke NotifikasiActivity
                startActivity(new Intent(this, NotifikasiActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.menu_profile) {
                // Mengarah ke ProfilActivity
                startActivity(new Intent(this, ProfilActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        // ====== ✅ Inisialisasi View ======
        Switch switchMode = findViewById(R.id.switchAuto);
        Button btnManual = findViewById(R.id.btnManual);
        TextView txtStatus = findViewById(R.id.textStatus);
        TextView txtFoggingStatus = findViewById(R.id.textFoggingStatus);
        TextView txtSuhu = findViewById(R.id.textSuhu);
        TextView txtFeels = findViewById(R.id.textFeels);
        LinearLayout cardHumidity = findViewById(R.id.cardHumidity);
        LinearLayout cardTemp = findViewById(R.id.cardTemp);

        // ====== ✅ Awal Manual OFF ======
        isManualOn = false;
        updateManualUI(btnManual, txtFoggingStatus);

        // ====== ✅ Switch Otomatis/Manual ======
        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            double suhu = getSuhu(txtSuhu);
            double kelembaban = getKelembaban(txtFeels);

            if (isChecked) {
                // AUTO MODE
                btnManual.setEnabled(false);
                txtStatus.setText("Mode Otomatis");

                if (suhu < 24 || kelembaban < 80) {
                    txtFoggingStatus.setText("Pengembunan: ON");
                    txtFoggingStatus.setTextColor(Color.GREEN);
                    txtStatus.setText("Pengembunan Aktif (Otomatis)");
                    cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.green)); // Hijau
                    cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.blue)); // Biru
                } else {
                    txtFoggingStatus.setText("Pengembunan: OFF");
                    txtFoggingStatus.setTextColor(Color.RED);
                    txtStatus.setText("Kondisi Stabil, Pengembunan OFF");
                    cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.red_light)); // Merah Muda
                    cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.red_light));
                }
            } else {
                // MANUAL MODE
                btnManual.setEnabled(true);
                txtStatus.setText("Mode Manual: Atur Sendiri");
                cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow)); // Kuning
                cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow));
                updateManualUI(btnManual, txtFoggingStatus);
            }
        });

        // ====== ✅ Button Manual ON/OFF ======
        btnManual.setOnClickListener(v -> {
            isManualOn = !isManualOn;
            updateManualUI(btnManual, txtFoggingStatus);
        });
    }

    // Update UI berdasarkan tombol manual
    private void updateManualUI(Button btnManual, TextView statusView) {
        if (isManualOn) {
            btnManual.setText("ON");
            btnManual.setBackgroundResource(R.drawable.bg_button_on);
            statusView.setText("Pengembunan: ON");
            statusView.setTextColor(Color.GREEN);
        } else {
            btnManual.setText("OFF");
            btnManual.setBackgroundResource(R.drawable.bg_button_off);
            statusView.setText("Pengembunan: OFF");
            statusView.setTextColor(Color.RED);
        }
    }

    // Ambil nilai suhu dari TextView
    private double getSuhu(TextView txtSuhu) {
        try {
            String suhuStr = txtSuhu.getText().toString().replace("°", "").trim();
            return Double.parseDouble(suhuStr);
        } catch (Exception e) {
            return 0;
        }
    }

    // Ambil kelembaban dari TextView
    private double getKelembaban(TextView txtFeels) {
        try {
            String feelsText = txtFeels.getText().toString();
            int index = feelsText.indexOf("Humidity:");
            if (index != -1) {
                String kelembabanStr = feelsText.substring(index + 9).replace("%", "").trim();
                return Double.parseDouble(kelembabanStr);
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }
}
