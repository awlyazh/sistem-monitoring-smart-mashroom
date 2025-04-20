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
            double suhu = getSuhu(txtSuhu);
            double kelembaban = getKelembaban(txtFeels);

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
                    cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.red_light));
                }
            } else {
                // Mode Manual
                btnManual.setEnabled(true);
                txtStatus.setText("Mode Manual: Atur Sendiri");
                cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow)); // Kuning
                cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow));
                updateManualUI(btnManual);
            }
        });

        // Button Manual ON/OFF
        btnManual.setOnClickListener(v -> {
            isManualOn = !isManualOn;
            updateManualUI(btnManual);
        });
    }

    // Update UI berdasarkan tombol manual
    private void updateManualUI(Button btnManual) {
        TextView statusView = findViewById(R.id.textStatus); // Ambil TextView untuk status

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

    // Ambil suhu dari TextView
    private double getSuhu(TextView txtSuhu) {
        String suhuText = txtSuhu.getText().toString().replace("°", "");
        return Double.parseDouble(suhuText);
    }

    // Ambil kelembaban dari TextView
    private double getKelembaban(TextView txtFeels) {
        String feelsText = txtFeels.getText().toString().split(":")[1].replace("%", "").trim();
        return Double.parseDouble(feelsText);
    }
}
