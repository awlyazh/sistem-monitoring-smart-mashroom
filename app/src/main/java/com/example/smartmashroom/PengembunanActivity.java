package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

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
    private TextView textStatus;
    private TextView textKondisi;
    private TextView textFeels; // Tambahan untuk menampilkan suhu dan kelembapan dari Firebase
    private View cardHumidity;
    private View cardTemp;

    private DatabaseReference mDatabase;

    private boolean isManualOn = false;
    private float currentSuhu = 0;
    private float currentKelembapan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ✅ HILANGKAN ACTION BAR PUTIH
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_pengembunan);

        switchAuto = findViewById(R.id.switchAuto);
        btnManual = findViewById(R.id.btnManual);
        textStatus = findViewById(R.id.textStatus);
        textKondisi = findViewById(R.id.textKondisi);
        textFeels = findViewById(R.id.textFeels); // Inisialisasi
        cardHumidity = findViewById(R.id.cardHumidity);
        cardTemp = findViewById(R.id.cardTemp);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        switchAuto.setOnCheckedChangeListener((buttonView, isChecked) -> updateMode(isChecked));
        btnManual.setOnClickListener(v -> toggleManualMode());

        monitorSensorData();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.menu_fogging);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });
    }

    private void monitorSensorData() {
        mDatabase.child("sensor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Double suhu = snapshot.child("suhu").getValue(Double.class);
                    Double kelembapan = snapshot.child("kelembapan").getValue(Double.class);

                    if (suhu != null) currentSuhu = suhu.floatValue();
                    if (kelembapan != null) currentKelembapan = kelembapan.floatValue();

                    // Format nilai suhu dan kelembapan ke 1 angka di belakang koma
                    String suhuText = String.format(Locale.US, "%.1f", currentSuhu);
                    String humidText = String.format(Locale.US, "%.1f", currentKelembapan);

                    // Tampilkan di TextView
                    textFeels.setText("Feels like: " + suhuText + "°\nHumidity: " + humidText + "%");

                    if (switchAuto.isChecked()) {
                        updateAutoControl();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                textStatus.setText("Gagal membaca data sensor.");
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
        if (currentSuhu < 24 || currentSuhu > 27 || currentKelembapan < 80 || currentKelembapan > 90) {
            mDatabase.child("status_pompa").setValue("ON");
            textStatus.setText("Kondisi Tidak Stabil, Pompa ON");
            textKondisi.setText("Kondisi Tidak Stabil");

            cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.card_humid_on));
            cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.card_temp_on));

            btnManual.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_on));
            btnManual.setText("ON");
        } else {
            mDatabase.child("status_pompa").setValue("OFF");
            textStatus.setText("Kondisi Stabil, Pompa OFF");
            textKondisi.setText("Kondisi Stabil");

            cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.card_off));
            cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.card_off));

            btnManual.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_off));
            btnManual.setText("OFF");
        }
    }

    private void toggleManualMode() {
        isManualOn = !isManualOn;
        mDatabase.child("pompa").child("status_pompa").setValue(isManualOn ? true : false);
        mDatabase.child("status_pompa").setValue(isManualOn ? "ON" : "OFF");
        updateManualUI();
    }

    private void updateManualUI() {
        if (isManualOn) {
            textStatus.setText("Pengabutan Manual: Aktif");
            btnManual.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_on));
            btnManual.setText("ON");
            textKondisi.setText("Mode Manual");

            cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.card_humid_on));
            cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.card_temp_on));
        } else {
            textStatus.setText("Pengabutan Manual: OFF");
            btnManual.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_off));
            btnManual.setText("OFF");
            textKondisi.setText("Mode Manual");

            cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.card_off));
            cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.card_off));
        }
    }
}
