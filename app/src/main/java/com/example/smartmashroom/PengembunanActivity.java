package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class PengembunanActivity extends AppCompatActivity {

    private Switch switchAuto;
    private Button btnManual;
    private TextView textStatus, textKondisi, textFeels, textHumidity, textTempControl, textStatusHumidity, textStatusTemp;
    private LinearLayout layoutHumidity, layoutTemp;

    private DatabaseReference mDatabase;

    private boolean isManualOn = false;
    private float currentSuhu = 0;
    private float currentKelembapan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_pengembunan);

        // Inisialisasi UI
        switchAuto = findViewById(R.id.switchAuto);
        btnManual = findViewById(R.id.btnManual);
        textStatus = findViewById(R.id.textStatus);
        textKondisi = findViewById(R.id.textKondisi);
        textFeels = findViewById(R.id.textFeels);
        textHumidity = findViewById(R.id.textHumidity);
        textTempControl = findViewById(R.id.textTempControl);
        textStatusHumidity = findViewById(R.id.textStatusHumidity);
        textStatusTemp = findViewById(R.id.textStatusTemp);
        layoutHumidity = findViewById(R.id.layoutHumidity);
        layoutTemp = findViewById(R.id.layoutTemp);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Event listener
        switchAuto.setOnCheckedChangeListener((buttonView, isChecked) -> updateMode(isChecked));
        btnManual.setOnClickListener(v -> toggleManualMode());

        monitorSensorData();

        // Bottom nav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.menu_fogging);

        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_home) {
                startActivity(new Intent(PengembunanActivity.this, BerandaActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.menu_fogging) {
                return true;
            } else if (id == R.id.menu_notif) {
                startActivity(new Intent(PengembunanActivity.this, NotifikasiActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.menu_profile) {
                startActivity(new Intent(PengembunanActivity.this, ProfilActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void monitorSensorData() {
        DatabaseReference sensorRef = mDatabase.child("sensor");

        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Double suhu = snapshot.child("suhu").getValue(Double.class);
                    Double kelembaban = snapshot.child("kelembaban").getValue(Double.class);

                    if (suhu != null && kelembaban != null) {
                        currentSuhu = suhu.floatValue();
                        currentKelembapan = kelembaban.floatValue();

                        String suhuText = String.format(Locale.US, "%.1f", currentSuhu);
                        String humidText = String.format(Locale.US, "%.1f", currentKelembapan);

                        textFeels.setText("Feels like: " + suhuText + "°\nHumidity: " + humidText + "%");
                        textHumidity.setText(humidText + " %");
                        textTempControl.setText(suhuText + " °C");

                        if (switchAuto.isChecked()) {
                            updateAutoControl();
                        }
                    } else {
                        textStatus.setText("Data suhu atau kelembapan null.");
                    }
                } else {
                    textStatus.setText("Data sensor tidak ditemukan.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textStatus.setText("Gagal membaca data sensor: " + error.getMessage());
            }
        });
    }

    private void updateMode(boolean isAutomatic) {
        if (isAutomatic) {
            mDatabase.child("pompa").child("mode_pompa").setValue("otomatis");
            btnManual.setEnabled(false);
            isManualOn = false;
            updateAutoControl();
        } else {
            mDatabase.child("pompa").child("mode_pompa").setValue("manual");
            btnManual.setEnabled(true);
            textStatus.setText("Mode Manual: Atur Sendiri");
            textKondisi.setText("Mode Manual");
            updateManualUI();
        }
    }

    private void updateAutoControl() {
        boolean kondisiTidakStabil = currentSuhu < 24 || currentSuhu > 27 || currentKelembapan < 80 || currentKelembapan > 90;

        if (kondisiTidakStabil) {
            mDatabase.child("status_pompa").setValue("ON");
            textStatus.setText("Kondisi Tidak Stabil, Pompa ON");
            textKondisi.setText("Kondisi Tidak Stabil");

            layoutHumidity.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_card_humidity));
            layoutTemp.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_card_temp));
            textStatusHumidity.setText("Status: On");
            textStatusTemp.setText("Status: On");

            btnManual.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_on));
            btnManual.setText("ON");
        } else {
            mDatabase.child("status_pompa").setValue("OFF");
            textStatus.setText("Kondisi Stabil, Pompa OFF");
            textKondisi.setText("Kondisi Stabil");

            layoutHumidity.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_card_humidity_off));
            layoutTemp.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_card_temp_off));
            textStatusHumidity.setText("Status: Off");
            textStatusTemp.setText("Status: Off");

            btnManual.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_off));
            btnManual.setText("OFF");
        }
    }

    private void toggleManualMode() {
        isManualOn = !isManualOn;
        mDatabase.child("pompa").child("status_pompa").setValue(isManualOn);
        mDatabase.child("status_pompa").setValue(isManualOn ? "ON" : "OFF");
        updateManualUI();
    }

    private void updateManualUI() {
        if (isManualOn) {
            textStatus.setText("Pengabutan Manual: Aktif");
            btnManual.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_on));
            btnManual.setText("ON");
            textKondisi.setText("Mode Manual");

            layoutHumidity.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_card_humidity));
            layoutTemp.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_card_temp));
            textStatusHumidity.setText("Status: On");
            textStatusTemp.setText("Status: On");
        } else {
            textStatus.setText("Pengabutan Manual: OFF");
            btnManual.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_off));
            btnManual.setText("OFF");
            textKondisi.setText("Mode Manual");

            layoutHumidity.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_card_humidity_off));
            layoutTemp.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_card_temp_off));
            textStatusHumidity.setText("Status: Off");
            textStatusTemp.setText("Status: Off");
        }
    }
}
