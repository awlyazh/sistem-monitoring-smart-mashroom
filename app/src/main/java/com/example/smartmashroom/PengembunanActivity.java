package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PengembunanActivity extends AppCompatActivity {

    private boolean isManualOn = false;
    private DatabaseReference mDatabase;
    private double currentSuhu = 0.0;
    private double currentKelembapan = 0.0;

    private TextView txtStatus, txtSuhu, txtFeels;
    private LinearLayout cardHumidity, cardTemp;
    private Button btnManual;
    private Switch switchMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengembunan);

        mDatabase = FirebaseDatabase.getInstance().getReference();

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
        switchMode = findViewById(R.id.switchAuto);
        btnManual = findViewById(R.id.btnManual);
        txtStatus = findViewById(R.id.textStatus);
        txtSuhu = findViewById(R.id.textSuhu);
        txtFeels = findViewById(R.id.textFeels);
        cardHumidity = findViewById(R.id.cardHumidity);
        cardTemp = findViewById(R.id.cardTemp);

        isManualOn = false;
        updateManualUI();

        // Mode Otomatis/Manual
        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> updateMode());

        // Tombol Manual
        btnManual.setOnClickListener(v -> {
            isManualOn = !isManualOn;
            updateManualUI();
            txtStatus.setText(isManualOn ? "Manual: Pengembunan Dinyalakan" : "Manual: Pengembunan Dimatikan");

            // Update ke Firebase
            mDatabase.child("status_pompa").setValue(isManualOn ? "ON" : "OFF");
        });

        // Ambil data dari Firebase secara real-time
        getDataFromFirebaseRealtime();
    }

    private void getDataFromFirebaseRealtime() {
        mDatabase.child("kelembapan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentKelembapan = dataSnapshot.getValue(Double.class);
                    txtFeels.setText("Kelembapan: " + currentKelembapan + "%");
                    updateMode();
                } else {
                    Toast.makeText(PengembunanActivity.this, "Data kelembapan tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PengembunanActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase.child("suhu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentSuhu = dataSnapshot.getValue(Double.class);
                    txtSuhu.setText("Suhu: " + currentSuhu + "°C");
                    updateMode();
                } else {
                    Toast.makeText(PengembunanActivity.this, "Data suhu tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PengembunanActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase.child("status_pompa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String statusPompa = dataSnapshot.getValue(String.class);
                    txtStatus.setText("Status Pompa: " + statusPompa);
                } else {
                    Toast.makeText(PengembunanActivity.this, "Data status pompa tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PengembunanActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMode() {
        if (switchMode.isChecked()) {
            // Mode Otomatis
            btnManual.setEnabled(false);

            if (currentSuhu < 24 && currentKelembapan < 80) {
                txtStatus.setText("Pengembunan Aktif (Otomatis)");
                cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
                cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            } else {
                txtStatus.setText("Kondisi Stabil, Pengembunan OFF");
                cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.red_light));
                cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.red_light));
            }
        } else {
            // Mode Manual
            btnManual.setEnabled(true);
            txtStatus.setText("Mode Manual: Atur Sendiri");
            cardHumidity.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow));
            cardTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow));
            updateManualUI();
        }
    }

    private void updateManualUI() {
        if (isManualOn) {
            btnManual.setText("ON");
            btnManual.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        } else {
            btnManual.setText("OFF");
            btnManual.setBackgroundColor(ContextCompat.getColor(this, R.color.red_light));
        }
    }
}
